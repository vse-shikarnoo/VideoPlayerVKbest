package kv.compose.videoplayervk.di


import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kv.compose.videoplayervk.data.api.VideoApi
import kv.compose.videoplayervk.data.local.VideoDao
import kv.compose.videoplayervk.data.local.VideoDatabase
import kv.compose.videoplayervk.data.repository.VideoRepositoryImpl
import kv.compose.videoplayervk.domain.repository.VideoRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideVideoApi(): VideoApi {

        return Retrofit.Builder()
            .baseUrl(VideoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build())
            .build()
            .create(VideoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoDatabase(
        @ApplicationContext context: Context
    ): VideoDatabase {
        return Room.databaseBuilder(
            context,
            VideoDatabase::class.java,
            "videos.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(db: VideoDatabase): VideoDao {
        return db.videoDao()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(
        api: VideoApi,
        dao: VideoDao
    ): VideoRepository {
        return VideoRepositoryImpl(api, dao)
    }
}