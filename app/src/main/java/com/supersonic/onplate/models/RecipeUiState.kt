package com.supersonic.onplate.models

import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient

data class RecipeUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val directions: List<Step> = emptyList(),
    val cookingTime: String = ""
)


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
    ingredients = ingredients,
    directions = directions,
    cookingTime = cookingTime
)

fun RecipeUiState.isValid() : Boolean {
    return title.isNotBlank() && description.isNotBlank()
}
