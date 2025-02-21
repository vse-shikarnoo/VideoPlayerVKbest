import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class VideosViewModelTest {
    private lateinit var repository: VideoRepository
    private lateinit var viewModel: VideosViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = VideosViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        assertEquals(
            VideosUiState(isLoading = false, error = null),
            viewModel.uiState.value
        )
    }

    @Test
    fun `refresh shows loading and updates state on success`() = runTest {
        // Given
        whenever(repository.refreshVideos()).thenReturn(NetworkResult.Success(Unit))

        // When & Then
        viewModel.uiState.test {
            viewModel.refresh()
            assertEquals(VideosUiState(isLoading = true), awaitItem())
            assertEquals(VideosUiState(isLoading = false), awaitItem())
        }
    }

    @Test
    fun `refresh shows error on network failure`() = runTest {
        // Given
        whenever(repository.refreshVideos()).thenReturn(NetworkResult.NetworkError())

        // When & Then
        viewModel.uiState.test {
            viewModel.refresh()
            assertEquals(VideosUiState(isLoading = true), awaitItem())
            assertEquals(
                VideosUiState(
                    isLoading = false,
                    error = "Отсутствует подключение к интернету"
                ),
                awaitItem()
            )
        }
    }
} 