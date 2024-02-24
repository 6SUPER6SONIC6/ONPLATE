package com.supersonic.onplate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.utils.Converters

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OnplateDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var Instance: OnplateDatabase? = null

        fun getDatabase(context: Context): OnplateDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, OnplateDatabase::class.java, "recipe_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}