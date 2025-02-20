package kv.compose.videoplayervk.domain.repository

import kotlinx.coroutines.flow.Flow
import kv.compose.videoplayervk.domain.model.Video

interface VideoRepository {
    fun getVideosFlow(): Flow<List<Video>>
    suspend fun refreshVideos()
    suspend fun getVideo(id: String): Video?
} 