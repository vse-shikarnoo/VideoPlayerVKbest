package kv.compose.videoplayervk.presentation.videos

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
class VideosViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(VideosState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getVideosFlow().collect { videos ->
                _state.update { it.copy(
                    videos = videos,
                    isLoading = false
                ) }
            }
        }
        refreshVideos()
    }

    fun refreshVideos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.refreshVideos()
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message ?: "Неизвестная ошибка",
                    isLoading = false
                ) }
            }
        }
    }
}

data class VideosState(
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 