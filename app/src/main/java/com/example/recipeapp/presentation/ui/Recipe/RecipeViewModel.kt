package com.example.recipeapp.presentation.ui.Recipe

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

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val recipeRepository: RecipeRepository,
    @Named("auth_token") private val token: String
): ViewModel() {
    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val isLoading = mutableStateOf(false)


    fun getRecipe(id: Int) {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000)
            recipe.value = recipeRepository.get(token=token, id=id)
            isLoading.value = false
            Log.d(TAG, "getRecipe: ${recipe.value?.title}")
        }
    }
}