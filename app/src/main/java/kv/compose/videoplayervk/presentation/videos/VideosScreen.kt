package kv.compose.videoplayervk.presentation.videos

import kv.compose.videoplayervk.presentation.components.ErrorView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kv.compose.videoplayervk.presentation.videos.components.VideoItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VideosScreen(
    viewModel: VideosViewModel = hiltViewModel(),
    onVideoClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = viewModel::refreshVideos
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn {
            items(state.videos) { video ->
                VideoItem(
                    video = video,
                    onClick = { onVideoClick(video.id) }
                )
            }
        }

        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Error handling
        state.error?.let { error ->
            ErrorView(
                message = error,
                onRetry = viewModel::refreshVideos,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))

            )
        }

        // Loading indicator
        if (state.isLoading && state.videos.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
} 