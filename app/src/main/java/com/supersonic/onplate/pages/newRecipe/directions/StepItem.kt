package com.supersonic.onplate.pages.newRecipe.directions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.components.RecipeTextField

@Composable
fun StepItem(
    stepValue: String,
    onStepValueChange: (Int, String) -> Unit,
    onRemoveStep: () -> Unit,
    removeEnabled: Boolean = true,
    id: Int
) {
    var value by remember { mutableStateOf(stepValue) }

    RecipeTextField(
        value = value,
        onValueChange = { value = it
            onStepValueChange(id, it) },
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        height = 112.dp,
        maxLines = 3,
        trailingIcon = {
            IconButton(
                onClick = onRemoveStep,
                enabled = removeEnabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )){
                Icon(
                    Icons.Filled.Clear, contentDescription = null,
                )
            }
        })
}