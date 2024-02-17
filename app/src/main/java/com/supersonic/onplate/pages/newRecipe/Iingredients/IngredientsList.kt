package com.supersonic.onplate.pages.newRecipe.Iingredients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

@Composable
fun IngredientsList(
    list: List<Ingredient>,
    onRemoveIngredient: (Ingredient) -> Unit,
    removeEnabled: Boolean = true,
) {
    list.forEach{ ingredient ->
        key(ingredient.id) {
            IngredientItem(onRemoveIngredient = { onRemoveIngredient(ingredient) }, removeEnabled = removeEnabled, id = ingredient.id)
        }
    }
}