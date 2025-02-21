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

            when (val result = repository.refreshVideos()) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
                is NetworkResult.NetworkError -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Отсутствует подключение к интернету. Проверьте соединение и попробуйте снова."
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }

                else -> {}
            }
        }
    }
}

data class VideosState(
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 