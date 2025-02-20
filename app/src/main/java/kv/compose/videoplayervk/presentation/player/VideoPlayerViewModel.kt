package kv.compose.videoplayervk.presentation.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kv.compose.videoplayervk.domain.model.Video
import kv.compose.videoplayervk.domain.repository.VideoRepository
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val repository: VideoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val videoId: String = checkNotNull(savedStateHandle["videoId"])
    private val _state = MutableStateFlow(VideoPlayerState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val video = repository.getVideo(videoId)
            _state.update { it.copy(
                video = video,
                isLoading = false
            ) }
        }
    }
}

data class VideoPlayerState(
    val video: Video? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isPlaying: Boolean = false
) 