package com.cc.recipe4u.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cc.recipe4u.DB.RecipeDatabase
import com.cc.recipe4u.DataClass.Recipe
import com.cc.recipe4u.Models.FirestoreModel
import com.cc.recipe4u.Objects.RecipeLocalTime

class RecipeViewModel(context: Context) : ViewModel() {
    private val thisContext = context
    private val recipeDao = RecipeDatabase.db(context).recipeDao()
    private val _allRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    private val allRecipes: LiveData<List<Recipe>> get() = _allRecipes

    fun getAllRecipes(): LiveData<List<Recipe>> {
        val localLastUpdated = RecipeLocalTime.getLocalLastUpdated(thisContext)
        FirestoreModel.getAllRecipes(localLastUpdated) { recipes ->
            var lastUpdated = 0L
            for (recipe in recipes) {
                recipeDao.insert(recipe)
                if (lastUpdated < recipe.lastUpdated) {
                    lastUpdated = recipe.lastUpdated
                }
            }
            RecipeLocalTime.setLocalLastUpdated(thisContext, lastUpdated)

            // Update the MutableLiveData with the latest data
            _allRecipes.postValue(recipeDao.getAll().value)
        }
        return allRecipes
    }

    fun getByCategory(category: String): LiveData<List<Recipe>> {
        return recipeDao.getByCategory(category)
    }

    fun getByOwner(ownerId: String): LiveData<List<Recipe>> {
        return recipeDao.getByOwner(ownerId)
    }

    fun createRecipe(recipe: Recipe) {
        FirestoreModel.createRecipe(recipe) {
            recipeDao.insert(recipe)
        }
    }
}


