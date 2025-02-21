import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kv.compose.videoplayervk.presentation.player.VideoPlayerScreen
import kv.compose.videoplayervk.presentation.player.VideoPlayerViewModel
import kv.compose.videoplayervk.presentation.videos.VideosScreen
import kv.compose.videoplayervk.presentation.videos.VideosViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Videos.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Videos.route,
            exitTransition = { NavigationAnimations.exitTransition },
            popEnterTransition = { NavigationAnimations.popEnterTransition }
        ) {
            val viewModel: VideosViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            VideosScreen(
                videosFlow = viewModel.videosFlow,
                uiState = uiState,
                onVideoClick = { videoId ->
                    navController.navigate(Screen.VideoPlayer.createRoute(videoId))
                },
                onRefresh = viewModel::refresh
            )
        }

        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            ),
            enterTransition = { NavigationAnimations.enterTransition },
            popExitTransition = { NavigationAnimations.popExitTransition }
        ) { backStackEntry ->

            VideoPlayerScreen(
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Videos : Screen("videos")
    object VideoPlayer : Screen("video/{videoId}") {
        fun createRoute(videoId: String) = "video/$videoId"
    }
} 