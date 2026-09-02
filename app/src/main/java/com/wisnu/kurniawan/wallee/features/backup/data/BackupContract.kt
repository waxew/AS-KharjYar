package com.wisnu.kurniawan.wallee.features.backup.data

/**
 * AS Team local backup contract.
 *
 * Backup files must keep the database identity and schema version so restore can reject
 * incompatible files instead of replacing user data with an invalid database.
 */
object BackupContract {
    const val DATABASE_NAME = "wallee-db"
    const val DATABASE_VERSION = 1
    const val BACKUP_EXTENSION = ".khybackup"
}
