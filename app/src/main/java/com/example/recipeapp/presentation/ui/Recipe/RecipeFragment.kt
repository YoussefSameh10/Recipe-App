package com.example.recipeapp.presentation.ui.Recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.example.recipeapp.R
import com.example.recipeapp.presentation.BaseApplication
import com.example.recipeapp.presentation.theme.AppTheme
import com.example.recipeapp.presentation.ui.RecipeList.RecipeListViewModel
import com.example.recipeapp.utils.DEFAULT_RECIPE_IMAGE
import com.example.recipeapp.utils.loadPicture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeId")?.let { recipeId ->
            viewModel.getRecipe(recipeId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {    
                val recipe = viewModel.recipe.value
                AppTheme(darkTheme = application.isDarkTheme.value) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center
                    ) {
                        recipe?.featuredImage?.let { url ->
                            val image = loadPicture(url = url, defaultImage = DEFAULT_RECIPE_IMAGE).value
                            image?.let { img ->
                                Image(
                                    img.asImageBitmap(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(300.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        if(viewModel.isLoading.value) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .padding(top = 32.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            recipe?.title?.let { title ->
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = SpaceBetween
                                ) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.h5,
                                        modifier = Modifier.fillMaxWidth(0.85f)
                                    )

                                    val rating = recipe.rating.toString()

                                    Text(
                                        text = rating,
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(End)
                                            .align(CenterVertically)
                                    )
                                }
                            }

                            recipe?.publisher?.let { publisher ->
                                val updated = recipe.dateUpdated
                                Text(
                                    text = if(updated != null) {
                                        "Updated $updated by $publisher"
                                    }
                                    else {
                                        "By $publisher"
                                    }
                                    ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                    ,
                                    style = MaterialTheme.typography.caption
                                )
                            }

                            recipe?.ingredients?.let { ingredients ->
                                for(ingredient in ingredients) {
                                    Text(
                                        text = ingredient,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp),
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}