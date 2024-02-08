package com.supersonic.onplate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.theme.ONPLATETheme


/**
 * A customizable text input field with a label and outlined border. Based on Material Design outlined text field.
 *
 * @param value The current input value of the text field.
 * @param onValueChange A callback invoked when the input value changes.
 * @param label The label displayed above the text field.
 * @param placeholder The placeholder text displayed inside the text field when it is empty.
 * @param maxLines The maximum number of lines for the text field (default is 1).
 * @param height The height of the text field.
 */
@Composable
fun RecipeTextField(
    value:String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    maxLines: Int = 1,
    height: Dp = 56.dp
) {

    var focusState by remember { mutableStateOf(false) }

    val labelColor = if (focusState) colorScheme.primary else colorScheme.secondary
    val labelFontWeight = if (focusState) FontWeight.SemiBold else FontWeight.Normal

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, color = labelColor, fontWeight = labelFontWeight)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder)},
            shape = RoundedCornerShape(8.dp),
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colorScheme.background,
                focusedContainerColor = colorScheme.background,
                focusedBorderColor = colorScheme.primary
            ),
            modifier = Modifier
                .onFocusChanged { focusState = it.isFocused }
                .fillMaxWidth()
                .height(height),
        )
    }

}

@Preview
@Composable
private fun RecipeTextFieldPreview () {
    ONPLATETheme {
        ContentCard(cardTitle = "Overview", modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var title by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }
                RecipeTextField(value = title, onValueChange = {title = it}, label = "Title")
                RecipeTextField(value = description, onValueChange = {description = it}, label = "Description", height = 112.dp, maxLines = 3)
            }
        }

    }
}