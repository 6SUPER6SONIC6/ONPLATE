package com.supersonic.onplate.pages.newRecipe

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ScaleType
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supersonic.onplate.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    photos: List<Uri> = emptyList(),
    openImagePreview: () -> Unit,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onBackClick: () -> Unit = {},
    onImageCaptured: (Uri) -> Unit,
) {
        Box(modifier = modifier) {
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build()
                )
            }
            val coroutineScope = rememberCoroutineScope()


            CameraPreview(
                modifier = Modifier
                    .align(Alignment.Center),
                scaleType = ScaleType.FIT_CENTER,
                onUseCase = {
                    previewUseCase = it
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val lastImage = photos.lastOrNull()
                val placeholder = R.mipmap.ic_launcher

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            if (lastImage != null){
                                openImagePreview()
                            }
                        },
                    shape = CircleShape,
//                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(lastImage ?: placeholder)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                }

                Box(modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            takePhoto(
                                context = context,
                                imageCapture = imageCaptureUseCase,
                                executor = context.executor,
                                onImageCaptured = onImageCaptured
                            )
                        }
                    }
                )

                IconButton(
                    onClick = onBackClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            CircleShape
                        )
                        .size(48.dp)
                ){
                  Icon(
                      imageVector = if (photos.isEmpty()) Icons.Outlined.Close else Icons.Outlined.Check,
                      modifier = Modifier.size(32.dp),
                      contentDescription = null
                  )

                }
            }

            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("camera", "Failed to bind camera use cases", ex)
                }
            }
        }


}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: ScaleType = ScaleType.FILL_CENTER,
    onUseCase: (UseCase) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            onUseCase(
                Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            )

            previewView
        }

    )
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    ) {

    val appName = context.resources.getString(R.string.app_name)
    val name = "$appName ${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
        .format(System.currentTimeMillis())}"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appName")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {

            override fun onError(exception: ImageCaptureException) {
                Log.e("camera_takePicture", "Image capture failed", exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let { onImageCaptured(it) }
            }
        }
    )

}
private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, executor
        )
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)
