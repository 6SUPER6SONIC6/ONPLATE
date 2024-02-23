package com.supersonic.onplate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val directions: List<Step>,
    val cookingTime: Int,
)
