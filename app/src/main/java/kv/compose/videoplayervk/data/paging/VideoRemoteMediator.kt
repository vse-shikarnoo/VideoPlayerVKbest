package kv.compose.videoplayervk.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.LoadType
import kv.compose.videoplayervk.data.api.VideoApi
import kv.compose.videoplayervk.data.local.VideoDao
import kv.compose.videoplayervk.data.local.VideoEntity
import kv.compose.videoplayervk.domain.model.Video
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class VideoRemoteMediator(
    private val api: VideoApi,
    private val dao: VideoDao
) : RemoteMediator<Int, VideoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VideoEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        // Получаем следующую страницу
                        (state.pages.size + 1).also {
                            // Если достигли лимита API Pexels (максимум 80 видео, то есть 4 страницы по 20)
                            if (it > 4) {
                                return MediatorResult.Success(endOfPaginationReached = true)
                            }
                        }
                    }
                }
            }

            val response = api.getVideos(page = page, pageSize = state.config.pageSize)

            if (loadType == LoadType.REFRESH) {
                dao.clearVideos()
            }

            val videos = response.data.map { dto ->
                VideoEntity(
                    id = dto.id,
                    title = dto.title,
                    thumbnailUrl = dto.thumbnailUrl,
                    videoUrl = dto.videoUrl,
                    duration = dto.duration.toLong(),
                    timestamp = System.currentTimeMillis()
                )
            }

            dao.insertVideos(videos)

            // Проверяем, достигнут ли конец списка
            MediatorResult.Success(
                endOfPaginationReached = response.data.isEmpty() || response.data.size < state.config.pageSize
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}