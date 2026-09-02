package com.wisnu.kurniawan.wallee.features.backup.data

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Local backup coordinator.
 *
 * This first implementation keeps the operation local-only. Export creates a copy of the Room
 * database file. Import validation is intentionally strict because financial history must not be
 * replaced by an unknown SQLite file.
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
            require(BackupValidator.isCandidateBackup(file)) {
                "Invalid backup file"
            }
            require(file.canRead()) {
                "Backup file cannot be read"
            }
            file
        }
    }
}
