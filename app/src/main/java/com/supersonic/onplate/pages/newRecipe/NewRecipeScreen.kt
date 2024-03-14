package com.supersonic.onplate.pages.newRecipe

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.supersonic.onplate.R
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.addEmptyIngredient
import com.supersonic.onplate.models.addEmptyStep
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.removeIngredient
import com.supersonic.onplate.models.removeStep
import com.supersonic.onplate.models.updateIngredientValue
import com.supersonic.onplate.models.updateStepValue
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.directions.StepsList
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
import com.supersonic.onplate.pages.newRecipe.ingredients.IngredientsList
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.ContentDialog
import com.supersonic.onplate.ui.components.PrimaryButton
import com.supersonic.onplate.ui.components.RecipeTextField
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
import kotlinx.coroutines.launch

object NewRecipeScreenDestination : NavigationDestination {
    override val route = "new_recipe"
    override val titleRes = R.string.screenTitle_NewRecipe
}

private val openCamera: MutableState<Boolean> = mutableStateOf(false)

@Composable
fun NewRecipeScreen(
    viewModel: NewRecipeViewModel,
    onBackClick: () -> Unit,
    onNavigateToCamera: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
                 NewRecipeTopBar(onBackClick)
        },
        content = {
                  NewRecipeScreenContent(
                      modifier = Modifier.padding(it),
                      recipeUiState = viewModel.recipeUiState,
                      onRecipeValueChange = viewModel::updateUiState,
                      onSaveClick = {
                          coroutineScope.launch {
                              viewModel.saveRecipe()
                          }
                          onBackClick()
                      },
                      onNavigateToCamera = onNavigateToCamera
                  )
        },
    )
}


@Composable
private fun NewRecipeTopBar(onBackClick: () -> Unit) {
    TopBar(title = stringResource(NewRecipeScreenDestination.titleRes), onBackClick = onBackClick)
}

@Composable
fun NewRecipeScreenContent(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    onRecipeValueChange: (RecipeUiState) -> Unit,
    onSaveClick: () -> Unit,
    onNavigateToCamera: () -> Unit
) {
    val context = LocalContext.current
    if (openCamera.value){

        Permission(
            permission = Manifest.permission.CAMERA,
            rationale = "You said you wanted a picture, so I'm going to have to ask for permission.",
            permissionNotAvailableContent = {
                ContentDialog(title = "I not have permission for a camera =(", onConfirm = {  }, onCancel = {
                    openCamera.value = false
                }) {
                    Text("You have not provided access to the camera. Go to the app settings to grant access.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        })
                    }) {
                        Text(text = "Open Settings")
                    }
                }
            },
            onCancelDialog = { openCamera.value = false }
        ){
            CameraCapture(
                modifier = modifier
            ) { file ->
                recipeUiState.photos.add(file.toUri())
                openCamera.value = false
            }
        }




    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {

            OverviewCard(recipeUiState = recipeUiState, onValueChange = onRecipeValueChange)
            IngredientsCard(recipeUiState = recipeUiState)
            DirectionsCard(recipeUiState = recipeUiState)
            PhotosCard(onNavigateToCamera = onNavigateToCamera, photos = recipeUiState.photos)
            PrimaryButton(text = "Save",
                enabled = recipeUiState.isValid(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                onClick = onSaveClick
            )

        }
    }
}

@Composable
private fun OverviewCard(
    recipeUiState: RecipeUiState,
    onValueChange: (RecipeUiState) -> Unit = {}
) {

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_overview), modifier = Modifier.padding(8.dp)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            // Title TextField
            RecipeTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = recipeUiState.title,
                onValueChange = {onValueChange(recipeUiState.copy(title = it))},
                label = stringResource(R.string.textField_label_title),
                placeholder = stringResource(R.string.textField_placeholder_title),
                singleLine = true,
            )

            // Description TextField
            RecipeTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = recipeUiState.description,
                onValueChange = {onValueChange(recipeUiState.copy(description = it))},
                label = stringResource(R.string.textField_label_description),
                placeholder = stringResource(R.string.textField_placeholder_description),
                maxLines = 3,
                height = 112.dp,
            )

            // Cooking Time TextField
            var openTimePickerDialog by rememberSaveable { mutableStateOf(false) }

            var hour by rememberSaveable { mutableIntStateOf(0) }
            var minute by rememberSaveable { mutableIntStateOf(0) }

            RecipeTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = recipeUiState.cookingTimeString,
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
                        initialHour = recipeUiState.cookingTimeHour,
                        initialMinute = recipeUiState.cookingTimeMinute,
                        onHourSelected = { hour = it },
                        onMinuteSelected = { minute = it },
                        onConfirm = {
                            onValueChange(
                            recipeUiState.copy(
                                cookingTimeHour = hour,
                                cookingTimeMinute = minute,
                                cookingTimeString = when {
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
                        ))},
                        onCancel = { openTimePickerDialog = false }
                    )
                }
            }

        }

    }
}

