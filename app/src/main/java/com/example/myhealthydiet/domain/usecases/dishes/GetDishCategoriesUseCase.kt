package com.example.myhealthydiet.domain.usecases.dishes

import com.example.myhealthydiet.domain.models.DishCategory
import com.example.myhealthydiet.domain.repository.DishRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDishCategoriesUseCase @Inject constructor(
    private val dishRepository: DishRepository
) {
    operator fun invoke(): Flow<List<DishCategory>> {
        return dishRepository.getAllCategories()
    }
}