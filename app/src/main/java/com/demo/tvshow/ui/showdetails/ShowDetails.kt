package com.demo.tvshow.ui.showdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.tvshow.BuildConfig
import com.demo.tvshow.R
import com.demo.tvshow.models.TVShowDTO
import com.demo.tvshow.models.TVShowInfoDTO
import com.demo.tvshow.ui.base.showAlert
import com.demo.tvshow.ui.theme.paddingLarge
import com.demo.tvshow.ui.theme.paddingMedium
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ShowDetails(serialId: Int, showDetailsViewModel: ShowDetailsViewModel = viewModel()) {

    val favorite by showDetailsViewModel.isFavorite.collectAsState()
    val similarShows by showDetailsViewModel.similarTVShows.collectAsState()
    var loading by remember {
        mutableStateOf(false)
    }
    val openAlert = remember {
        mutableStateOf(false)
    }

    var alertMessage by remember {
        mutableStateOf("")
    }

    var tvShowInfo by remember {
        mutableStateOf(TVShowInfoDTO(id = 0))
    }

    LaunchedEffect(Unit) {
        showDetailsViewModel.getTVShowInfo(serialId)
        showDetailsViewModel.tvShowInfo.collect {
            when (it) {
                ShowDetailUiState.Empty -> {

                }

                is ShowDetailUiState.Error -> {
                    loading = false
                    openAlert.value = true
                    alertMessage = it.exception ?: ""
                }

                ShowDetailUiState.Loading -> {
                    loading = true
                }

                is ShowDetailUiState.Success -> {
                    loading = false
                    tvShowInfo = it.tvShowInfo
                }
            }
        }
    }
    Column(
        modifier = Modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Box() {
                GlideImage(
                    imageModel = { BuildConfig.IMAGE_URL + tvShowInfo.posterPath }, // loading a network image using an URL.
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillBounds,
                        alignment = Alignment.Center
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .height(256.dp)
                )


                Surface(
                    color = Color.Black.copy(alpha = 0.25f),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    IconButton(onClick = {
                        showDetailsViewModel.markFavorite(tvShowInfo.id, !favorite)
                    }, content = {
                        if (favorite) {
                            Icon(
                                Icons.Outlined.Favorite, "favorite", tint = Color.White
                            )
                        } else {
                            Icon(
                                Icons.Outlined.FavoriteBorder, "Unfavorite", tint = Color.White
                            )
                        }

                    })

                }


            }
        }
        Column(Modifier.padding(paddingLarge)) {
            Text(
                stringResource(id = R.string.title_description),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                tvShowInfo.overview ?: "",
                style = MaterialTheme.typography.labelSmall
            )

            Box(Modifier.height(paddingMedium))
            Text(
                stringResource(id = R.string.title_seasons, tvShowInfo.seasons.size),
                style = MaterialTheme.typography.labelLarge
            )

            LazyRow() {
                items(tvShowInfo.seasons.size) { index ->
                    val item = tvShowInfo.seasons[index]
                    Card(
                        modifier = Modifier
                            .padding(paddingMedium)
                    ) {
                        Box(
                            Modifier
                                .size(128.dp)
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
                                Text(
                                    stringResource(
                                        id = R.string.label_no_of_episodes,
                                        item.episodeCount ?: 0
                                    ),
                                    style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
                                    maxLines = 1,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(paddingLarge),
                                )

                            }
                        }

                    }
                }
            }
            Box(Modifier.height(paddingMedium))
            Text(
                stringResource(id = R.string.title_similar_shows),
                style = MaterialTheme.typography.labelLarge
            )

            LazyRow() {
                items(similarShows.size) { index ->
                    val item = similarShows[index]
                    TVShowItem(item = item)
                }
            }
        }
        if (openAlert.value) {
            showAlert(
                alertDialog = openAlert, message =
                alertMessage
            )
        }
    }
}


@Composable
fun TVShowItem(item: TVShowDTO) {
    Card(
        modifier = Modifier
            .padding(paddingMedium)
    ) {
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
                }

            }


        }

    }
}