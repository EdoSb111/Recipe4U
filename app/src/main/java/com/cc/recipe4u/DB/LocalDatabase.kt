package com.cc.recipe4u.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cc.recipe4u.Dao.RecipeDao
import com.cc.recipe4u.DataClass.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
}