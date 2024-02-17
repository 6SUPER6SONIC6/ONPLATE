package com.supersonic.onplate.pages.newRecipe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supersonic.onplate.R
import com.supersonic.onplate.pages.newRecipe.Iingredients.IngredientsList
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
                  NewRecipeScreenContent(modifier = Modifier.padding(it), viewModel = viewModel)
        },
    )
}


@Composable
private fun NewRecipeTopBar(onBackClick: () -> Unit) {
    TopBar(title = stringResource(R.string.screenTitle_NewRecipe), onBackClick = onBackClick)
}

@Composable
private fun NewRecipeScreenContent(modifier: Modifier, viewModel: NewRecipeScreenViewModel) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {

        OverviewCard(modifier)
        IngredientsCard(modifier = modifier, viewModel = viewModel)
        PhotosCard(modifier)

    }

}

@Composable
private fun OverviewCard(modifier: Modifier) {

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_overview), modifier = modifier.padding(8.dp)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            // Title TextField
            var title by rememberSaveable { mutableStateOf("") }

            RecipeTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = title,
                onValueChange = {title = it},
                label = stringResource(R.string.textField_label_title),
                placeholder = stringResource(R.string.textField_placeholder_title),
                singleLine = true,
            )

            // Description TextField
            var description by rememberSaveable { mutableStateOf("") }

            RecipeTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = description,
                onValueChange = {description = it},
                label = stringResource(R.string.textField_label_description),
                placeholder = stringResource(R.string.textField_placeholder_description),
                maxLines = 3,
                height = 112.dp,
            )

            // Cooking Time TextField
            var openTimePickerDialog by rememberSaveable { mutableStateOf(false) }
            var hour by rememberSaveable { mutableIntStateOf(0) }
            var minute by rememberSaveable { mutableIntStateOf(0) }
            val cookingTimeValue = when {
                hour == 0 && minute == 0 -> ""
                hour == 1 && minute == 0 -> "$hour hour"
                hour == 0 && minute == 1 -> "$minute minute"
                hour == 1 && minute == 1 -> "$hour hour $minute minute"
                hour == 1 && minute > 1 -> "$hour hour $minute minutes"
                hour > 1 && minute == 1 -> "$hour hours $minute minute"
                hour == 0 -> "$minute minutes"
                minute == 0 -> "$hour hours"

                else -> {"$hour hours $minute minutes"}
            }

            RecipeTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = cookingTimeValue,
                onValueChange = {},
                label = stringResource(R.string.textField_label_cooking_time),
                placeholder = stringResource(R.string.textField_placeholder_cooking_time),
                singleLine = true,
                readOnly = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                          LaunchedEffect(interactionSource) {
                              interactionSource.interactions.collect {
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
                        onHourSelected = { hour = it },
                        onMinuteSelected = { minute = it },
                        onCancel = { openTimePickerDialog = false }
                    )
                }
            }

        }

    }
}

@Composable
private fun IngredientsCard(
    modifier: Modifier,
    viewModel: NewRecipeScreenViewModel,
    ) {

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_ingredients), modifier = modifier.padding(8.dp)) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IngredientsList(
                list = viewModel.ingredients,
                onRemoveIngredient = {ingredient ->
                    viewModel.removeIngredient(ingredient)
                },
                removeEnabled = viewModel.ingredients.size > 1
                )

            IconButton(
                onClick = { viewModel.addEmptyIngredient() }, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorScheme.onSecondaryContainer
                )) {
                Icon(
                    Icons.Filled.Add,
                    modifier = Modifier.size(32.dp),
                    contentDescription = null
                )
            }

        }
    }
}



@Composable
private fun PhotosCard(
    modifier: Modifier,
    photos: List<String> = emptyList()) {

        ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = modifier.padding(8.dp)) {

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

        }
}

@Preview(showSystemUi = true)
@Composable
private fun NewRecipeScreenContentPreview() {
    ONPLATETheme {
        NewRecipeScreenContent(Modifier, viewModel = NewRecipeScreenViewModel())
    }
}

@Preview
@Composable
private fun OverviewCardPreview() {
    ONPLATETheme {
        OverviewCard(Modifier)
    }
}

@Preview
@Composable
private fun IngredientsCardPreview() {
    ONPLATETheme {
        IngredientsCard(Modifier, viewModel = NewRecipeScreenViewModel())
    }
}

@Preview
@Composable
private fun PhotosCardPreview() {
    ONPLATETheme {
        PhotosCard(Modifier)
    }
}