package com.wisnu.kurniawan.wallee.features.backup.data

import android.database.sqlite.SQLiteDatabase
import java.io.File

/**
 * Strict restore guard for KharjYar financial data.
 *
 * A file name is never trusted by itself. The candidate must be a healthy SQLite database with
 * the expected Room user version, required tables and the exact Room identity hash.
 */
object BackupValidator {
    fun isCandidateBackup(file: File): Boolean {
        return file.exists() &&
            file.isFile &&
            file.length() > 0 &&
            file.name.endsWith(BackupContract.BACKUP_EXTENSION, ignoreCase = true)
    }

    fun validateSqliteBackup(file: File): Boolean {
        if (!file.exists() || !file.isFile || file.length() <= 0) return false

        return runCatching {
            var database: SQLiteDatabase? = null
            try {
                database = SQLiteDatabase.openDatabase(
                    file.absolutePath,
                    null,
                    SQLiteDatabase.OPEN_READONLY,
                )

                val integrityOk = database.rawQuery("PRAGMA integrity_check", null).use { cursor ->
                    cursor.moveToFirst() && cursor.getString(0).equals("ok", ignoreCase = true)
                }

                val versionOk = database.rawQuery("PRAGMA user_version", null).use { cursor ->
                    cursor.moveToFirst() && cursor.getInt(0) == BackupContract.DATABASE_VERSION
                }

                val tables = database.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table'",
                    null,
                ).use { cursor ->
                    buildSet {
                        while (cursor.moveToNext()) add(cursor.getString(0))
                    }
                }

                val identityOk = database.rawQuery(
                    "SELECT identity_hash FROM room_master_table WHERE id = 42 LIMIT 1",
                    null,
                ).use { cursor ->
                    cursor.moveToFirst() && cursor.getString(0) == BackupContract.ROOM_IDENTITY_HASH
                }

                integrityOk &&
                    versionOk &&
                    identityOk &&
                    BackupContract.REQUIRED_TABLES.all(tables::contains)
            } finally {
                database?.close()
            }
        }.getOrDefault(false)
    }
}
