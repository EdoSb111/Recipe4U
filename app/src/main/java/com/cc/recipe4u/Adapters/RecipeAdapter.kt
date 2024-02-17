package com.cc.recipe4u.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cc.recipe4u.DataClass.Recipe
import com.cc.recipe4u.R
import com.squareup.picasso.Picasso

class RecipeAdapter(private var recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewRecipe: ImageView = itemView.findViewById(R.id.imageViewRecipe)
        val textViewRecipeName: TextView = itemView.findViewById(R.id.textViewRecipeName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val buttonView: Button = itemView.findViewById(R.id.buttonView)
    }

    init {
        // Sort the recipes list alphabetically by name
        recipes = recipes.sortedBy { it.name }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_card_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        // Bind data to views
        holder.textViewRecipeName.text = recipe.name
        holder.textViewDescription.text = recipe.description
        // Load image using your preferred image loading library (Glide, Picasso, etc.)
        // holder.imageViewRecipe.setImageResource(recipe.imageResource)
        if (recipe.imageUri != "null") {
            Picasso.get()
                .load(recipe.imageUri)
                .placeholder(R.drawable.progress_animation)
                .into(holder.imageViewRecipe, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        holder.imageViewRecipe.scaleType = ImageView.ScaleType.CENTER_CROP
                    }

                    override fun onError(e: Exception?) {
                        // Set your visibility to VISIBLE
                    }
                })
        }

        // Set click listener for the "View" button if needed
        holder.buttonView.setOnClickListener {
            // Handle button click, e.g., navigate to a detailed view
        }
    }

    override fun getItemCount(): Int = recipes.size
}
