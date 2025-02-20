package kv.compose.videoplayervk.domain.model

data class Video(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: Long
) {
    val formattedDuration: String
        get() {
            val minutes = duration / 60
            val seconds = duration % 60
            return String.format("%d:%02d", minutes, seconds)
        }
} 