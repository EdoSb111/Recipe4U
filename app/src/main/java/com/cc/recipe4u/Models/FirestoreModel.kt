package com.cc.recipe4u.Models

import com.cc.recipe4u.DataClass.Recipe
import com.cc.recipe4u.Objects.RecipeLocalTime
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreModel {
    fun getAllRecipes(since: Long, listener: (List<Recipe>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("recipes")
            .whereGreaterThan(RecipeLocalTime.LAST_UPDATED, since)
            .get()
            .addOnSuccessListener { result ->
                val recipes = mutableListOf<Recipe>()
                for (document in result) {
                    val recipe = document.toObject(Recipe::class.java)
                    recipes.add(recipe)
                }
                listener(recipes)
            }
    }

    fun createRecipe(recipe: Recipe, listener: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { listener() }
    }

    fun updateRecipe(recipe: Recipe, listener: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("recipes")
            .document(recipe.recipeId)
            .set(recipe)
            .addOnSuccessListener { listener() }
    }
}