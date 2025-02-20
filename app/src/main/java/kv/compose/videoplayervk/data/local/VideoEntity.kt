package kv.compose.videoplayervk.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kv.compose.videoplayervk.domain.model.Video

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: Long,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toVideo(): Video {
        return Video(
            id = id,
            title = title,
            thumbnailUrl = thumbnailUrl,
            videoUrl = videoUrl,
            duration = duration
        )
    }
} 