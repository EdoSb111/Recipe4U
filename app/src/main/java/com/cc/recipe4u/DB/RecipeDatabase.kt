package com.cc.recipe4u.DB

import android.content.Context
import androidx.room.Room

object RecipeDatabase {
    fun db(context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java, "recipe-database"
        ).build()
    }
}