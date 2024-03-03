package com.supersonic.onplate.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient

data class RecipeUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val ingredients: MutableList<Ingredient> = mutableStateListOf(),
    val directions: MutableList<Step> = mutableStateListOf(),
    val cookingTime: String = ""
)

fun RecipeUiState.updateIngredientValue(id: Int, value: String) {
    val index = ingredients.indexOfFirst { it.id == id }
    if (index != -1) {
        ingredients[index] = ingredients[index].copy(value = value)
    }
}

fun RecipeUiState.removeIngredient(item: Ingredient) {
    if (ingredients.size > 1) {
        ingredients.remove(item)
    }
}

fun RecipeUiState.addEmptyIngredient() {

    if (ingredients.isEmpty()){
        ingredients.add(Ingredient(ingredients.size))
    } else {
        ingredients.add(Ingredient(ingredients.last().id + 1))
    }
}

fun RecipeUiState.updateStepValue(id: Int, value: String) {
    val index = directions.indexOfFirst { it.id == id }
    if (index != -1) {
        directions[index] = directions[index].copy(value = value)
    }
}

fun RecipeUiState.removeStep(item: Step) {
    if (directions.size > 1) {
        directions.remove(item)
    }
}

fun RecipeUiState.addEmptyStep() {

    if (directions.isEmpty()) {
        directions.add(Step(0))
    } else {
        directions.add(Step(id = directions.last().id + 1))
    }
}

fun RecipeUiState.toRecipe(): Recipe = Recipe(
    id = id,
    title = title,
    description = description,
    ingredients = ingredients,
    directions = directions,
    cookingTime = cookingTime
)
fun Recipe.toRecipeUiState() : RecipeUiState = RecipeUiState(
    id = id,
    title = title,
    description = description,
    ingredients = ingredients.toMutableStateList(),
    directions = directions.toMutableStateList(),
    cookingTime = cookingTime
)

fun RecipeUiState.isValid() : Boolean {
    return title.isNotBlank() && description.isNotBlank()
}
