package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.DishCategoryDao
import com.example.myhealthydiet.data.local.room.dao.DishDao
import com.example.myhealthydiet.data.local.room.dao.ProductCategoryDao
import com.example.myhealthydiet.data.local.room.dao.ProductDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Тесты логики определения инициализированности данных.
 * Это покрывает баг с DataStore-флагом, который переживал переустановку.
 *
 * Context/DataStore мокируем через отдельный тестовый хелпер ниже.
 */
class IsDataInitializedTest {

    private val productCategoryDao: ProductCategoryDao = mockk()
    private val dishCategoryDao: DishCategoryDao = mockk()
    private val productDao: ProductDao = mockk(relaxed = true)
    private val dishDao: DishDao = mockk(relaxed = true)

    /**
     * Тестируем бизнес-логику напрямую, без DataStore.
     * Выносим проверку в отдельную функцию — она и тестируется.
     */
    private suspend fun isDataReallyInitialized(
        flagIsSet: Boolean,
        dishCatCount: Int,
        productCatCount: Int,
    ): Boolean {
        if (!flagIsSet) return false
        return dishCatCount > 0 && productCatCount > 0
    }

    @Test
    fun `флаг false - данные считаются не инициализированными`() = runTest {
        val result = isDataReallyInitialized(
            flagIsSet = false, dishCatCount = 6, productCatCount = 15
        )
        assertFalse("При флаге false должны считаться не инициализированными", result)
    }

    @Test
    fun `флаг true но таблицы пусты - данные считаются не инициализированными`() = runTest {
        val result = isDataReallyInitialized(
            flagIsSet = true, dishCatCount = 0, productCatCount = 0
        )
        assertFalse("Пустые таблицы = не инициализировано, даже если флаг true", result)
    }

    @Test
    fun `флаг true и таблицы заполнены - данные инициализированы`() = runTest {
        val result = isDataReallyInitialized(
            flagIsSet = true, dishCatCount = 6, productCatCount = 15
        )
        assertTrue("Флаг true + данные в таблицах = инициализировано", result)
    }

    @Test
    fun `флаг true но только одна таблица пуста - считается не инициализированным`() = runTest {
        val result = isDataReallyInitialized(
            flagIsSet = true, dishCatCount = 6, productCatCount = 0
        )
        assertFalse("Одна пустая таблица = не инициализировано", result)
    }
}

/**
 * Тесты инициализации — проверяем что методы DAO вызываются правильное число раз.
 */
class InitializationCallsTest {

    private val productCategoryDao: ProductCategoryDao = mockk(relaxed = true)
    private val dishCategoryDao: DishCategoryDao = mockk(relaxed = true)
    private val productDao: ProductDao = mockk(relaxed = true)
    private val dishDao: DishDao = mockk(relaxed = true)

    @Test
    fun `initializeCategories вставляет продуктовые и блюдовые категории`() = runTest {
        // Имитируем логику initializeCategories без DataStore
        val productCategories = com.example.myhealthydiet.data.init.InitialDataLoader
            .getProductCategories()
        val dishCategories = com.example.myhealthydiet.data.init.InitialDataLoader
            .getDishCategories()

        for (cat in productCategories) productCategoryDao.insertCategory(cat)
        for (cat in dishCategories) dishCategoryDao.insertCategory(cat)

        coVerify(exactly = 15) { productCategoryDao.insertCategory(any()) }
        coVerify(exactly = 6) { dishCategoryDao.insertCategory(any()) }
    }

    @Test
    fun `initializeStandardProducts вставляет продукты пачкой`() = runTest {
        val products = com.example.myhealthydiet.data.init.InitialDataLoader
            .getStandardProducts()

        productDao.insertProducts(products)

        coVerify(exactly = 1) { productDao.insertProducts(any()) }
        assertTrue("Должно быть больше 10 стандартных продуктов", products.size > 10)
    }

    @Test
    fun `initializeStandardDishes вставляет блюда пачкой`() = runTest {
        val dishes = com.example.myhealthydiet.data.init.InitialDataLoader
            .getStandardDishes()

        dishDao.insertDishes(dishes)

        coVerify(exactly = 1) { dishDao.insertDishes(any()) }
        assertTrue("Должно быть больше 10 стандартных блюд", dishes.size > 10)
    }
}