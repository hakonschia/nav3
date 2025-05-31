package no.nrk.radio.nav3

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

private val noTransitionContentTransform: AnimatedContentTransitionScope<*>.() -> ContentTransform? = {
    EnterTransition.None togetherWith ExitTransition.None
}

val noAnimationMetadata = NavDisplay.transitionSpec(noTransitionContentTransform) +
        NavDisplay.popTransitionSpec(noTransitionContentTransform) +
        NavDisplay.predictivePopTransitionSpec(noTransitionContentTransform)
