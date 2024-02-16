package com.cc.recipe4u.DataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val recipeId: String,
    val name: String,
    val category: String,
    val description: String,
    var imageUri: String,
    val ingredients: List<String>,
    val procedure: String,
    val rating: Float,
    val numberOfRatings: Int,
    val ownerId: String,
    val lastUpdated: Long
)
