package com.cc.recipe4u.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cc.recipe4u.Dao.RecipeDao
import com.cc.recipe4u.DataClass.Recipe

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
}