package com.example.recipeapp.presentation.ui.RecipeList

import android.annotation.SuppressLint
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.presentation.BaseApplication
import com.example.recipeapp.presentation.components.DefaultSnackBar
import com.example.recipeapp.presentation.components.RecipeCard
import com.example.recipeapp.presentation.components.SearchAppBar
import com.example.recipeapp.presentation.components.ShimmerAnimation
import com.example.recipeapp.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication
    private val viewModel: RecipeListViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(darkTheme = application.isDarkTheme.value) {
                    val focusManager = LocalFocusManager.current
                    val recipes = viewModel.recipes.value
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = viewModel.query.value,
                                selectedCategory = viewModel.selectedCategory.value,
                                focusManager = focusManager,
                                categoryScrollPosition = viewModel.categoryScrollPosition,
                                onQueryChange = viewModel::onQueryChange,
                                newSearch = {
                                            if(viewModel.selectedCategory.value?.value == "Milk") {
                                                lifecycleScope.launch {
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        message = "Invalid Category!",
                                                        actionLabel = "Hide"
                                                    )
                                                }
                                            } else {
                                                viewModel.newSearch()
                                            }
                                },
                                onSelectedCategoryChange = viewModel::onSelectedCategoryChange,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = application::toggleTheme
                            )
                        },
                        scaffoldState = scaffoldState,
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.background)
                        ) {
                            LazyColumn {
                                itemsIndexed(items = recipes) { index, recipe ->
                                    viewModel.onChangeRecipeScrollPosition(index)
                                    if(index + 1 >= viewModel.page.value * PAGE_SIZE && !viewModel.isLoading.value) {
                                        viewModel.nextPage()
                                    }
                                    RecipeCard(
                                        recipe = recipe,
                                        onClick = {
                                            val bundle = Bundle()
                                            bundle.putInt("recipeId", recipe.id?:-1)
                                            findNavController().navigate(R.id.action_recipeListFragment_to_recipeFragment, bundle)
                                        }
                                    )
                                }
                            }

                            if(viewModel.isLoading.value && viewModel.recipes.value.isEmpty()) {
                                ShimmerAnimation()
                            }

                            if (viewModel.isLoading.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(TopCenter)
                                        .padding(top = 32.dp)
                                )
                            }
                            DefaultSnackBar(
                                snackBarHostState = scaffoldState.snackbarHostState,
                                onDismiss = {
                                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                },
                                modifier = Modifier.align(BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}