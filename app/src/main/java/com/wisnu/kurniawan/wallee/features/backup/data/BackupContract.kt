package com.wisnu.kurniawan.wallee.features.backup.data

import com.wisnu.kurniawan.wallee.foundation.datasource.local.KHARJYAR_DATABASE_NAME
import com.wisnu.kurniawan.wallee.foundation.datasource.local.KHARJYAR_DATABASE_VERSION

/**
 * Stable metadata used to recognize a KharjYar database backup.
 * Keep this synchronized with the exported Room schema whenever the schema version changes.
 */
object BackupContract {
    const val DATABASE_NAME = KHARJYAR_DATABASE_NAME
    const val DATABASE_VERSION = KHARJYAR_DATABASE_VERSION
    const val ROOM_IDENTITY_HASH = "c7461b85a32f3164856cd24fbb176831"
    const val BACKUP_EXTENSION = ".khybackup"

    val REQUIRED_TABLES = setOf(
        "AccountDb",
        "TransactionDb",
        "AccountRecordDb",
        "TransactionRecordDb",
        "room_master_table",
    )
}
