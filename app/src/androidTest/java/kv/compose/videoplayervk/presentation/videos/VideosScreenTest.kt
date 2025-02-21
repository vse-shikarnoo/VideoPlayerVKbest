package kv.compose.videoplayervk.presentation.videos

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf
import kv.compose.videoplayervk.domain.model.Video
import kv.compose.videoplayervk.presentation.videos.VideosScreen
import kv.compose.videoplayervk.presentation.videos.VideosUiState
import org.junit.Rule
import org.junit.Test

class VideosScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun videosListDisplayed() {
        // Given
        val videos = listOf(
            Video(
                id = "1",
                title = "Test Video",
                thumbnailUrl = "thumbnail.jpg",
                videoUrl = "video.mp4",
                duration = 120
            )
        )

        // When
        composeTestRule.setContent {
            VideosScreen(
                videosFlow = flowOf(PagingData.from(videos)),
                uiState = VideosUiState(),
                onVideoClick = {},
                onRefresh = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("Test Video")
            .assertIsDisplayed()
    }

    @Test
    fun errorStateDisplayed() {
        // Given
        val errorMessage = "Ошибка загрузки"

        // When
        composeTestRule.setContent {
            VideosScreen(
                videosFlow = flowOf(PagingData.empty()),
                uiState = VideosUiState(error = errorMessage),
                onVideoClick = {},
                onRefresh = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }
} 