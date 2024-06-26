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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supersonic.onplate.R
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.addEmptyIngredient
import com.supersonic.onplate.models.addEmptyStep
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.removeImage
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
import com.supersonic.onplate.ui.components.HorizontalSlider
import com.supersonic.onplate.ui.components.PrimaryButton
import com.supersonic.onplate.ui.components.RecipeTextField
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.Permission
import kotlinx.coroutines.launch

object NewRecipeScreenDestination : NavigationDestination {
    override val route = "new_recipe"
    override val titleRes = R.string.screenTitle_NewRecipe
}

@Composable
fun NewRecipeScreen(
    viewModel: NewRecipeViewModel,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val recipeUiState = viewModel.recipeUiState

    NewRecipeScreenBody(
        modifier = Modifier,
        recipeUiState = recipeUiState,
        onRecipeValueChange = viewModel::updateUiState,
        screenUiState = viewModel.screenUiState.collectAsState().value,
        topBarTitle = stringResource(NewRecipeScreenDestination.titleRes),
        openPhotoView = {
            viewModel.openPhotoView(it)
        },
        openCamera = { viewModel.openCamera() },
        onNavigateBack = {
            viewModel.navigateBack()
        },
        onBackClick = onBackClick,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveRecipe()
                onBackClick()
            }
        }
    )
}

@Composable
fun NewRecipeScreenBody(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    screenUiState: NewRecipeUiState,
    onRecipeValueChange: (RecipeUiState) -> Unit,
    topBarTitle: String,
    openPhotoView: (Int) -> Unit,
    openCamera: () -> Unit,
    onNavigateBack: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {

    val contentResolver = LocalContext.current.contentResolver

    when(screenUiState) {
        is NewRecipeUiState.Camera -> {
            CameraCapture(
                modifier = modifier,
                photos = recipeUiState.photos,
                openImagePreview = { openPhotoView(recipeUiState.photos.lastIndex) },
                onBackClick = onNavigateBack,
            ) { capturedImageUri ->
                recipeUiState.photos.add(capturedImageUri)
            }
        }

        is NewRecipeUiState.PhotoView -> {
            val initialPhotoIndex = screenUiState.initialPhotoIndex
            PhotoView(
                modifier = modifier,
                recipeUiState = recipeUiState,
                initialPhotoIndex = initialPhotoIndex,
                onNavigateBack = onNavigateBack,
                onDeleteClick = {uri ->
                    recipeUiState.removeImage(uri, contentResolver)
                })

        }

        NewRecipeUiState.BaseContent -> {
            Scaffold(
                topBar = { NewRecipeTopBar(
                    title = topBarTitle,
                    onBackClick
                ) },
                content = { paddingValues ->
                    NewRecipeScreenContent(
                        modifier = Modifier.padding(paddingValues),
                        recipeUiState = recipeUiState,
                        onRecipeValueChange = onRecipeValueChange,
                        onPhotoViewButtonClick = { openPhotoView(it) },
                        onCameraButtonClick = openCamera,
                        onSaveClick = onSaveClick
                    )
                },
            )
        }
    }

//    when {
//        openCamera -> {
//            CameraCapture(
//                modifier = modifier,
//                photos = recipeUiState.photos,
//                openImagePreview = {
//                    initialPhoto = recipeUiState.photos.lastIndex
//                    openCamera = false
//                    photoPreview = true
//                    isPhotoPreviewOpenedFromCamera = true
//                },
//                onBackClick = {
//                    openCamera = false
//                    isPhotoPreviewOpenedFromCamera = false
//                },
//            ) { capturedImageUri ->
//                recipeUiState.photos.add(capturedImageUri)
//            }
//        }
//
//        photoPreview -> {
//
//            var currentPhotoUri: Uri? = null
//
//            Box(
//                modifier = modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                HorizontalSlider(
//                    sliderList = recipeUiState.photos,
//                    selectedPhoto = {currentPhotoUri = it},
//                    initialPhoto = initialPhoto,
//                    modifier = Modifier.fillMaxSize()
//                )
//
//                IconButton(
//                    onClick = {
//                    currentPhotoUri?.let { recipeUiState.removeImage(it, contentResolver) }
//                },
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(8.dp)
//                ) {
//                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
//                }
//
//                IconButton(
//                    modifier = Modifier
//                        .align(Alignment.TopStart)
//                        .padding(8.dp),
//                    onClick = {
//                    if (isPhotoPreviewOpenedFromCamera){
//                        photoPreview = !photoPreview
//                        openCamera = !openCamera
//                    } else {
//                        photoPreview = !photoPreview
//                    }
//                }) {
//                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
//                }
//
//            }
//        }
//
//        else -> {
//            Scaffold(
//                topBar = { NewRecipeTopBar(
//                    title = topBarTitle,
//                    onBackClick
//                ) },
//                content = {
//                    NewRecipeScreenContent(
//                        modifier = Modifier.padding(it),
//                        recipeUiState = recipeUiState,
//                        onRecipeValueChange = onRecipeValueChange,
//                        onSaveClick = onSaveClick
//                    )
//                },
//            )
//        }
//
//    }

//    if (openCamera){
//
//        CameraCapture(
//            modifier = modifier,
//            photos = recipeUiState.photos,
//            removePhoto = {
//                recipeUiState.removeImage(it, contentResolver)
//                          },
//            onBackClick = { openCamera = false},
//        ) { capturedImageUri ->
//            recipeUiState.photos.add(capturedImageUri)
//        }
//
//    } else {
//
//        Scaffold(
//            topBar = { NewRecipeTopBar(
//                title = topBarTitle,
//                onBackClick
//            ) },
//            content = {
//                NewRecipeScreenContent(
//                    modifier = Modifier.padding(it),
//                    recipeUiState = recipeUiState,
//                    onRecipeValueChange = onRecipeValueChange,
//                    onSaveClick = onSaveClick
//                )
//            },
//        )
//
//    }



}

@Composable
fun PhotoView(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    initialPhotoIndex: Int,
    onNavigateBack: () -> Unit,
    onDeleteClick: (Uri) -> Unit,
) {

    var currentPhotoUri: Uri? = null

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalSlider(
            sliderList = recipeUiState.photos,
            selectedPhoto = {currentPhotoUri = it},
            initialPhoto = initialPhotoIndex,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = { currentPhotoUri?.let { onDeleteClick(it) } },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            onClick = onNavigateBack
        ) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }

    }
}


