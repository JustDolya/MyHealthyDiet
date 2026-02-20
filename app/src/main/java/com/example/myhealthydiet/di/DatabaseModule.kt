package com.example.myhealthydiet.di

import android.content.Context
import androidx.room.Room
import com.example.myhealthydiet.data.local.room.AppDatabase
import com.example.myhealthydiet.data.local.room.dao.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "MyHealthyDietDatabase"
        )
            .fallbackToDestructiveMigration()
            // .createFromAsset("databases/prepopulated.db") // если будете использовать prepopulated DB
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideDailyNutritionDao(database: AppDatabase): DailyNutritionDao = database.dailyNutritionDao()

    @Provides
    fun provideProductCategoryDao(database: AppDatabase): ProductCategoryDao = database.productCategoryDao()

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao = database.productDao()

    @Provides
    fun provideDishDao(database: AppDatabase): DishDao = database.dishDao()

    @Provides
    fun provideDishCategoryDao(database: AppDatabase): DishCategoryDao = database.dishCategoryDao()

    @Provides
    fun provideConsumptionHistoryDao(database: AppDatabase): ConsumptionHistoryDao = database.consumptionHistoryDao()
}