package com.wisnu.kurniawan.wallee.features.backup.data

import java.io.File

/**
 * Minimal restore guard. The final importer must additionally validate SQLite tables before copy.
 */
object BackupValidator {
    fun isCandidateBackup(file: File): Boolean {
        return file.exists() &&
            file.isFile &&
            file.length() > 0 &&
            file.name.endsWith(BackupContract.BACKUP_EXTENSION)
    }
}