@Composable
private fun NewRecipeTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopBar(
        title = title,
        onBackClick = onBackClick
    )
}

@Composable
fun NewRecipeScreenContent(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    onRecipeValueChange: (RecipeUiState) -> Unit,
    onCameraButtonClick: () -> Unit,
    onPhotoViewButtonClick: (Int) -> Unit,
    onSaveClick: () -> Unit,
) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            OverviewCard(recipeUiState = recipeUiState, onValueChange = onRecipeValueChange)
            IngredientsCard(recipeUiState = recipeUiState)
            DirectionsCard(recipeUiState = recipeUiState)
            PhotosCard(
                photos = recipeUiState.photos,
                onCameraButtonClick = onCameraButtonClick,
                onPhotoViewButtonClick = {onPhotoViewButtonClick(it)}
                )
            PrimaryButton(text = "Save",
                enabled = recipeUiState.isValid(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                onClick = onSaveClick
            )

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
    onCameraButtonClick: () -> Unit,
    onPhotoViewButtonClick: (Int) -> Unit,
) {

    var requestCameraPermission by remember { mutableStateOf(false) }


        ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = Modifier.padding(8.dp)) {

            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ) {
                items(photos.size) { photo ->
                    Surface(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .padding(4.dp)
                            .clickable {
                                onPhotoViewButtonClick(photo)
                            },
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
                            .padding(4.dp)
                            .clickable {
                                requestCameraPermission = true
                            },
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

                                )
                                Text(text = "Add Photo")
                            }
                        }
                    }
                }
            }
        }

    if (requestCameraPermission){
        val context = LocalContext.current
        Permission(
            permission = Manifest.permission.CAMERA,
            rationale = "You said you wanted a picture, so I'm going to have to ask for permission.",
            permissionNotAvailableContent = {
                ContentDialog(
                    title = "Camera Permission",
                    confirmButtonText = "Go to settings",
                    cancelButtonText = "Not now",
                    onConfirm = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                },
                    onCancel = {
                    requestCameraPermission = false
                }) {
                    Text(
                        text = "ONPLATE needs access to the camera so you can take photos and add them to your recipe.\n" +
                            "Setting > Permissions > Camera",
                        style = typography.bodyMedium
                    )
                }
            },
            onCancelDialog = { requestCameraPermission = false },
            onPermissionGranted = { if (it){
                onCameraButtonClick()
            }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NewRecipeScreenContentPreview() {
    ONPLATETheme {
        NewRecipeScreenContent(modifier = Modifier, recipeUiState = RecipeUiState(
            ingredients = mutableListOf(Ingredient()), directions = mutableListOf(Step())
        ), onRecipeValueChange = {}, onCameraButtonClick = {}, onPhotoViewButtonClick = {}) {}
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
        PhotosCard(onCameraButtonClick = {}, onPhotoViewButtonClick = {})
    }
}