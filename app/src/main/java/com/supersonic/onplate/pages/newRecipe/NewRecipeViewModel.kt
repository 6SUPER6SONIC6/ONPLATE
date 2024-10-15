package com.supersonic.onplate.pages.newRecipe

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.addEmptyIngredient
import com.supersonic.onplate.models.addEmptyStep
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {
    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    private val _screenUiState = MutableStateFlow<NewRecipeUiState>(NewRecipeUiState.BaseContent)
    val screenUiState = _screenUiState.asStateFlow()
    private var previousScreenUiState: NewRecipeUiState? = null

    private val _images = mutableStateListOf<Media>()
    val images = _images

    var isPhotoPickerOpened by mutableStateOf(false)
        private set

    var scrollPosition by mutableIntStateOf(0)
        private set

    init {
        recipeUiState.addEmptyIngredient()
        recipeUiState.addEmptyStep()
    }

    fun updateUiState(newRecipeUiState: RecipeUiState) {
        recipeUiState = newRecipeUiState.copy()
    }

    fun updateScrollPosition(updatedScrollPosition: Int){
        scrollPosition = updatedScrollPosition
    }

    suspend fun saveRecipe() {
        if (recipeUiState.isValid()) {
            recipesRepository.insertRecipe(recipeUiState.toRecipe())
        }
    }

    fun openCamera() {
        _screenUiState.value = NewRecipeUiState.Camera
    }

    fun openPhotoView(initialPhotoIndex: Int) {
        previousScreenUiState = _screenUiState.value
        _screenUiState.value = NewRecipeUiState.PhotoView(initialPhotoIndex)
    }

    fun openPhotoPicker(){
        isPhotoPickerOpened = !isPhotoPickerOpened
    }

    fun navigateBack() {
        _screenUiState.value = previousScreenUiState ?: NewRecipeUiState.BaseContent
        previousScreenUiState = null
    }

    fun setImages(contentResolver: ContentResolver){
        viewModelScope.launch {
            getImages(contentResolver)
        }
    }

    suspend fun getImages(contentResolver: ContentResolver) = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
        )

        val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Query all the device storage volumes instead of the primary only
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val images = mutableListOf<Media>()

        contentResolver.query(
            collectionUri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                val name = cursor.getString(displayNameColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)

                val image = Media(uri, name, size, mimeType)
                images.add(image)
            }
        }

        _images.addAll(images)
    }
}

data class Media(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String
)