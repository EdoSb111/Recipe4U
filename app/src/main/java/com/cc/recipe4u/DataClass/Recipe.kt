package com.cc.recipe4u.DataClass

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val recipeId: String = "",  // Default values for non-nullable properties
    val name: String = "",
    val category: String = "",
    val description: String = "",
    var imageUri: String = "",
    val ingredients: List<String> = emptyList(),
    val procedure: String = "",
    val rating: Float = 0.0f,
    val numberOfRatings: Int = 0,
    val ownerId: String = "",
    val lastUpdated: Long = 0
) {
    @Ignore
    constructor() : this("", "", "", "", "", emptyList(), "", 0.0f, 0, "", 0)
}

