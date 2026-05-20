package com.example.myhealthydiet.domain.usecases.history

import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.models.IngredientItem
import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.repository.DishRepository
import com.example.myhealthydiet.domain.repository.HistoryRepository
import com.example.myhealthydiet.domain.repository.NutritionRepository
import com.example.myhealthydiet.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Тесты добавления еды в рацион.
 * Проверяем: правильно ли считается КБЖУ, вызываются ли нужные методы репозиториев.
 */
class AddConsumptionUseCaseTest {

    // Моки репозиториев
    private val historyRepository: HistoryRepository = mockk(relaxed = true)
    private val productRepository: ProductRepository = mockk()
    private val dishRepository: DishRepository = mockk()
    private val nutritionRepository: NutritionRepository = mockk(relaxed = true)

    private lateinit var useCase: AddConsumptionUseCase

    // Тестовый продукт: 100 г = 180 ккал, Б23 Ж18 У1
    private val testProduct = Product(
        id = 1, categoryId = 2, name = "Куриная грудка",
        calories = 180, proteins = 23, fats = 18, carbs = 1, isCustom = false
    )

    // Тестовое блюдо: целое блюдо = 500 ккал, Б30 Ж20 У50
    private val testDish = Dish(
        id = 1, categoryId = 1, name = "Сырники",
        minutesToCook = 20, ingredients = emptyList<IngredientItem>(), steps = "",
        calories = 500, proteins = 30, fats = 20, carbs = 50,
        imageUri = null, isCustom = false
    )

    @Before
    fun setUp() {
        useCase = AddConsumptionUseCase(
            historyRepository = historyRepository,
            productRepository = productRepository,
            dishRepository = dishRepository,
            nutritionRepository = nutritionRepository,
        )
    }

    // ── Продукты ──────────────────────────────────────────────────────────────

    @Test
    fun `добавление продукта 150г - КБЖУ рассчитывается пропорционально`() = runTest {
        coEvery { productRepository.getProductById(1) } returns testProduct

        val result = useCase(foodId = 1, isDish = false, grams = 150)

        assertTrue("Должен вернуть успех", result.isSuccess)
        // При 150г: 180 * 1.5 = 270 ккал, 23 * 1.5 = 34 г белка
        coVerify {
            historyRepository.addConsumption(match { history ->
                history.calories == 270 && history.proteins == 35
            })
        }
    }

    @Test
    fun `добавление продукта 0 граммов - возвращает ошибку`() = runTest {
        val result = useCase(foodId = 1, isDish = false, grams = 0)
        assertTrue("Нулевые граммы должны вернуть ошибку", result.isFailure)
    }

    @Test
    fun `добавление несуществующего продукта - возвращает ошибку`() = runTest {
        coEvery { productRepository.getProductById(999) } returns null

        val result = useCase(foodId = 999, isDish = false, grams = 100)
        assertTrue("Несуществующий продукт должен вернуть ошибку", result.isFailure)
    }

    @Test
    fun `добавление продукта вычитает КБЖУ из дневной нормы`() = runTest {
        coEvery { productRepository.getProductById(1) } returns testProduct

        useCase(foodId = 1, isDish = false, grams = 100)

        coVerify { nutritionRepository.subtractNutrition(180, 23, 18, 1) }
    }

    // ── Блюда ─────────────────────────────────────────────────────────────────

    @Test
    fun `добавление блюда 50 грамм порции - КБЖУ уменьшается вдвое`() = runTest {
        coEvery { dishRepository.getDishById(1) } returns testDish

        val result = useCase(foodId = 1, isDish = true, grams = 50)

        assertTrue("Должен вернуть успех", result.isSuccess)
        // 50% от блюда: 500 * 0.5 = 250 ккал
        coVerify {
            historyRepository.addConsumption(match { history ->
                history.calories == 250 && history.isDish
            })
        }
    }

    @Test
    fun `добавление блюда 100 грамм - КБЖУ равно полному блюду`() = runTest {
        coEvery { dishRepository.getDishById(1) } returns testDish

        useCase(foodId = 1, isDish = true, grams = 100)

        coVerify {
            historyRepository.addConsumption(match { history ->
                history.calories == 500 && history.proteins == 30
            })
        }
    }

    @Test
    fun `добавление несуществующего блюда - возвращает ошибку`() = runTest {
        coEvery { dishRepository.getDishById(999) } returns null

        val result = useCase(foodId = 999, isDish = true, grams = 100)
        assertTrue("Несуществующее блюдо должно вернуть ошибку", result.isFailure)
    }
}