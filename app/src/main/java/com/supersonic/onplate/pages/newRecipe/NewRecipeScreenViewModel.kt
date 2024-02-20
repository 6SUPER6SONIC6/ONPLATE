package com.supersonic.onplate.pages.newRecipe

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
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


    private val _steps = getStepsList().toMutableStateList()
    val steps: List<Step>
        get() = _steps

    fun removeStep(item: Step) {
        if (_steps.size > 1) {
            _steps.remove(item)
        }
    }

    fun addEmptyStep() {
        _steps.add(Step(_steps.last().id + 1))
    }

    private fun getStepsList() = List(1) { i -> Step(i) }
}