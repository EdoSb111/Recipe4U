package com.cc.recipe4u.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.recipe4u.Adapters.RecipeAdapter
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
    private val recipeViewModel: RecipeViewModel by viewModels()

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
        recipeViewModel.setContextAndDB(requireContext())

        initCategoryTabs()
        initRecipeRecyclerView()

        return view
    }

    private fun initRecipeRecyclerView() {
        recipeViewModel.getAllRecipes().observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                val category = categoryTabLayout.getTabAt(categoryTabLayout.selectedTabPosition)?.text.toString()
                val filteredRecipes = if (category == "All") {
                    recipes
                } else {
                    recipes.filter { it.category == category }
                }
                val adapter = RecipeAdapter(filteredRecipes)
                recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                recipeRecyclerView.adapter = adapter
            }
        }
    }
    private fun initCategoryTabs() {
        categoryTabLayout.addTab(categoryTabLayout.newTab().setText("All"))

        for (category in localDataRepository.categories) {
            categoryTabLayout.addTab(categoryTabLayout.newTab().setText(category))
        }

        categoryTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                initRecipeRecyclerView()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })
    }
}