@Composable
private fun IngredientsCard(
    recipeUiState: RecipeUiState
    ) {

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_ingredients), modifier = Modifier.padding(8.dp)) {

        val ingredientsList = recipeUiState.ingredients

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IngredientsList(
                list = ingredientsList,
                onIngredientValueChange = {id, value ->
                    recipeUiState.updateIngredientValue(id, value)
                },
                onRemoveIngredient = {ingredient ->
                    recipeUiState.removeIngredient(ingredient)
                },
                removeEnabled = ingredientsList.size > 1
                )

            IconButton(
                onClick = { recipeUiState.addEmptyIngredient() },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorScheme.onSecondaryContainer
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
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
private fun DirectionsCard(
    recipeUiState: RecipeUiState,
) {
    val stepsList = recipeUiState.directions

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_directions), modifier = Modifier.padding(8.dp)) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepsList(
                list = stepsList,
                onStepValueChange = { id, value ->
                    recipeUiState.updateStepValue(id, value)
                },
                onRemoveStep = { step ->
                    recipeUiState.removeStep(step)
                },
                removeEnabled = stepsList.size > 1
            )

            IconButton(
                onClick = { recipeUiState.addEmptyStep() },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.padding(top = 8.dp)
            ) {
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
    photos: List<Uri> = emptyList(),
    onNavigateToCamera: () -> Unit
) {


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
                            contentDescription = null
                        )
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
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clickable {


                                            openCamera.value = true
                                        }
                                )
                                Text(text = "Add Photo")
                            }
                        }
                    }
                }
            }
        }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(
    permission: String = Manifest.permission.CAMERA,
    rationale: String = "This permission is important for this app. Please grant the permission.",
    permissionNotAvailableContent: @Composable () -> Unit = {},
    onCancelDialog: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    val permissionState = rememberPermissionState(permission = permission)


    when {
        permissionState.hasPermission -> content()
        permissionState.shouldShowRationale -> permissionNotAvailableContent()
        !permissionState.hasPermission -> Rational(
        text = rationale,
        onRequestPermission = { permissionState.launchPermissionRequest() },
        onCancelDialog = onCancelDialog
    )
    }

//    PermissionRequired(
//        permissionState = permissionState,
//        permissionNotGrantedContent = {
//            Rational(
//                text = rationale,
//                onRequestPermission = { permissionState.launchPermissionRequest() },
//                onCancelDialog = onCancelDialog
//            )
//        },
//        permissionNotAvailableContent = permissionNotAvailableContent,
//        content = content)

}

@Composable
private fun Rational(
    text: String,
    onRequestPermission: () -> Unit,
    onCancelDialog: () -> Unit
) {
    ContentDialog(title = "Permission request", onConfirm = onRequestPermission , onCancel = onCancelDialog) {
        Text(text = text)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NewRecipeScreenContentPreview() {
    ONPLATETheme {
        NewRecipeScreenContent(modifier = Modifier, recipeUiState = RecipeUiState(
            ingredients = mutableListOf(Ingredient()), directions = mutableListOf(Step())
        ), onRecipeValueChange = {}, onSaveClick = {}, onNavigateToCamera = {})
    }
}

@Preview
@Composable
private fun OverviewCardPreview() {
    ONPLATETheme {
        OverviewCard(
            recipeUiState = RecipeUiState()
        )
    }
}

@Preview
@Composable
private fun IngredientsCardPreview() {
    ONPLATETheme {
//        IngredientsCard(viewModel = NewRecipeScreenViewModel())
    }
}

@Preview
@Composable
private fun PhotosCardPreview() {
    ONPLATETheme {
        PhotosCard(onNavigateToCamera = {})
    }
}