package com.cc.recipe4u.DataClass

data class Recipe(
    val recipeId: String,
    val name: String,
    val category: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val procedure: String,
    val rating: Float,
    val numberOfRatings: Int,
    val ownerId: String
)
