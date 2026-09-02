package com.wisnu.kurniawan.wallee.foundation.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wisnu.kurniawan.wallee.foundation.datasource.local.model.AccountDb
import com.wisnu.kurniawan.wallee.foundation.datasource.local.model.AccountRecordDb
import com.wisnu.kurniawan.wallee.foundation.datasource.local.model.TransactionDb
import com.wisnu.kurniawan.wallee.foundation.datasource.local.model.TransactionRecordDb
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Legacy on-device database file name. Do not rename without a file-level migration because
 * existing installs already store their financial history under this name.
 */
const val KHARJYAR_DATABASE_NAME = "wallee-db"
const val KHARJYAR_DATABASE_VERSION = 1

@Database(
    entities = [
        AccountDb::class,
        TransactionDb::class,
        AccountRecordDb::class,
        TransactionRecordDb::class,
    ],
    version = KHARJYAR_DATABASE_VERSION,
)
@TypeConverters(DateConverter::class)
abstract class WalleeDatabase : RoomDatabase() {
    abstract fun walleeWriteDao(): WalleeWriteDao
    abstract fun walleeReadDao(): WalleeReadDao

    @DelicateCoroutinesApi
    companion object {
        @Volatile
        private var INSTANCE: WalleeDatabase? = null

        fun getInstance(context: Context): WalleeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        /**
         * Closes the singleton before a database-file restore. Callers must restart the process
         * immediately after restoring because already injected DAO instances reference the old DB.
         */
        fun closeInstanceForRestore() {
            synchronized(this) {
                INSTANCE?.close()
                INSTANCE = null
            }
        }

        private fun buildDatabase(context: Context): WalleeDatabase {
            // AS Team data-safety policy: never enable Room destructive migration here.
            // When the schema version changes, add an explicit Migration(X, Y) and register it
            // with addMigrations(...). A missing migration should fail loudly instead of deleting
            // the user's accounts and transaction history.
            return Room.databaseBuilder(
                context,
                WalleeDatabase::class.java,
                KHARJYAR_DATABASE_NAME,
            ).build()
        }
    }
}
