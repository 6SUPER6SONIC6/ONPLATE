package com.supersonic.onplate.pages.newRecipe

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.supersonic.onplate.pages.newRecipe.camera.CameraCapture
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.directions.StepsList
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
import com.supersonic.onplate.pages.newRecipe.ingredients.IngredientsList
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.ContentDialog
import com.supersonic.onplate.ui.components.HorizontalImageSlider
import com.supersonic.onplate.ui.components.PrimaryButton
import com.supersonic.onplate.ui.components.RecipeTextField
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.Permission
import kotlinx.coroutines.flow.collectLatest
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
    val screenUiState = viewModel.screenUiState.collectAsState().value

    NewRecipeScreenBody(
        modifier = Modifier.fillMaxSize(),
        recipeUiState = recipeUiState,
        screenUiState = screenUiState,
        onRecipeValueChange = viewModel::updateUiState,
        scrollPosition = viewModel.scrollPosition,
        onScrollPositionChange = viewModel::updateScrollPosition,
        topBarTitle = stringResource(NewRecipeScreenDestination.titleRes),
        openPhotoView = { viewModel.openPhotoView(it) },
        openCamera = { viewModel.openCamera() },
        onNavigateBack = { viewModel.navigateBack() },
        onBackClick = onBackClick,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveRecipe()
                onBackClick()
            }
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeScreenBody(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    screenUiState: NewRecipeUiState,
    onRecipeValueChange: (RecipeUiState) -> Unit,
    scrollPosition: Int = 0,
    onScrollPositionChange: (Int) -> Unit,
    topBarTitle: String,
    openPhotoView: (Int) -> Unit,
    openCamera: () -> Unit,
    onNavigateBack: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {

    var openNavigateBackConfirmationDialog by remember { mutableStateOf(false) }
    var openDeletePhotoDialog by remember { mutableStateOf(false) }
    val contentResolver = LocalContext.current.contentResolver
    var imageToDelete: Uri? by remember { mutableStateOf(null) }

    var requestCameraPermission by remember { mutableStateOf(false) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia() ) { imageUri ->
        recipeUiState.photos.addAll(imageUri)
    }

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))
    val scope = rememberCoroutineScope()

    //Handling back presses through BackHandler to correctly change the screen state
    BackHandler {
        if (screenUiState == NewRecipeUiState.BaseContent){
            openNavigateBackConfirmationDialog = true
        } else {
            onNavigateBack()
        }
    }

    when {
        openNavigateBackConfirmationDialog -> {
            NavigateBackConfirmationDialog(
                onConfirmNavigationBack = {
                    openNavigateBackConfirmationDialog = false
                    onBackClick.invoke()
                                          },
                onCancelNavigationBack = { openNavigateBackConfirmationDialog = false }
            )
        }
        openDeletePhotoDialog -> {
            DeletePhotoDialog(
                onDeleteConfirm = {
                    recipeUiState.removeImage(imageToDelete, contentResolver)
                    openDeletePhotoDialog = false
                    if (recipeUiState.photos.isEmpty()) {
                        onNavigateBack()
                    }
                },
                onDeleteCancel = { openDeletePhotoDialog = false }
            )
        }

        requestCameraPermission -> {
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
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                        openCamera.invoke()
                        requestCameraPermission = false
                    }

                }
                }
            )
        }
    }

    //Screen state
    when(screenUiState) {

       NewRecipeUiState.Camera -> {
            CameraCapture(
                modifier = modifier,
                imageToPreview = recipeUiState.photos.lastOrNull(),
                openImagePreview = { openPhotoView(recipeUiState.photos.lastIndex) },
                onBackClick = onNavigateBack,
                onImageCaptured = { capturedImageUri ->
                recipeUiState.photos.add(capturedImageUri)
            }
            )
        }

        is NewRecipeUiState.PhotoView -> {
            val initialPhotoIndex = screenUiState.initialPhotoIndex
            PhotoView(
                modifier = modifier,
                imageList = recipeUiState.photos,
                initialPhotoIndex = initialPhotoIndex,
                onNavigateBack = onNavigateBack,
                onDeleteClick = { uri ->
                    imageToDelete = uri
                    openDeletePhotoDialog = true
                }
            )

        }

        NewRecipeUiState.BaseContent -> {

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    NewRecipeTopBar(
                    title = topBarTitle,
                        onBackClick = {
                            openNavigateBackConfirmationDialog = true
                        }
                ) },
                content = { paddingValues ->
                    NewRecipeScreenContent(
                        modifier = Modifier.padding(paddingValues),
                        scrollPosition = scrollPosition,
                        onScrollPositionChange = onScrollPositionChange,
                        recipeUiState = recipeUiState,
                        onRecipeValueChange = onRecipeValueChange,
                        onPhotoViewButtonClick = { openPhotoView(it) },
                        onCameraButtonClick = openCamera,
                        onOpenBottomSheet = {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onSaveClick = onSaveClick
                    )
                },
                sheetContent = {
                    BottomSheetContent(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        onTakePhoto = { requestCameraPermission = true },
                        onPickImage = { scope.launch {
                            scaffoldState.bottomSheetState.hide()
                            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        } }
                    )
                },
                sheetPeekHeight = 0.dp
            )
        }

        NewRecipeUiState.PhotoPicker -> TODO()
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
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    onTakePhoto: () -> Unit,
    onPickImage: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier,
            onClick = onTakePhoto
        ) {
            Text(text = "Take a photo")
        }

    Button(
        modifier = Modifier,
        onClick = onPickImage
    ) {
        Text(text = "Select a photo from gallery")
    }

    }

}

