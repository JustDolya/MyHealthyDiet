package com.example.myhealthydiet.domain.repository

interface SyncRepository {
    // Синхронизация: отправка локальных данных в Firebase
    suspend fun syncAllData(): Result<Unit>

    // Проверка и загрузка данных из облака, если они новее
    suspend fun checkAndSyncIfNeeded(): Result<Unit>

    // Принудительная загрузка всех данных в облако
    suspend fun uploadAllData(): Result<Unit>

    // Принудительная загрузка всех данных из облака
    suspend fun downloadAllData(): Result<Unit>

    // Загрузка стандартных продуктов/блюд при первом запуске
    suspend fun loadCommonData(): Result<Unit>
}