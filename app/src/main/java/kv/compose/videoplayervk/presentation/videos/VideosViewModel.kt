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
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData
import androidx.paging.cachedIn

@HiltViewModel
class VideosViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideosUiState())
    val uiState = _uiState.asStateFlow()

    val videosFlow: Flow<PagingData<Video>> = repository.getPagedVideos()
        .cachedIn(viewModelScope)

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = repository.refreshVideos()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                is NetworkResult.NetworkError -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = "Отсутствует подключение к интернету"
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}

data class VideosUiState(
    val isLoading: Boolean = false,
    val error: String? = null
) 