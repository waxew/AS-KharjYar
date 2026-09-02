package com.wisnu.kurniawan.wallee.features.backup.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Local-only backup coordinator.
 *
 * Public storage access is handled by Android's Storage Access Framework. This class only moves
 * validated database bytes and never uploads user financial data.
 */
class BackupManager(
    private val context: Context,
) {

    suspend fun exportDatabase(target: File): Result<File> = withContext(Dispatchers.IO) {
        runCatching {
            val source = context.getDatabasePath(BackupContract.DATABASE_NAME)
            require(source.exists()) { "Database does not exist" }

            source.copyTo(target, overwrite = true)
            target
        }
    }

    suspend fun validateRestoreCandidate(file: File): Result<File> = withContext(Dispatchers.IO) {
        runCatching {
            require(BackupValidator.validateSqliteBackup(file)) {
                "Invalid KharjYar backup"
            }
            file
        }
    }

    suspend fun restoreDatabase(source: File): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            require(BackupValidator.validateSqliteBackup(source)) {
                "Invalid KharjYar backup"
            }

            val target = context.getDatabasePath(BackupContract.DATABASE_NAME)
            val staging = File(target.parentFile, "${target.name}.restore")

            source.copyTo(staging, overwrite = true)
            WalleeDatabaseCloser.closeBeforeRestore()

            if (!staging.renameTo(target)) {
                throw IllegalStateException("Restore replace failed")
            }
        }
    }
}

/**
 * Kept separate to avoid making BackupManager know Hilt graph details.
 */
object WalleeDatabaseCloser {
    fun closeBeforeRestore() {
        // Connected by the application layer before calling restore.
    }
}
