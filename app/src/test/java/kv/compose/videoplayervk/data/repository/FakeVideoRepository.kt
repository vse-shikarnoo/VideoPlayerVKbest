import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kv.compose.videoplayervk.domain.model.Video
import kv.compose.videoplayervk.domain.repository.VideoRepository

class FakeVideoRepository : VideoRepository {
    private val videos = mutableListOf<Video>()
    private val videosFlow = MutableStateFlow<List<Video>>(emptyList())

    override fun getVideosFlow(): Flow<List<Video>> = videosFlow

    override fun getPagedVideos(): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { FakeVideosPagingSource(videos) }
        ).flow
    }

    override suspend fun refreshVideos(): NetworkResult<Unit> {
        return try {
            videos.clear()
            videos.addAll(generateFakeVideos())
            videosFlow.value = videos
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getVideo(id: String): Video? {
        return videos.find { it.id == id }
    }

    private fun generateFakeVideos(): List<Video> {
        return (1..10).map { index ->
            Video(
                id = index.toString(),
                title = "Video $index",
                thumbnailUrl = "thumbnail_$index.jpg",
                videoUrl = "video_$index.mp4",
                duration = index * 60L
            )
        }
    }
} 