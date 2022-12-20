package com.example.recipeapp.network.responses

import com.example.recipeapp.network.model.RecipeDto
import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse (
    @SerializedName("results")
    var recipes: List<RecipeDto>
)