package kv.compose.videoplayervk.presentation.navigation

import NavigationAnimations
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kv.compose.videoplayervk.presentation.player.VideoPlayerScreen
import kv.compose.videoplayervk.presentation.videos.VideosScreen

sealed class Screen(val route: String) {
    object Videos : Screen("videos")
    object Player : Screen("player/{videoId}") {
        fun createRoute(videoId: String) = "player/$videoId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Videos.route
    ) {
        composable(Screen.Videos.route,
            exitTransition = { NavigationAnimations.exitTransition },
            popEnterTransition = { NavigationAnimations.popEnterTransition }) {
            VideosScreen(
                onVideoClick = { videoId ->
                    navController.navigate(Screen.Player.createRoute(videoId))
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            ),
            enterTransition = { NavigationAnimations.enterTransition },
            popExitTransition = { NavigationAnimations.popExitTransition }
        ) {
            VideoPlayerScreen()
        }
    }
} 