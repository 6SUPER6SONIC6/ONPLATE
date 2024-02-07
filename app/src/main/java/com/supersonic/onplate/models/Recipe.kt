package com.supersonic.onplate.models

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients:List<String>,
    val directions: List<String>,
    val cookingTime: Int,
    val photos: List<String>
)
