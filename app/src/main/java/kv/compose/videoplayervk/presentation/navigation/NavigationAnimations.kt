import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationAnimations {
    val enterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))

    val exitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(300))

    val popEnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))

    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(300))
} 