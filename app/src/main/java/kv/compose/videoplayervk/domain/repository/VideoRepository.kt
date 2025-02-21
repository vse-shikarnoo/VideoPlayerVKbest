package kv.compose.videoplayervk.domain.repository

import NetworkResult
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kv.compose.videoplayervk.domain.model.Video

interface VideoRepository {
    fun getVideosFlow(): Flow<List<Video>>
    suspend fun refreshVideos(): NetworkResult<Unit>
    suspend fun getVideo(id: String): Video?
    fun getPagedVideos(): Flow<PagingData<Video>>
} 