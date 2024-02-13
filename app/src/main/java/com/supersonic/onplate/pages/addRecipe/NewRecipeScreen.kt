package com.supersonic.onplate.pages.addRecipe

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.RecipeTextField
import com.supersonic.onplate.ui.components.TimePickerDialog
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun NewRecipeScreen(
    viewModel: NewRecipeScreenViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
                 NewRecipeTopBar(onBackClick)
        },
        content = {
                  NewRecipeScreenContent(modifier = Modifier.padding(it))
        },
    )
}


@Composable
private fun NewRecipeTopBar(onBackClick: () -> Unit) {
    TopBar(title = stringResource(R.string.screen_NewRecipe), onBackClick = onBackClick)
}

@Composable
private fun NewRecipeScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {

        OverviewCard()

        /*Photos
        ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = Modifier.padding(8.dp)) {

            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ) {
                items(photos.size) { photo ->
                    Surface(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, colorScheme.onSecondaryContainer)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photos[photo])
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null)
                    }
                }

                item {
                    Surface(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, colorScheme.onSecondaryContainer)
                    ){
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                                Text(text = "Add Photo")
                            }
                        }
                    }
                }
            }

        } */

    }

}

@Composable
private fun OverviewCard() {

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_overview),) {

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var hour by remember { mutableIntStateOf(0) }
        var minute by remember { mutableIntStateOf(0) }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            RecipeTextField(
                value = title,
                onValueChange = {title = it},
                label = stringResource(R.string.textField_label_title),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            RecipeTextField(
                modifier = Modifier.padding(top = 8.dp),
                value = description,
                onValueChange = {description = it},
                label = stringResource(R.string.textField_label_description),
                maxLines = 3,
                height = 112.dp,
            )

            var openTimePickerDialog by remember { mutableStateOf(false) }

            RecipeTextField(value = when {
                hour == 0 -> "$minute minutes"
                hour == 1 -> "$hour hour $minute minutes"
                hour == 1 && minute == 0 -> "$hour hour"
                minute == 0 -> "$hour hours"


                else -> {"$hour hours $minute minutes"}
            },
                onValueChange = {}, label = stringResource(R.string.textField_label_cooking_time),
                singleLine = true,
                readOnly = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                          LaunchedEffect(interactionSource) {
                              interactionSource.interactions.collect() {
                                  if (it is PressInteraction.Release) {
                                      openTimePickerDialog = true
                                  }
                              }
                          }
                    },
                )

            when {
                openTimePickerDialog -> {
                    TimePickerDialog(
                        initialHour = hour,
                        initialMinute = minute,
                        onHourSelected = {hour = it},
                        onMinuteSelected = {minute = it},
                        onCancel = {openTimePickerDialog = false})
                }
            }

        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun OverviewCardPreview() {
    ONPLATETheme {
        OverviewCard()
    }
}