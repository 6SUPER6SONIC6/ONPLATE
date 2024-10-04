package com.supersonic.onplate.pages.newRecipe.directions

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

//    list.forEach { step ->
//        key(step.id) {
//            StepItem(
//                onStepValueChange = onStepValueChange,
//                onRemoveStep = { onRemoveStep(step) },
//                removeEnabled = removeEnabled,
//                id = step.id,
//                stepValue = step.value)
//        }
//    }
}