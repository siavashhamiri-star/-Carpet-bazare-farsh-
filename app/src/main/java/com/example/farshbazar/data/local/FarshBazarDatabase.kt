package com.example.farshbazar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.farshbazar.data.model.*

@Database(
    entities = [
        Carpet::class,
        Vendor::class,
        Review::class,
        Suggestion::class,
        UserProfile::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FarshBazarDatabase : RoomDatabase() {
    abstract fun farshBazarDao(): FarshBazarDao

    companion object {
        @Volatile
        private var INSTANCE: FarshBazarDatabase? = null

        fun getDatabase(context: Context): FarshBazarDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    buildDatabase(context).also { INSTANCE = it }
                } catch (t: Throwable) {
                    android.util.Log.e("FarshBazarDatabase", "Resetting database due to schema change: ${t.message}")
                    try {
                        context.applicationContext.deleteDatabase("farsh_bazar_database")
                    } catch (_: Throwable) {}
                    buildDatabase(context).also { INSTANCE = it }
                }
            }
        }

        private fun buildDatabase(context: Context): FarshBazarDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FarshBazarDatabase::class.java,
                "farsh_bazar_database"
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}
