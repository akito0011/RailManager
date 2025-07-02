package com.example.trainmanager.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ItinerarioEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itinerariDao(): ItinerariDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "items_table"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}