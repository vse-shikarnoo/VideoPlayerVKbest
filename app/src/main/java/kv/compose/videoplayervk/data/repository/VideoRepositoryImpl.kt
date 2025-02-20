package kv.compose.videoplayervk.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kv.compose.videoplayervk.data.api.VideoApi
import kv.compose.videoplayervk.data.local.VideoDao
import kv.compose.videoplayervk.data.local.VideoEntity
import kv.compose.videoplayervk.domain.model.Video
import kv.compose.videoplayervk.domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: VideoApi,
    private val dao: VideoDao
) : VideoRepository {

    override fun getVideosFlow(): Flow<List<Video>> = dao.getVideos()
        .map { entities -> 
            entities.map { it.toVideo() }
        }
        .catch { e ->
            emit(emptyList())
            Log.e("VideoRepository", "Error getting videos", e)
        }

    override suspend fun refreshVideos() {
        try {
            val response = api.getVideos().data
            val videos = response.map { dto ->
                VideoEntity(
                    id = dto.id,
                    title = dto.title,
                    thumbnailUrl = dto.thumbnailUrl,
                    videoUrl = dto.videoUrl,
                    duration = dto.duration.toLong(),
                    timestamp = System.currentTimeMillis()
                )
            }
            dao.clearVideos()
            dao.insertVideos(videos)
        } catch (e: Exception) {
            Log.e("VideoRepository", "Error refreshing videos", e)
            throw e
        }
    }

    override suspend fun getVideo(id: String): Video? {
        return dao.getVideoById(id)?.toVideo()
    }
} 