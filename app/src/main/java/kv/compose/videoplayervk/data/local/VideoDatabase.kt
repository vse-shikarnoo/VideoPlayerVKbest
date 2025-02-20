package kv.compose.videoplayervk.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VideoEntity::class],
    version = 1
)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}

