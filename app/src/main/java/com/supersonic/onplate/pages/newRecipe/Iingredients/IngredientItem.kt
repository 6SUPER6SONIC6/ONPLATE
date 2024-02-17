package com.supersonic.onplate.pages.newRecipe.Iingredients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.components.RecipeTextField


@Composable
fun IngredientItem(
    onRemoveIngredient: () -> Unit,
    removeEnabled: Boolean = true,
    id: Int
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        var ingredient by remember { mutableStateOf(id.toString()) }

        RecipeTextField(value = ingredient, onValueChange = {ingredient = it}, modifier = Modifier, singleLine = true)

        IconButton(
            onClick = onRemoveIngredient,
            enabled = removeEnabled,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colorScheme.onSecondaryContainer
            )){
            Icon(
                Icons.Filled.Clear, contentDescription = null,
                modifier = Modifier.size(32.dp))
        }
    }
}