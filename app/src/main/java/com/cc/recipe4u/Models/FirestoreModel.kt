package com.cc.recipe4u.Models

import android.net.Uri
import android.util.Log
import com.cc.recipe4u.DataClass.Recipe
import com.cc.recipe4u.Objects.RecipeLocalTime
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

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

    fun createRecipe(recipe: Recipe, listener: (Recipe) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val newRecipeRef = db.collection("recipes").document()
        val newRecipe = recipe.copy(recipeId = newRecipeRef.id)
        newRecipeRef.set(newRecipe)
            .addOnSuccessListener {
                listener(newRecipe)
            }
            .addOnFailureListener {
                Log.d("updateRecipe", "failed: ${it.message}")
            }
    }

    fun updateRecipe(recipe: Recipe, listener: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("recipes")
            .document(recipe.recipeId)
            .set(recipe)
            .addOnSuccessListener { listener() }
    }

    // Function to upload an image to Firestore Storage and get the URL
    fun uploadImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imageFileName = UUID.randomUUID().toString() // Generate a unique filename for the image
        val imageRef: StorageReference = storageRef.child("user_images/$imageFileName")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Get the download URL of the uploaded image
                    val imageUrl = uri.toString()
                    onSuccess(imageUrl)
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.d("uploadImage", "failed: ${exception.message}")
                onFailure()
            }
    }
}