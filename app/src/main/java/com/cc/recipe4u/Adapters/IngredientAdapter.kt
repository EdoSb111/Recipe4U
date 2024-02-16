package com.cc.recipe4u.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.cc.recipe4u.R

class IngredientAdapter(private var ingredients: List<String>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun updateList(newList: List<String>) {
        ingredients = newList
        notifyDataSetChanged()
    }

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkboxIngredient: CheckBox = itemView.findViewById(R.id.checkboxIngredient)

        fun bind(ingredient: String) {
            checkboxIngredient.text = ingredient
        }

        fun isChecked(): Boolean {
            return checkboxIngredient.isChecked
        }

        fun getIngredient(): String {
            return checkboxIngredient.text.toString()
        }
    }
}
