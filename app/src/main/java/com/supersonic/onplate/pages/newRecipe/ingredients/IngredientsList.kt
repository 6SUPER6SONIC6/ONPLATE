package com.supersonic.onplate.pages.newRecipe.ingredients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

@Composable
fun IngredientsList(
    list: List<Ingredient>,
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: (Ingredient) -> Unit,
    removeEnabled: Boolean = true,
) {
    list.forEach { ingredient ->
        key(ingredient.id) {
            IngredientItem(
                onIngredientValueChange = onIngredientValueChange,
                onRemoveIngredient = { onRemoveIngredient(ingredient) },
                removeEnabled = removeEnabled,
                id = ingredient.id,
                ingredientValue = ingredient.value
            )
        }
    }
}