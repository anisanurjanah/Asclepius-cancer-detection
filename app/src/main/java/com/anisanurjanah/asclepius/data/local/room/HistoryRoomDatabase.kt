package com.anisanurjanah.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anisanurjanah.asclepius.data.local.entity.History

@Database(entities = [History::class], version = 2)
abstract class HistoryRoomDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryRoomDatabase::class.java,
                    "history.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `history_new` " +
                    "(`label` TEXT NOT NULL, " +
                    "`imageUri` TEXT, " +
                    "`score` TEXT, " +
                    "`timestamp` TEXT, " +
                    "PRIMARY KEY(`label`))"
        )

        db.execSQL(
            "INSERT INTO `history_new` (`label`, `imageUri`, `score`, `timestamp`) " +
                    "SELECT `label`, null, null, null FROM `history`"
        )

        db.execSQL("DROP TABLE `history`")

        db.execSQL("ALTER TABLE `history_new` RENAME TO `history`")
    }
}