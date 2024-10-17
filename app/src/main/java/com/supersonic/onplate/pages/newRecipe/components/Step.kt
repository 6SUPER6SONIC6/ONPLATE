package com.supersonic.onplate.pages.newRecipe.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.components.RecipeTextField
import kotlinx.coroutines.delay

data class Step(
    val id: Int = 0,
    val value: String = "",
)

@Composable
fun StepItem(
    stepValue: String,
    onStepValueChange: (Int, String) -> Unit,
    onRemoveStep: () -> Unit,
    removeEnabled: Boolean = true,
    id: Int
) {
    var value by remember { mutableStateOf(stepValue) }
    val animationDuration = 300
    var isRemoved by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(animationDuration.toLong())
            onRemoveStep.invoke()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        RecipeTextField(
            value = value,
            onValueChange = {
                value = it
                onStepValueChange(id, it)
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            height = 112.dp,
            maxLines = 3,
            trailingIcon = {
                IconButton(
                    onClick = { isRemoved = true },
                    enabled = removeEnabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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

@Composable
fun StepsList(
    list: List<Step>,
    onStepValueChange: (Int, String) -> Unit,
    onRemoveStep: (Step) -> Unit,
    removeEnabled: Boolean = true,
) {

    // Maintain a map to track visibility of each item
    val visibilityMap = remember { mutableStateMapOf<Int, Boolean>() }

    // Ensure visibility for new items
    list.forEach { step ->
        if (!visibilityMap.containsKey(step.id)) visibilityMap[step.id] = false
    }

    Column {
        list.forEach { step ->
            key(step.id) {
                // Start visibility as true after composition
                LaunchedEffect(step.id) {
                    visibilityMap[step.id] = true
                }

                // Use AnimatedVisibility to animate appearance of new items
                AnimatedVisibility(
                    visible = visibilityMap[step.id] == true,  // Controlled by the map
                    enter = expandVertically(
                        animationSpec = tween(durationMillis = 300),
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                ) {
                    StepItem(
                        onStepValueChange = onStepValueChange,
                        onRemoveStep = {
                            visibilityMap[step.id] = false
                            onRemoveStep(step)
                        },
                        removeEnabled = removeEnabled,
                        id = step.id,
                        stepValue = step.value)
                }
            }
        }
    }
}