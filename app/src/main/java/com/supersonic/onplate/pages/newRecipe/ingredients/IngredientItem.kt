package com.supersonic.onplate.pages.newRecipe.ingredients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.components.RecipeTextField
import kotlinx.coroutines.delay


@Composable
fun IngredientItem(
    ingredientValue: String,
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: () -> Unit,
    removeEnabled: Boolean = true,
    id: Int
) {
    var value by remember { mutableStateOf(ingredientValue) }
    val animationDuration = 500
    var isRemoved by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(animationDuration.toLong())
            onRemoveIngredient.invoke()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        enter = expandVertically(
            animationSpec = tween(durationMillis = animationDuration),
            expandFrom = Alignment.Top
        ) + fadeIn(),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        RecipeTextField(
            value = value,
            onValueChange = {
                value = it
                onIngredientValueChange(id, it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        isRemoved = true
                              },
                    enabled = removeEnabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        Icons.Filled.Clear, contentDescription = null,
                    )
                }
            }
        )
    }
}