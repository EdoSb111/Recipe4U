package com.cc.recipe4u.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cc.recipe4u.DataClass.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category = :category")
    fun getByCategory(category: String): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE ownerId = :ownerId")
    fun getByOwner(ownerId: String): LiveData<List<Recipe>>

    @Update
    fun update(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe)
}