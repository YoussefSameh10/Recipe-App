package com.example.recipeapp.presentation.ui.RecipeList

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.presentation.TAG
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String
): ViewModel(){
    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    var categoryScrollPosition = 0
    var isLoading = mutableStateOf(false)
    val page = mutableStateOf(1)
    var recipeListScrollPosition = 0

    init {
        newSearch()
    }

    fun newSearch() {
        isLoading.value = true
        resetSearchState()
        viewModelScope.launch {
            delay(2000)
            val result = repository.search(
                token = token,
                page = 1,
                query = query.value
            )
            recipes.value = result
            isLoading.value = false
        }
    }

    fun nextPage() {
        viewModelScope.launch {
            if((recipeListScrollPosition + 1) >= page.value * PAGE_SIZE) {
                isLoading.value = true
                incrementPage()

                delay(3000)
                Log.d(TAG, "nextPage: ${page.value}")
                if(page.value > 1) {
                    val result = repository.search(
                        token = token,
                        page = page.value,
                        query = query.value
                    )
                    appendRecipes(result)
                }
                isLoading.value = false
            }
        }
    }

    fun onQueryChange(query: String) {
        this.query.value = query
    }

    fun onSelectedCategoryChange(category: FoodCategory) {
        selectedCategory.value = category
        onQueryChange(category.value)
    }

    fun onChangeCategoryScrollPosition(position: Int) {
        categoryScrollPosition = position
    }

    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
        if(selectedCategory.value?.value != query.value) {
            clearSelectedCategory()
        }
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }
    private fun clearSelectedCategory() {
        selectedCategory.value = null
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }

    private fun appendRecipes(recipesToAdd: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipesToAdd)
        this.recipes.value = current
    }
}