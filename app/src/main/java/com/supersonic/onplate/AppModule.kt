package com.supersonic.onplate

import android.content.Context
import com.supersonic.onplate.db.OnplateDatabase
import com.supersonic.onplate.db.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOnplateDatabase(@ApplicationContext context: Context): OnplateDatabase {
        return OnplateDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRecipeDao(onplateDatabase: OnplateDatabase): RecipeDao {
        return onplateDatabase.recipeDao()
    }
}