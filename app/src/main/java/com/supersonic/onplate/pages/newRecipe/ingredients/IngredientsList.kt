package com.supersonic.onplate.pages.newRecipe.ingredients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

@Composable
fun IngredientsList(
    list: List<Ingredient>,
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: (Ingredient) -> Unit,
    removeEnabled: Boolean = true,
) {

    // Maintain a map to track visibility of each item
    val visibilityMap = remember { mutableStateMapOf<Int, Boolean>() }

    // Ensure visibility for new items
    list.forEach { ingredient ->
        if (!visibilityMap.containsKey(ingredient.id)) visibilityMap[ingredient.id] = false
    }

    Column {
        list.forEach { ingredient ->
            key(ingredient.id) {
                // Start visibility as true after composition
                LaunchedEffect(ingredient.id) {
                    visibilityMap[ingredient.id] = true
                }

                // Use AnimatedVisibility to animate appearance of new items
                AnimatedVisibility(
                    visible = visibilityMap[ingredient.id] == true,  // Controlled by the map
                    enter = expandVertically(
                        animationSpec = tween(300),
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                ) {
                    IngredientItem(
                        onIngredientValueChange = onIngredientValueChange,
                        onRemoveIngredient = {
                            // Trigger the exit animation by setting visibility to false
                            visibilityMap[ingredient.id] = false
                            onRemoveIngredient(ingredient)

                        },
                        removeEnabled = removeEnabled,
                        id = ingredient.id,
                        ingredientValue = ingredient.value
                    )
                }
            }
        }
    }

//    list.forEach { ingredient ->
//        key(ingredient.id) {
//
//                IngredientItem(
//                    onIngredientValueChange = onIngredientValueChange,
//                    onRemoveIngredient = { onRemoveIngredient(ingredient) },
//                    removeEnabled = removeEnabled,
//                    id = ingredient.id,
//                    ingredientValue = ingredient.value
//                )
//        }
//    }
}