package com.supersonic.onplate.data

import com.supersonic.onplate.db.RecipeDao
import com.supersonic.onplate.models.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecipesRepository @Inject constructor(private val recipeDao: RecipeDao) {

    fun getAllRecipesStream(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    fun getRecipeStream(id: Int) : Flow<Recipe?> = recipeDao.getRecipe(id)

    suspend fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)

    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)
}