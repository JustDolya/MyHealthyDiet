package com.example.myhealthydiet.domain.usecases.dishes

import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.repository.DishRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDishesUseCase @Inject constructor(
    private val dishRepository: DishRepository
) {
    operator fun invoke(): Flow<List<Dish>> {
        return dishRepository.getAllDishes()
    }

    fun byCategory(categoryId: Int): Flow<List<Dish>> {
        return dishRepository.getDishesByCategory(categoryId)
    }
}