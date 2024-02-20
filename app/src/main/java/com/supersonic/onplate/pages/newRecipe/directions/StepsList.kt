package com.supersonic.onplate.pages.newRecipe.directions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

@Composable
fun StepsList(
    list: List<Step>,
    onRemoveStep: (Step) -> Unit,
    removeEnabled: Boolean = true,
) {
    list.forEach { step ->
        key(step.id) {
            StepItem(
                onRemoveStep = { onRemoveStep(step) },
                removeEnabled = removeEnabled,
                id = step.id)
        }
    }
}