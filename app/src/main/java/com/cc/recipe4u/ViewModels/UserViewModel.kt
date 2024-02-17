package com.cc.recipe4u.ViewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cc.recipe4u.DataClass.User
import com.cc.recipe4u.Objects.GlobalVariables
import com.cc.recipe4u.Repositories.UserRepository
import java.util.*

class UserViewModel(private val userId: String) : ViewModel() {

    private val userRepository = UserRepository()
    private val _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User> get() = _userLiveData

    init {
        fetchUser()
    }

    private fun fetchUser() {
        userRepository.fetchUser(userId,
            onSuccess = { user ->
                _userLiveData.postValue(user)
                GlobalVariables.currentUser = user
            },
            onFailure = {
                // Handle failure
            }
        )
    }

    fun updateUser(user: User) {
        userRepository.updateUser(user,
            onSuccess = {
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
            },
            onFailure = {
                // Handle failure
            }
        )
    }

    fun updateUserName(newName: String) {
        userRepository.updateUserName(userId, newName,
            onSuccess = {
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
            },
            onFailure = {
                // Handle failure
            }
        )
    }

    fun updateUserPhoto(imageUri: Uri) {
        userRepository.updateUserPhoto(userId, imageUri,
            onSuccess = { newPhotoUrl ->
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
            },
            onFailure = {
                // Handle failure
            }
        )
    }

    fun updateUserRecipeIds(newRecipeIds: List<String>, onSuccess: () -> Unit, onFailure: () -> Unit) {
        userRepository.updateUserRecipeIds(userId, newRecipeIds,
            onSuccess = {
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
                onSuccess()
            },
            onFailure = {
                onFailure()
                // Handle failure
            }
        )
    }

    fun updateUserFavoriteRecipeId(newFavoriteRecipeIds: String) {
        userRepository.updateUserFavoriteRecipeId(userId, newFavoriteRecipeIds,
            onSuccess = {
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
            },
            onFailure = {
                // Handle failure
            }
        )
    }

    fun removeUserFavoriteRecipeId(favoriteRecipeId: String) {
        userRepository.removeUserFavoriteRecipeId(userId, favoriteRecipeId,
            onSuccess = {
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
            },
            onFailure = {
                // Handle failure
            }
        )
    }

    fun updateUserRatedRecipes(newRatedRecipes: Map<String, Int>) {
        userRepository.updateUserRatedRecipes(userId, newRatedRecipes,
            onSuccess = {
                // After a successful update, fetch the user again to reflect changes
                fetchUser()
            },
            onFailure = {
                // Handle failure
            }
        )
    }
}
