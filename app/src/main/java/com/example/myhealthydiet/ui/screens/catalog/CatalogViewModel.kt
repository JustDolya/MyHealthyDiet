package com.example.myhealthydiet.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.models.DishCategory
import com.example.myhealthydiet.domain.models.IngredientItem
import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.models.ProductCategory
import com.example.myhealthydiet.domain.repository.DishRepository
import com.example.myhealthydiet.domain.repository.ProductRepository
import com.example.myhealthydiet.domain.usecases.dishes.AddCustomDishUseCase
import com.example.myhealthydiet.domain.usecases.dishes.GetDishCategoriesUseCase
import com.example.myhealthydiet.domain.usecases.dishes.GetDishesUseCase
import com.example.myhealthydiet.domain.usecases.history.AddConsumptionUseCase
import com.example.myhealthydiet.domain.usecases.products.AddCustomProductUseCase
import com.example.myhealthydiet.domain.usecases.products.GetProductCategoriesUseCase
import com.example.myhealthydiet.domain.usecases.products.GetProductsUseCase
import com.example.myhealthydiet.domain.usecases.products.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogUiState(
    val selectedDish: Dish? = null,
    val selectedProduct: Product? = null,
    val productSearchQuery: String = "",
    val isLoading: Boolean = false,
    val addSuccess: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getDishCategoriesUseCase: GetDishCategoriesUseCase,
    private val getDishesUseCase: GetDishesUseCase,
    private val getProductCategoriesUseCase: GetProductCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val addConsumptionUseCase: AddConsumptionUseCase,
    private val addCustomDishUseCase: AddCustomDishUseCase,
    private val addCustomProductUseCase: AddCustomProductUseCase,
    private val dishRepository: DishRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState

    val dishCategories: StateFlow<List<DishCategory>> = getDishCategoriesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val productCategories: StateFlow<List<ProductCategory>> = getProductCategoriesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDishCategoryId = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val dishesByCategory: StateFlow<List<Dish>> = _selectedDishCategoryId
        .flatMapLatest { id ->
            if (id > 0) getDishesUseCase.byCategory(id) else getDishesUseCase()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProductCategoryId = MutableStateFlow(0)
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val productsByCategory: StateFlow<List<Product>> =
        combine(_selectedProductCategoryId, _searchQuery) { id, query -> Pair(id, query) }
            .flatMapLatest { (id, query) ->
                when {
                    query.isNotBlank() -> searchProductsUseCase(query)
                    id > 0             -> getProductsUseCase.byCategory(id)
                    else               -> getProductsUseCase()
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setDishCategory(categoryId: Int) {
        _selectedDishCategoryId.value = 0
        _selectedDishCategoryId.value = categoryId
    }

    fun setProductCategory(categoryId: Int) {
        _searchQuery.value = ""
        _uiState.update { it.copy(productSearchQuery = "") }
        _selectedProductCategoryId.value = 0
        _selectedProductCategoryId.value = categoryId
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(productSearchQuery = query) }
    }

    fun clearAddSuccess() = _uiState.update { it.copy(addSuccess = false) }
    fun clearError() = _uiState.update { it.copy(error = null) }

    fun loadDishById(dishId: Int) {
        if (_uiState.value.selectedDish?.id == dishId) return
        viewModelScope.launch {
            val dish = dishRepository.getDishById(dishId)
            _uiState.update { it.copy(selectedDish = dish) }
        }
    }

    fun loadProductById(productId: Int) {
        if (_uiState.value.selectedProduct?.id == productId) return
        viewModelScope.launch {
            val product = productRepository.getProductById(productId)
            _uiState.update { it.copy(selectedProduct = product) }
        }
    }

    fun addDishToRation(dishId: Int, portionPercent: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = addConsumptionUseCase(foodId = dishId, isDish = true, grams = portionPercent)
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, addSuccess = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Ошибка")
            }
        }
    }

    fun addProductToRation(productId: Int, grams: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = addConsumptionUseCase(foodId = productId, isDish = false, grams = grams)
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, addSuccess = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Ошибка")
            }
        }
    }

    fun addCustomDish(
        name: String,
        categoryId: Int,
        minutesToCook: Int,
        ingredients: List<IngredientItem>,
        steps: String,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int,
        imageUri: String?,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = addCustomDishUseCase(
                name = name,
                categoryId = categoryId,
                minutesToCook = minutesToCook,
                ingredients = ingredients,
                steps = steps,
                calories = calories,
                proteins = proteins,
                fats = fats,
                carbs = carbs,
                imageUri = imageUri,
            )
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, addSuccess = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Ошибка")
            }
        }
    }

    fun addCustomProduct(
        name: String,
        categoryId: Int,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = addCustomProductUseCase(
                name = name,
                categoryId = categoryId,
                calories = calories,
                proteins = proteins,
                fats = fats,
                carbs = carbs,
            )
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, addSuccess = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Ошибка")
            }
        }
    }
}