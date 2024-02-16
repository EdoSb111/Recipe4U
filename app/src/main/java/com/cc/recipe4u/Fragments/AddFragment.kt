package com.cc.recipe4u.Fragments

import GalleryHandler
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.recipe4u.Adapters.IngredientAdapter
import com.cc.recipe4u.DataClass.Recipe
import com.cc.recipe4u.Objects.GlobalVariables
import com.cc.recipe4u.R
import com.cc.recipe4u.ViewModels.AuthViewModel
import com.cc.recipe4u.ViewModels.RecipeViewModel
import com.cc.recipe4u.ViewModels.UserViewModel
import com.google.android.gms.common.api.internal.LifecycleActivity
import com.google.android.material.textfield.TextInputEditText

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PICK_IMAGE_REQUEST = 1

class AddFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recipeNameEditText: EditText
    private lateinit var imageViewRecipe: ImageView
    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextDescription: EditText
    private lateinit var editTextIngredient: EditText
    private lateinit var editTextProcedure: EditText
    private lateinit var buttonAddIngredient: Button
    private lateinit var buttonSave: Button
    private lateinit var buttonCancel: Button
    private lateinit var editTextFilter: TextInputEditText
    private lateinit var recyclerViewIngredients: RecyclerView
    private lateinit var adapter: IngredientAdapter

    private var imageUri: Uri? = null
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val userViewModel: UserViewModel = UserViewModel(GlobalVariables.currentUser!!.userId)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                GalleryHandler.getPhotoUriFromGallery(requireActivity(), pickImageLauncher, null)
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // Handle the selected image URI
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    imageViewRecipe.scaleType = ImageView.ScaleType.CENTER_CROP
                    imageViewRecipe.setImageURI(selectedImageUri)
                    imageUri = selectedImageUri
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        recipeViewModel.setContextAndDB(requireContext())

        // Initialize views
        recipeNameEditText = view.findViewById(R.id.editTextRecipeName)
        imageViewRecipe = view.findViewById(R.id.imageViewRecipe)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        editTextProcedure = view.findViewById(R.id.editTextProcedure)
        buttonSave = view.findViewById(R.id.buttonSave)
        buttonCancel = view.findViewById(R.id.buttonCancel)

        // Set onClickListener for the image view to pick an image from the gallery
        imageViewRecipe.setOnClickListener {
            GalleryHandler.getPhotoUriFromGallery(requireActivity(), pickImageLauncher, requestPermissionLauncher)
        }

        // Set onClickListener for the save button
        buttonSave.setOnClickListener {
            uploadRecipe()
        }

        // Set onClickListener for the cancel button
        buttonCancel.setOnClickListener {
            // Handle cancel button click here
        }

        editTextFilter = view.findViewById(R.id.editTextFilter)
        recyclerViewIngredients = view.findViewById(R.id.recyclerViewIngredients)

        // Initialize RecyclerView and Adapter
        recyclerViewIngredients.layoutManager = LinearLayoutManager(requireContext())
        adapter = IngredientAdapter(getSampleIngredients())
        recyclerViewIngredients.adapter = adapter

        // Set up text change listener for filtering
        editTextFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterIngredients(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing
            }
        })

        return view
    }

    private fun uploadRecipe() {
        // Get the values from the views
        val recipeName = recipeNameEditText.text.toString()
        val category = spinnerCategory.selectedItem.toString()
        val description = editTextDescription.text.toString()
        val procedure = editTextProcedure.text.toString()

        // Get the checked ingredients from the RecyclerView
        val checkedIngredients = adapter.getCheckedItems().toList()

        // Create a Recipe object
        val recipe = Recipe(
            recipeId = "",
            name = recipeName,
            category = category,
            description = description,
            imageUri = imageUri.toString(),
            ingredients = checkedIngredients,
            procedure = procedure,
            rating = 0.0f,
            numberOfRatings = 0,
            ownerId = GlobalVariables.currentUser!!.userId,
            lastUpdated = System.currentTimeMillis()
        )

        // Call the createRecipe method in RecipeViewModel
        recipeViewModel.createRecipe(recipe) { recipeWithId ->
            // After a successful creation, update the user's recipeIds
            userViewModel.updateUserRecipeIds(listOf(recipeWithId.recipeId))
        }
    }

    private fun filterIngredients(query: String) {
        val filteredList = getSampleIngredients().filter {
            it.lowercase().contains(query.lowercase())
        }
        adapter.filter.filter(query)
    }

    private fun getSampleIngredients(): List<String> {
        // Replace this with your actual list of ingredients
        return listOf("Sugar",
            "Salt",
            "Flour",
            "Eggs",
            "Milk",
            "Butter",
            "Olive Oil",
            "Baking Powder",
            "Vanilla Extract",
            "Cinnamon",
            "Nutmeg",
            "Garlic",
            "Onion",
            "Tomatoes",
            "Chicken",
            "Beef",
            "Pasta",
            "Rice",
            "Lemon",
            "Honey")
    }
}
