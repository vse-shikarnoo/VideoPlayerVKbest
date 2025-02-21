package kv.compose.videoplayervk.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kv.compose.videoplayervk.data.api.VideoApi
import kv.compose.videoplayervk.domain.model.Video
import retrofit2.HttpException
import java.io.IOException

class VideosPagingSource(
    private val api: VideoApi,
    private val pageSize: Int = 20
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
            val response = api.getVideos(page = page, pageSize = pageSize)

            LoadResult.Page(
                data = response.data.map { dto ->
                    Video(
                        id = dto.id,
                        title = dto.title,
                        thumbnailUrl = dto.thumbnailUrl,
                        videoUrl = dto.videoUrl,
                        duration = dto.duration.toLong()
                    )
                },
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