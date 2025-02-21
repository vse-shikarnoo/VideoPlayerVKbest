import androidx.paging.PagingSource
import androidx.paging.PagingState
import kv.compose.videoplayervk.domain.model.Video

class FakeVideosPagingSource(
    private val videos: List<Video>
) : PagingSource<Int, Video>() {

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, videos.size)

        return if (startIndex < videos.size) {
            LoadResult.Page(
                data = videos.subList(startIndex, endIndex),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endIndex < videos.size) page + 1 else null
            )
        } else {
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
    }
} 