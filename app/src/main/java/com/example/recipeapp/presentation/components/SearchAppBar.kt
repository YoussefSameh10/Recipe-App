package com.example.recipeapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.ui.RecipeList.FoodCategory
import com.example.recipeapp.presentation.ui.RecipeList.getAllFoodCategories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchAppBar(
    query: String,
    selectedCategory: FoodCategory?,
    focusManager: FocusManager,
    categoryScrollPosition: Int,
    onQueryChange: (String) -> Unit,
    newSearch: () -> Unit,
    onSelectedCategoryChange: (FoodCategory) -> Unit,
    onChangeCategoryScrollPosition: (Int) -> Unit,
    onToggleTheme: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp),
                    value = query,
                    onValueChange = {onQueryChange(it)},
                    label = {
                        Text("Search")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, "")
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            newSearch()
                        }
                    ),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )

                IconButton(
                    modifier = Modifier.align(CenterVertically),
                    onClick = onToggleTheme
                ) {
                    Icon(Icons.Filled.MoreVert, "")
                }
            }

            val scrollState = rememberScrollState()
            Row(modifier = Modifier
                .horizontalScroll(scrollState)
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    scrollState.scrollTo(categoryScrollPosition)
                }
                for(category in getAllFoodCategories()) {
                    Chip(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            disabledBackgroundColor = Color.LightGray
                        ),
                        enabled = selectedCategory != category,
                        onClick = {
                            onSelectedCategoryChange(category)
                            newSearch()
                            onChangeCategoryScrollPosition(scrollState.value)
                        }
                    ) {
                        Text(
                            category.value,
                            style = MaterialTheme.typography.body2,
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}