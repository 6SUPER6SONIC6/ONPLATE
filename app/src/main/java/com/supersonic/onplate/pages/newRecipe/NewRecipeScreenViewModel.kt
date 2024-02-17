package com.supersonic.onplate.pages.newRecipe

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.supersonic.onplate.pages.newRecipe.Iingredients.Ingredient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewRecipeScreenViewModel @Inject constructor() : ViewModel() {

    private val _ingredients = getIngredientsList().toMutableStateList()
    val ingredients: List<Ingredient>
        get() = _ingredients

    fun removeIngredient(item: Ingredient) {
        if (_ingredients.size > 1) {
            _ingredients.remove(item)
        }
    }


    fun addEmptyIngredient() {
        _ingredients.add(Ingredient(_ingredients.last().id + 1))
    }
    private fun getIngredientsList() = List(1) { i -> Ingredient(i) }
}