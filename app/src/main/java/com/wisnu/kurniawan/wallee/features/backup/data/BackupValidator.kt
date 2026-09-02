package com.wisnu.kurniawan.wallee.features.backup.data

import android.database.sqlite.SQLiteDatabase
import java.io.File

/**
 * Restore guard for financial data.
 *
 * File name and extension alone are not trusted. SQLite integrity, schema version and required
 * Room tables must match before a restore operation can continue.
 */
object BackupValidator {
    fun isCandidateBackup(file: File): Boolean {
        return file.exists() &&
            file.isFile &&
            file.length() > 0 &&
            file.name.endsWith(BackupContract.BACKUP_EXTENSION)
    }

    fun validateSqliteBackup(file: File): Boolean {
        if (!isCandidateBackup(file)) return false

        return runCatching {
            val database = SQLiteDatabase.openDatabase(
                file.absolutePath,
                null,
                SQLiteDatabase.OPEN_READONLY,
            )

            val integrity = database.rawQuery("PRAGMA integrity_check", null)
                .use { cursor ->
                    cursor.moveToFirst() && cursor.getString(0) == "ok"
                }

            val version = database.rawQuery("PRAGMA user_version", null)
                .use { cursor ->
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

            database.close()

            integrity && version && BackupContract.REQUIRED_TABLES.all(tables::contains)
        }.getOrDefault(false)
    }
}
