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

        /** Called before replacing the SQLite file during local restore. */
        fun closeInstanceForRestore() {
            synchronized(this) {
                INSTANCE?.close()
                INSTANCE = null
            }
        }

        private fun buildDatabase(context: Context): WalleeDatabase {
            return Room.databaseBuilder(
                context,
                WalleeDatabase::class.java,
                KHARJYAR_DATABASE_NAME,
            ).build()
        }
    }
}