@Composable
fun NewRecipeScreenContent(
    modifier: Modifier,
    scrollPosition: Int = 0,
    onScrollPositionChange: (Int) -> Unit,
    recipeUiState: RecipeUiState,
    onRecipeValueChange: (RecipeUiState) -> Unit,
    onOpenBottomSheet: () -> Unit,
    onCameraButtonClick: () -> Unit,
    onPhotoViewButtonClick: (Int) -> Unit,
    onSaveClick: () -> Unit,
) {

    // Remember scroll position after screen state changes
    val scrollState = rememberScrollState(scrollPosition)
    LaunchedEffect(scrollState) {
        snapshotFlow {
            scrollState.value
        }
            .collectLatest { index ->
                onScrollPositionChange(index)
            }
    }

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
        ) {
            OverviewCard(recipeUiState = recipeUiState, onValueChange = onRecipeValueChange)
            IngredientsCard(recipeUiState = recipeUiState)
            DirectionsCard(recipeUiState = recipeUiState)
            PhotosCard(
                photos = recipeUiState.photos,
                recipeUiState = recipeUiState,
                onOpenBottomSheet = onOpenBottomSheet,
                onCameraButtonClick = onCameraButtonClick,
                onPhotoViewButtonClick = {onPhotoViewButtonClick(it)}
                )
            PrimaryButton(
                text = "Save",
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
                removeEnabled = ingredientsList.size > 1,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotosCard(
    photos: List<Uri> = mutableStateListOf(),
    onOpenBottomSheet: () -> Unit,
    onCameraButtonClick: () -> Unit,
    onPhotoViewButtonClick: (Int) -> Unit,
    recipeUiState: RecipeUiState
) {

    var requestCameraPermission by remember { mutableStateOf(false) }
    var requestImagePickerPermissions by remember { mutableStateOf(false) }


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
                                onOpenBottomSheet.invoke()
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

    when{
        requestCameraPermission -> {
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
//        requestImagePickerPermissions -> {
//            val context = LocalContext.current
//            Permission(
//                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                rationale = "READ_EXTERNAL_STORAGE",
//                permissionNotAvailableContent = {
//                    ContentDialog(
//                        title = "Storage Permission",
//                        confirmButtonText = "Go to settings",
//                        cancelButtonText = "Not now",
//                        onConfirm = {
//                            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                                data = Uri.fromParts("package", context.packageName, null)
//                            })
//                        },
//                        onCancel = {
//                            requestImagePickerPermissions = false
//                        }) {
//                        Text(
//                            text = "ONPLATE needs access to the camera so you can take photos and add them to your recipe.\n" +
//                                    "Setting > Permissions > Camera",
//                            style = typography.bodyMedium
//                        )
//                    }
//                },
//                onCancelDialog = { requestImagePickerPermissions = false },
//                onPermissionGranted = { if (it){
//                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                }
//                }
//            )
//        }
    }
}

@Composable
fun PhotoView(
    modifier: Modifier,
    imageList: List<Uri>,
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
        HorizontalImageSlider(
            sliderList = imageList,
            selectedPhoto = { currentPhotoUri = it },
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
private fun NavigateBackConfirmationDialog(
    onConfirmNavigationBack: () -> Unit,
    onCancelNavigationBack: () -> Unit
) {
    ContentDialog(
        title = "Navigate back?",
        confirmButtonText = "Navigate back",
        cancelButtonText = "Stay",
        onConfirm = onConfirmNavigationBack,
        onCancel = onCancelNavigationBack
    ) {
        Text(text = "Are you sure you want to go back?")
    }
}

@Composable
private fun DeletePhotoDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
) {
    ContentDialog(
        title = stringResource(R.string.delete_photo_dialog_title),
        icon = Icons.Outlined.Delete,
        onConfirm = onDeleteConfirm, 
        onCancel = onDeleteCancel,
    ) {
        Text(text = "Delete this photo?")
    }
}



@Preview(showSystemUi = true)
@Composable
private fun NewRecipeScreenContentPreview() {
    ONPLATETheme {
        NewRecipeScreenContent(modifier = Modifier, recipeUiState = RecipeUiState(
            ingredients = mutableListOf(Ingredient()), directions = mutableListOf(Step())
        ), onRecipeValueChange = {}, onCameraButtonClick = {}, onPhotoViewButtonClick = {}, onScrollPositionChange = {}, onOpenBottomSheet = {}) {}
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
        PhotosCard(onCameraButtonClick = {}, onPhotoViewButtonClick = {}, recipeUiState = RecipeUiState(), onOpenBottomSheet = {})
    }
}