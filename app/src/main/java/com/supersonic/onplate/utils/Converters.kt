package com.supersonic.onplate.utils

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient

class Converters {

    @TypeConverter
    fun fromIngredientList(ingredientList: List<Ingredient>): String {
        return Gson().toJson(ingredientList)
    }

    @TypeConverter
    fun toIngredientList(ingredientListString: String): List<Ingredient>{
        val type = object : TypeToken<List<Ingredient>>() {}.type
        return Gson().fromJson(ingredientListString, type)
    }

    @TypeConverter
    fun fromStepList(stepList: List<Step>): String {
        return Gson().toJson(stepList)
    }

    @TypeConverter
    fun toStepList(stepListString: String): List<Step>{
        val type = object : TypeToken<List<Step>>() {}.type
        return Gson().fromJson(stepListString, type)
    }

    @TypeConverter
    fun fromUriList(uriList: List<Uri?>): String {
        return uriList.joinToString(separator = ",") { it.toString() }
    }

    @TypeConverter
    fun toUriList(uriListString: String): List<Uri> {
        return uriListString.split(",").map { Uri.parse(it) }
    }


}