package kv.compose.videoplayervk.data.repository

import NetworkResult
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
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kv.compose.videoplayervk.data.paging.VideosPagingSource
import kv.compose.videoplayervk.data.paging.VideoRemoteMediator
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map

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

    override suspend fun refreshVideos(): NetworkResult<Unit> {
        return try {
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
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("VideoRepository", "Error refreshing videos", e)
            when (e) {
                is java.net.UnknownHostException,
                is java.net.SocketTimeoutException,
                is java.io.IOException -> NetworkResult.NetworkError()
                else -> NetworkResult.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    override suspend fun getVideo(id: String): Video? {
        return dao.getVideoById(id)?.toVideo()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedVideos(): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            remoteMediator = VideoRemoteMediator(api, dao),
            pagingSourceFactory = { dao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Video(
                    id = entity.id,
                    title = entity.title,
                    thumbnailUrl = entity.thumbnailUrl,
                    videoUrl = entity.videoUrl,
                    duration = entity.duration
                )
            }
        }
    }
} 