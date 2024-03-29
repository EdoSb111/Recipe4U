package com.cc.recipe4u.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.recipe4u.Adapters.RecipeAdapter
import com.cc.recipe4u.DataClass.Recipe
import com.cc.recipe4u.Objects.localDataRepository
import com.cc.recipe4u.R
import com.cc.recipe4u.ViewModels.RecipeViewModel
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var categoryTabLayout: TabLayout
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var searchTextInput: EditText
    private val recipeViewModel: RecipeViewModel by viewModels()
    private var recipes = listOf<Recipe>()

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        categoryTabLayout = view.findViewById(R.id.categoryTabLayout)
        recipeRecyclerView = view.findViewById(R.id.recipesRecyclerView)
        searchTextInput = view.findViewById(R.id.searchTextInputLayout)
        recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipeViewModel.setContextAndDB(requireContext())

        initCategoryTabs()
        initRecipeRecyclerView()
        setSearchRecipes()

        return view
    }

    private fun initRecipeRecyclerView() {
        recipeViewModel.getAllRecipes().observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                this.recipes = recipes
                updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        val category = categoryTabLayout.getTabAt(categoryTabLayout.selectedTabPosition)?.text.toString()
        val name = searchTextInput.text.toString()
        val filteredRecipes = recipes.filter { recipe ->
            if (category == "All") {
                recipe.name.contains(name, ignoreCase = true)
            } else {
                recipe.name.contains(name, ignoreCase = true) && recipe.category == category
            }
        }
        val adapter = RecipeAdapter(filteredRecipes, this)
        recipeRecyclerView.adapter = adapter
    }
    private fun initCategoryTabs() {
        categoryTabLayout.addTab(categoryTabLayout.newTab().setText("All"))

        for (category in localDataRepository.categories) {
            categoryTabLayout.addTab(categoryTabLayout.newTab().setText(category))
        }

        categoryTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateAdapter()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })
    }

    private fun setSearchRecipes() {
        searchTextInput.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            updateAdapter()
        })
    }
}