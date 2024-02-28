package com.supersonic.onplate.pages.newRecipe.ingredients

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.components.RecipeTextField


@Composable
fun IngredientItem(
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: () -> Unit,
    removeEnabled: Boolean = true,
    id: Int
) {
    var value by remember { mutableStateOf(id.toString()) }

    RecipeTextField(
        value = value,
        onValueChange = { value = it
            onIngredientValueChange(id, it) },
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = onRemoveIngredient,
                enabled = removeEnabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorScheme.onSecondaryContainer
                )){
                Icon(
                    Icons.Filled.Clear, contentDescription = null,
                )
            }
        })
}