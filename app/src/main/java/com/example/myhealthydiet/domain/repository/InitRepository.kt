package com.example.myhealthydiet.domain.repository

interface InitRepository {
    suspend fun isDataInitialized(): Boolean
    suspend fun initializeCategories()
    suspend fun initializeStandardProducts()
    suspend fun initializeStandardDishes()
    suspend fun markDataAsInitialized()
}