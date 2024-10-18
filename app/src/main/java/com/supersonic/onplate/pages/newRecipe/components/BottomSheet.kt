package com.supersonic.onplate.pages.newRecipe.components

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView.ScaleType
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.supersonic.onplate.data.Media
import com.supersonic.onplate.ui.components.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ImagesModalBottomSheet(
    modifier: Modifier = Modifier,
    photos: List<Media>,
    requestImagePickerPermission: () -> Unit,
    onTakePhoto: () -> Unit,
    onPickImage: (Uri) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val state = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = state,
//                containerColor = colorScheme.secondaryContainer,
        dragHandle = {
            BottomSheetDragHandle(
                state = state,
                onBackClick = {
                    coroutineScope.launch {
                        state.hide()
                        onDismissRequest.invoke()
                    }
                }
            )
                     },
        modifier = modifier
    ) {
        BottomSheetContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            photos = photos,
            requestImagePickerPermission = requestImagePickerPermission,
            onTakePhoto = onTakePhoto,
            onPickImage = { onPickImage(it) }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BottomSheetContent(
    modifier: Modifier = Modifier,
    photos: List<Media>,
    requestImagePickerPermission: () -> Unit,
    onTakePhoto: () -> Unit,
    onPickImage: (Uri) -> Unit
) {
    val permission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        READ_MEDIA_IMAGES
    } else {
        READ_EXTERNAL_STORAGE
    }

    val coroutineScope = rememberCoroutineScope()
    val permissionState = rememberPermissionState(permission)
    val gridState = rememberLazyGridState()
    val isAtTop by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
        }
    }

    Box(modifier = modifier){

        LazyVerticalGrid(
            modifier = Modifier,
            state = gridState,
            columns = GridCells.Adaptive(110.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .padding(2.dp)
                        .clickable { onTakePhoto.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    BottomSheetCameraView(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp))
                    )
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            tint = colorScheme.onPrimary,
                            contentDescription = null
                        )
                }
            }
            if (permissionState.hasPermission) {
                items(photos) { photo ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onPickImage.invoke(photo.uri) }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.uri)
                                .crossfade(true)
//                            .diskCachePolicy(CachePolicy.ENABLED)
//                            .placeholder(R.drawable.ic_launcher_background)
                                .build(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clickable { requestImagePickerPermission.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                tint = colorScheme.error,
                                contentDescription = null
                            )
                            Text(
                                text = "Tap to allow access to your Gallery",
                                textAlign = TextAlign.Center,
                                style = typography.labelSmall,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
        if (!isAtTop) {
            IconButton(
                onClick = {
                    coroutineScope.launch{ gridState.animateScrollToItem(1) }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = colorScheme.surfaceContainer,
                    contentColor = colorScheme.onSurface
                ),
                modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.rotate(90f),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun BottomSheetCameraView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var previewUseCase by remember { mutableStateOf<UseCase>(androidx.camera.core.Preview.Builder().build()) }

    CameraPreview(
        modifier = modifier,
        scaleType = ScaleType.FILL_CENTER,
        onUseCase = {
            previewUseCase = it
        }
    )

    LaunchedEffect(previewUseCase) {
        val cameraProvider = context.getCameraProvider()
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, previewUseCase
            )
        } catch (ex: Exception){
            Log.e("camera", "Failed to bind camera use cases", ex)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun BottomSheetDragHandle (
    modifier: Modifier = Modifier,
    animationDuration: Int = 150,
    state: SheetState,
    onBackClick: () -> Unit
) {

    AnimatedContent(
        modifier = modifier,
        targetState = state.targetValue,
        contentAlignment = Alignment.Center,
        transitionSpec = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(durationMillis = animationDuration),
            ) + fadeIn(tween(durationMillis = 50)) togetherWith slideOutOfContainer(
                         towards = AnimatedContentTransitionScope.SlideDirection.Down,
                         animationSpec = tween(durationMillis = animationDuration)
                     ) + fadeOut(tween(durationMillis = 50))
        },
        label = "dragHandle animation"
    ) { targetState ->
        when(targetState){
            SheetValue.Expanded -> {
                TopBar(
                    title = "Select Photos",
                    onBackClick = onBackClick
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BottomSheetDefaults.DragHandle()
                }
            }
        }
    }
}