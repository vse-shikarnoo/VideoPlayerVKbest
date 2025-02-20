package kv.compose.videoplayervk.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header
import com.google.gson.annotations.SerializedName

interface VideoApi {
    @GET("/v1/videos/popular")
    suspend fun getVideos(
        @Header("Authorization") apiKey: String = "zG1cIlgK2yBZyscMTGujiqAFuJmodQjxRtXzAQ8v4qMYAipq3uKTjS1R",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("min_duration") minDuration: Int = 0,
        @Query("max_duration") maxDuration: Int = 60
    ): PexelsResponse

    @GET("/v1/videos/search")
    suspend fun searchVideos(
        @Header("Authorization") apiKey: String = "zG1cIlgK2yBZyscMTGujiqAFuJmodQjxRtXzAQ8v4qMYAipq3uKTjS1R",
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelsResponse

    companion object {
        const val BASE_URL = "https://api.pexels.com/"
    }
}

data class PexelsResponse(
    @SerializedName("videos")
    val data: List<VideoDto>
)

data class VideoDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("image")
    val thumbnailUrl: String,
    @SerializedName("video_files")
    private val videoFiles: List<VideoFile>
) {
    val title: String
        get() = "Video by ${user.name}"
    
    val videoUrl: String
        get() = videoFiles.firstOrNull { it.quality == "hd" }?.link 
            ?: videoFiles.firstOrNull()?.link 
            ?: ""
}

data class VideoFile(
    @SerializedName("link")
    val link: String,
    @SerializedName("quality")
    val quality: String
)

data class User(
    @SerializedName("name")
    val name: String
)

private fun generateSignature(): String {
    // Реализация генерации подписи для API OK.ru
    // md5(request_params + secret_key)
    return ""
}