package com.supersonic.onplate.pages.newRecipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewRecipeScreenViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    fun updateUiState(newRecipeUiState: RecipeUiState) {
        recipeUiState = newRecipeUiState.copy()
    }

// Ingredients
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

    fun updateIngredientValue(id: Int, value: String) {
        val index = _ingredients.indexOfFirst { it.id == id }
        if (index != -1) {
            _ingredients[index] = _ingredients[index].copy(value = value)
        }
    }

// Steps
    private val _steps = getStepsList().toMutableStateList()
    val steps: List<Step>
        get() = _steps

    fun removeStep(item: Step) {
        if (_steps.size > 1) {
            _steps.remove(item)
        }
    }

    fun addEmptyStep() {
        _steps.add(Step(id = _steps.last().id + 1))
    }

    private fun getStepsList() = List(1) { i -> Step(i) }

    fun updateStepValue(id: Int, value: String) {
        val index = _steps.indexOfFirst { it.id == id }
        if (index != -1) {
            _steps[index] = _steps[index].copy(value = value)
        }
    }

    //Room
    suspend fun saveRecipe() {
        if (recipeUiState.isValid()) {
            recipesRepository.insertRecipe(recipeUiState.toRecipe())
        }
    }
}