package kv.compose.videoplayervk.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
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
) : PagingSource<Int, Video>() {

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val page = params.key ?: 1
            val response = api.getVideos(page = page, pageSize = params.loadSize)

            // Преобразуем DTO в сущности и сохраняем в базу данных
            val videoEntities = response.data.map { dto ->
                VideoEntity(
                    id = dto.id,
                    title = dto.title,
                    thumbnailUrl = dto.thumbnailUrl,
                    videoUrl = dto.videoUrl,
                    duration = dto.duration.toLong(),
                    timestamp = System.currentTimeMillis()
                )
            }
            
            // Сохраняем видео в базу данных
            dao.insertVideos(videoEntities)

            // Преобразуем в domain модели для UI
            val videos = videoEntities.map { entity ->
                Video(
                    id = entity.id,
                    title = entity.title,
                    thumbnailUrl = entity.thumbnailUrl,
                    videoUrl = entity.videoUrl,
                    duration = entity.duration
                )
            }

            LoadResult.Page(
                data = videos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.data.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
} 