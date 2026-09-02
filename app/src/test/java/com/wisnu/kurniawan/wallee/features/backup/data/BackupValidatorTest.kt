package com.wisnu.kurniawan.wallee.features.backup.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import java.io.File
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BackupValidatorTest {

    private lateinit var context: Context
    private lateinit var workingDir: File

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        workingDir = File(context.cacheDir, "backup-validator-test").apply {
            deleteRecursively()
            mkdirs()
        }
    }

    @After
    fun tearDown() {
        workingDir.deleteRecursively()
    }

    @Test
    fun accepts_current_room_identity_and_schema_version() {
        val backup = createBackup(
            name = "valid${BackupContract.BACKUP_EXTENSION}",
            version = BackupContract.DATABASE_VERSION,
            identityHash = BackupContract.ROOM_IDENTITY_HASH,
        )

        assertTrue(BackupValidator.isCandidateBackup(backup))
        assertTrue(BackupValidator.validateSqliteBackup(backup))
    }

    @Test
    fun rejects_corrupted_file() {
        val backup = File(workingDir, "corrupted${BackupContract.BACKUP_EXTENSION}")
        backup.writeText("not-a-sqlite-database")

        assertTrue(BackupValidator.isCandidateBackup(backup))
        assertFalse(BackupValidator.validateSqliteBackup(backup))
    }

    @Test
    fun rejects_incompatible_database_version() {
        val backup = createBackup(
            name = "old${BackupContract.BACKUP_EXTENSION}",
            version = BackupContract.DATABASE_VERSION + 1,
            identityHash = BackupContract.ROOM_IDENTITY_HASH,
        )

        assertFalse(BackupValidator.validateSqliteBackup(backup))
    }

    @Test
    fun rejects_wrong_room_identity() {
        val backup = createBackup(
            name = "wrong-identity${BackupContract.BACKUP_EXTENSION}",
            version = BackupContract.DATABASE_VERSION,
            identityHash = "wrong-room-identity",
        )

        assertFalse(BackupValidator.validateSqliteBackup(backup))
    }

    @Test
    fun rejects_file_with_wrong_extension_as_candidate() {
        val backup = createBackup(
            name = "valid.sqlite",
            version = BackupContract.DATABASE_VERSION,
            identityHash = BackupContract.ROOM_IDENTITY_HASH,
        )

        assertFalse(BackupValidator.isCandidateBackup(backup))
        assertTrue(BackupValidator.validateSqliteBackup(backup))
    }

    private fun createBackup(
        name: String,
        version: Int,
        identityHash: String,
    ): File {
        val file = File(workingDir, name)
        val db = SQLiteDatabase.openOrCreateDatabase(file, null)
        try {
            BackupContract.REQUIRED_TABLES
                .filterNot { it == "room_master_table" }
                .forEach { tableName ->
                    db.execSQL("CREATE TABLE `$tableName` (`id` TEXT)")
                }
            db.execSQL(
                "CREATE TABLE room_master_table (id INTEGER PRIMARY KEY, identity_hash TEXT)",
            )
            db.execSQL(
                "INSERT INTO room_master_table (id, identity_hash) VALUES (42, ?)",
                arrayOf(identityHash),
            )
            db.execSQL("PRAGMA user_version = $version")
        } finally {
            db.close()
        }
        return file
    }
}
