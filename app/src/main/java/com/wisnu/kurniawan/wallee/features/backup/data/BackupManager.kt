package com.wisnu.kurniawan.wallee.features.backup.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import com.wisnu.kurniawan.wallee.foundation.datasource.local.WalleeDatabase
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Local-only backup and restore coordinator for KharjYar.
 *
 * Public storage access is delegated to Android's Storage Access Framework. Financial data never
 * leaves the device unless the user explicitly selects a destination document.
 */
class BackupManager(
    private val context: Context,
) {

    /** Exports a consistent database snapshot to a user-selected SAF document. */
    suspend fun exportDatabase(destination: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            checkpointDatabase()

            val source = context.getDatabasePath(BackupContract.DATABASE_NAME)
            require(source.exists() && source.length() > 0) { "Database does not exist" }

            val output = context.contentResolver.openOutputStream(destination, "w")
                ?: error("Cannot open backup destination")

            source.inputStream().use { input ->
                output.use { stream ->
                    input.copyTo(stream)
                    stream.flush()
                }
            }
            Unit
        }
    }

    /**
     * Copies a user-selected document into private cache before validation. The SAF document name
     * is not trusted, so the staged copy receives KharjYar's controlled extension locally.
     */
    suspend fun stageRestore(source: Uri): Result<File> = withContext(Dispatchers.IO) {
        runCatching {
            val staged = File(
                context.cacheDir,
                "kharjyar-restore-${System.currentTimeMillis()}${BackupContract.BACKUP_EXTENSION}",
            )

            try {
                val input = context.contentResolver.openInputStream(source)
                    ?: error("Cannot open selected backup")
                input.use { stream ->
                    FileOutputStream(staged, false).use { output ->
                        stream.copyTo(output)
                        output.fd.sync()
                    }
                }

                require(BackupValidator.validateSqliteBackup(staged)) {
                    "Invalid or incompatible KharjYar backup"
                }
                staged
            } catch (error: Throwable) {
                staged.delete()
                throw error
            }
        }
    }

    /**
     * Replaces the live Room database only after validation and creates a rollback file first.
     * Call [restartApplication] immediately after success because existing injected DAO instances
     * belong to the closed Room database.
     */
    suspend fun restoreDatabase(stagedBackup: File): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            require(BackupValidator.validateSqliteBackup(stagedBackup)) {
                "Invalid or incompatible KharjYar backup"
            }

            val target = context.getDatabasePath(BackupContract.DATABASE_NAME)
            val parent = target.parentFile ?: error("Database directory unavailable")
            parent.mkdirs()

            val incoming = File(parent, "${target.name}.incoming")
            val rollback = File(parent, "${target.name}.rollback")
            incoming.delete()
            rollback.delete()

            // Write and fsync an incoming copy in the same filesystem before touching live data.
            stagedBackup.inputStream().use { input ->
                FileOutputStream(incoming, false).use { output ->
                    input.copyTo(output)
                    output.fd.sync()
                }
            }
            require(BackupValidator.validateSqliteBackup(incoming)) {
                "Staged restore validation failed"
            }

            checkpointDatabase()
            WalleeDatabase.closeInstanceForRestore()
            deleteSidecarFiles(target)

            var oldDatabaseMoved = false
            try {
                if (target.exists()) {
                    check(target.renameTo(rollback)) { "Cannot create restore rollback" }
                    oldDatabaseMoved = true
                }

                check(incoming.renameTo(target)) { "Cannot activate restored database" }
                rollback.delete()
                stagedBackup.delete()
            } catch (error: Throwable) {
                incoming.delete()
                if (oldDatabaseMoved && rollback.exists() && !target.exists()) {
                    rollback.renameTo(target)
                }
                throw error
            }
            Unit
        }
    }

    /** Schedules a clean launcher restart after Room has been replaced on disk. */
    fun restartApplication() {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: return
        launchIntent.addFlags(
            android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK,
        )

        val immutableFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            RESTART_REQUEST_CODE,
            launchIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or immutableFlag,
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + RESTART_DELAY_MS,
            pendingIntent,
        )
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    private fun checkpointDatabase() {
        val database = WalleeDatabase.getInstance(context)
        database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").use { cursor ->
            while (cursor.moveToNext()) {
                // Consume the cursor so SQLite completes the checkpoint before file copying.
            }
        }
    }

    private fun deleteSidecarFiles(database: File) {
        File(database.absolutePath + "-wal").delete()
        File(database.absolutePath + "-shm").delete()
    }

    private companion object {
        const val RESTART_REQUEST_CODE = 4017
        const val RESTART_DELAY_MS = 300L
    }
}
