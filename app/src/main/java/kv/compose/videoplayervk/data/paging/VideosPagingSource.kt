package kv.compose.videoplayervk.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kv.compose.videoplayervk.data.api.VideoApi
import kv.compose.videoplayervk.domain.model.Video
import retrofit2.HttpException
import java.io.IOException

class VideosPagingSource(
    private val api: VideoApi
) : PagingSource<Int, Video>() {

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val page = params.key ?: 1
            val response = api.getVideos(page = page)

            val videos = response.data.map { dto ->
                Video(
                    id = dto.id,
                    title = dto.title,
                    thumbnailUrl = dto.thumbnailUrl,
                    videoUrl = dto.videoUrl,
                    duration = dto.duration.toLong()
                )
            }

            LoadResult.Page(
                data = videos,
                prevKey = null, // Только вперед
                nextKey = if (videos.isEmpty() || page >= 4) null else page + 1 // Pexels ограничивает до 4 страниц
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}