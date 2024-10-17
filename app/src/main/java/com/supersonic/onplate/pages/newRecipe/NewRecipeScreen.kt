package com.supersonic.onplate.pages.newRecipe

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.supersonic.onplate.data.Media
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
import com.supersonic.onplate.pages.newRecipe.components.CameraCapture
import com.supersonic.onplate.pages.newRecipe.components.CameraPermissionDialog
import com.supersonic.onplate.pages.newRecipe.components.DeletePhotoDialog
import com.supersonic.onplate.pages.newRecipe.components.ImagesModalBottomSheet
import com.supersonic.onplate.pages.newRecipe.components.Ingredient
import com.supersonic.onplate.pages.newRecipe.components.IngredientsList
import com.supersonic.onplate.pages.newRecipe.components.NavigateBackConfirmationDialog
import com.supersonic.onplate.pages.newRecipe.components.ReadImagesPermissionDialog
import com.supersonic.onplate.pages.newRecipe.components.Step
import com.supersonic.onplate.pages.newRecipe.components.StepsList
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.HorizontalImageSlider
import com.supersonic.onplate.ui.components.PrimaryButton
import com.supersonic.onplate.ui.components.RecipeTextField
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
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
    val images = viewModel.images.collectAsState()

    NewRecipeScreenBody(
        modifier = Modifier.fillMaxSize(),
        recipeUiState = recipeUiState,
        screenUiState = screenUiState,
        galleryPhotos = images.value,
        updateImages = viewModel::loadImages,
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
                onBackClick.invoke()
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
    galleryPhotos: List<Media>,
    updateImages: (ContentResolver) -> Unit,
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
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    var imageToDelete: Uri? by remember { mutableStateOf(null) }


    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    var requestCameraPermission by remember { mutableStateOf(false) }
    var requestImagePickerPermission by remember { mutableStateOf(false) }
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

        openBottomSheet -> {
            updateImages.invoke(contentResolver)
            ImagesModalBottomSheet(
                photos = galleryPhotos,
                sheetState = bottomSheetState,
                requestImagePickerPermission = {
                    openBottomSheet = false
                    requestImagePickerPermission = true
                },
                onTakePhoto = {
                    scope.launch {
                        bottomSheetState.hide()
                        openBottomSheet = false
                        requestCameraPermission = true
                    }
                              },
                onPickImage ={ recipeUiState.photos.add(it) },
                onDismissRequest = { openBottomSheet = false }
            )
        }

        requestCameraPermission -> {
            CameraPermissionDialog(
                context = context,
                onCancel = {
                    requestCameraPermission = false
                    openBottomSheet = true
                },
                onPermissionGranted = {
                    if (it){
                        openCamera.invoke()
                        requestCameraPermission = false
                    }
                }
            )
        }

        requestImagePickerPermission -> {
            ReadImagesPermissionDialog(
                context = context,
                onCancel = {
                    requestImagePickerPermission = false
                    openBottomSheet = true
                },
                onPermissionGranted = {
                    if (it) requestImagePickerPermission = false
                    openBottomSheet = true
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

            Scaffold(
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
                        galleryPhotos = galleryPhotos,
                        scrollPosition = scrollPosition,
                        onScrollPositionChange = onScrollPositionChange,
                        recipeUiState = recipeUiState,
                        onRecipeValueChange = onRecipeValueChange,
                        onOpenBottomSheet = {
                            scope.launch {
                                bottomSheetState.show()
                            }
                            openBottomSheet = true
                        },
                        onPhotoViewButtonClick = { openPhotoView(it) },
                        onSaveClick = onSaveClick
                    )
                },
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
fun NewRecipeScreenContent(
    modifier: Modifier,
    scrollPosition: Int = 0,
    onScrollPositionChange: (Int) -> Unit,
    galleryPhotos: List<Media>,
    recipeUiState: RecipeUiState,
    onRecipeValueChange: (RecipeUiState) -> Unit,
    onOpenBottomSheet: () -> Unit,
    onPhotoViewButtonClick: (Int) -> Unit,
    onSaveClick: () -> Unit,
) {

    // Remember scroll position after screen state changes
//    val scrollState = rememberScrollState(scrollPosition)
//    LaunchedEffect(scrollState) {
//        snapshotFlow {
//            scrollState.value
//        }
//            .collectLatest { index ->
//                onScrollPositionChange(index)
//            }
//    }

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            OverviewCard(recipeUiState = recipeUiState, onValueChange = onRecipeValueChange)
            IngredientsCard(recipeUiState = recipeUiState)
            DirectionsCard(recipeUiState = recipeUiState)
            PhotosCard(
                photosUriList = recipeUiState.photos,
                onOpenBottomSheet = onOpenBottomSheet,
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
private fun IngredientsCard(recipeUiState: RecipeUiState) {

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
private fun DirectionsCard(recipeUiState: RecipeUiState) {
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
    photosUriList: List<Uri> = mutableStateListOf(),
    onOpenBottomSheet: () -> Unit,
    onPhotoViewButtonClick: (Int) -> Unit
) {
    ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = Modifier.padding(8.dp)) {
            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ) {
                items(photosUriList) { photoUri ->
                    Surface(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .padding(4.dp)
                            .clickable {
                                onPhotoViewButtonClick(photosUriList.indexOf(photoUri))
                            },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, colorScheme.onSecondaryContainer)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photoUri)
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
            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun NewRecipeScreenContentPreview() {
    ONPLATETheme {
        NewRecipeScreenContent(modifier = Modifier, onScrollPositionChange = {}, recipeUiState = RecipeUiState(
            ingredients = mutableListOf(Ingredient()), directions = mutableListOf(Step())
        ), onRecipeValueChange = {}, onOpenBottomSheet = {}, onPhotoViewButtonClick = {}, galleryPhotos = listOf(), onSaveClick = {})
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
        PhotosCard(onOpenBottomSheet = {}, onPhotoViewButtonClick = {})
    }
}