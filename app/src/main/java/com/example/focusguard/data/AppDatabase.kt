package com.example.focusguard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [BlockedApp::class, PermissionRequest::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focus_guard_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromStatus(status: RequestStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): RequestStatus = RequestStatus.valueOf(value)
}
