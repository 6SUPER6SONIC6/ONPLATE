package com.supersonic.onplate.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
import com.supersonic.onplate.utils.Converters

@Entity(tableName = "recipes")
@TypeConverters(Converters::class)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val directions: List<Step>,
    val photos: List<Uri>,
    val cookingTimeHour: Int,
    val cookingTimeMinute: Int,
    val cookingTimeString: String,
    @ColumnInfo(defaultValue = "false")
    val favorite: Boolean,
)
