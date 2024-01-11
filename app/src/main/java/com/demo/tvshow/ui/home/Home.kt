package com.demo.tvshow.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.demo.tvshow.BuildConfig
import com.demo.tvshow.R
import com.demo.tvshow.Screen
import com.demo.tvshow.data.entity.TVShowEntity
import com.demo.tvshow.ui.base.showAlert
import com.demo.tvshow.ui.theme.defaultCornerSize
import com.demo.tvshow.ui.theme.fieldBackGroundColor
import com.demo.tvshow.ui.theme.fontMedium
import com.demo.tvshow.ui.theme.paddingLarge
import com.demo.tvshow.ui.theme.paddingMedium
import com.demo.tvshow.ui.theme.primaryTextColor
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Home(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val scrollState = rememberLazyGridState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val trendingTVShows by homeViewModel.trendingShowsFlow.collectAsState(mutableListOf())
    var loading by remember {
        mutableStateOf(false)
    }
    val alertDialog = remember { mutableStateOf(false) }
    val alertMessage = remember {
        mutableStateOf("")
    }
    var isLoadMoreVisible by remember {
        mutableStateOf(false)
    }

    // observer when reached end of list
    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    // act when end of list reached
    LaunchedEffect(endOfListReached) {
        isLoadMoreVisible = endOfListReached
    }



    LaunchedEffect(Unit) {
        homeViewModel.trendingShows.collect {
            when (it) {
                HomeUiState.Empty -> {

                }

                is HomeUiState.Error -> {
                    loading = false
                    alertDialog.value = true
                    alertMessage.value = it.exception ?: ""

                }

                HomeUiState.Loading -> {
                    loading = true
                }

                is HomeUiState.Success -> {
                    loading = false
                    homeViewModel.currentPage = it.currentPage
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingLarge)
    ) {
        OutlinedTextField(
            searchText,
            {
                searchText = it
            },
            placeholder = { Text(text = stringResource(id = R.string.hint_search_tv_shows)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
            ),
            maxLines = 1,
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                homeViewModel.searchTrendingTVShowsThisWeek(searchText.text.trim())
            }),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(defaultCornerSize),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = fieldBackGroundColor,
                focusedContainerColor = fieldBackGroundColor,
                focusedIndicatorColor = fieldBackGroundColor,
                unfocusedIndicatorColor = fieldBackGroundColor,
                focusedTextColor = primaryTextColor
            ),
            leadingIcon = {
                Icon(Icons.Outlined.Search, "Search")
            },

            )
        Box(Modifier.height(paddingMedium))
        Text(
            stringResource(id = R.string.title_most_popular),
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Black, fontSize = fontMedium
            ),
        )
        Box(Modifier.height(paddingMedium))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2), state = scrollState) {
                    items(trendingTVShows.size) { index ->
                        TVShowItem(navController, trendingTVShows[index])
                    }
                }
            }
        }
        if (isLoadMoreVisible) {

            Button(onClick = {
                homeViewModel.getTrendingTVShowsThisWeek(homeViewModel.currentPage)
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(stringResource(id = R.string.btn_load_more))
            }
        }
        showAlert(alertDialog, alertMessage.value)
    }
}


@Composable
fun TVShowItem(navController: NavController, item: TVShowEntity) {
    Card(modifier = Modifier
        .padding(paddingMedium)
        .clickable {
            navController.navigate("${Screen.ShowDetails.route}?serial_id=${item.id}")
        }) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(128.dp)
        ) {

            GlideImage(
                imageModel = { BuildConfig.IMAGE_URL + item.posterPath }, // loading a network image using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center
                )
            )
            Surface(
                color = Color.Black.copy(alpha = 0.25f)
            ) {
                Row(Modifier.padding(paddingMedium)) {
                    Text(
                        item.name ?: "",
                        style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f),
                    )
                    if (item.liked) {
                        Icon(Icons.Outlined.Favorite, "favorite", tint = Color.White)
                    }
                }

            }

        }

    }
}

fun LazyGridState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

