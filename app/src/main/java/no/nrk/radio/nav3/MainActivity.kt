package no.nrk.radio.nav3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import no.nrk.radio.nav3.navigation.BottomSheetNavigationModel
import no.nrk.radio.nav3.navigation.DialogNavigationModel
import no.nrk.radio.nav3.navigation.FrontPageNavigationModel
import no.nrk.radio.nav3.navigation.InitializingNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.navigation.PlayerNavigationModel
import no.nrk.radio.nav3.navigation.SeriesNavigationModel
import no.nrk.radio.nav3.scenes.BottomSheetNavEntry
import no.nrk.radio.nav3.scenes.CustomSceneStrategy
import no.nrk.radio.nav3.scenes.DialogNavEntry
import no.nrk.radio.nav3.screens.BottomSheetScreen
import no.nrk.radio.nav3.screens.DialogScreen
import no.nrk.radio.nav3.screens.FrontPageScreen
import no.nrk.radio.nav3.screens.PlayerScreen
import no.nrk.radio.nav3.screens.SeriesScreen
import no.nrk.radio.nav3.ui.theme.Nav3Theme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Nav3Theme {
                val deepLinkViewModel = koinViewModel<DeepLinkViewModel>()

                val snackbarHostState = remember { SnackbarHostState() }
                val navigationController = rememberNavigationController<NavigationModel>(
                    snackbarHostState = snackbarHostState,
                    startDestinations = listOf(InitializingNavigationModel)
                )

                LaunchedEffect(Unit) {
                    deepLinkViewModel.parseDeepLink(intent.data)

                    deepLinkViewModel.navigations.filterNotNull().take(1).collect { navigations ->
                        navigationController.clearBackStackAndNavigate(navigations)
                        deepLinkViewModel.setHandled()
                    }
                }

                Scaffold(
                    snackbarHost = {
                        snackbarHostState.currentSnackbarData?.let { snackbarData ->
                            Snackbar(
                                snackbarData = snackbarData
                            )
                        }
                    }
                ) { contentPadding ->
                    NavDisplay(
                        backStack = navigationController.backStack,
                        entryDecorators = listOf(
                            rememberSceneSetupNavEntryDecorator(),
                            rememberSavedStateNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        sceneStrategy = CustomSceneStrategy(),
                        modifier = Modifier
                            .padding(contentPadding)
                    ) { navigationModel ->
                        when (navigationModel) {
                            is InitializingNavigationModel -> NavEntry(navigationModel) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Text("Parsing deep link :))")

                                    CircularProgressIndicator()
                                }
                            }

                            is FrontPageNavigationModel -> NavEntry(navigationModel) {
                                FrontPageScreen(
                                    onNavigate = navigationController::addToBackStack
                                )
                            }

                            is SeriesNavigationModel -> NavEntry(navigationModel) {
                                SeriesScreen(
                                    navigationModel = navigationModel,
                                    onNavigate = navigationController::addToBackStack,
                                    onNavigateUp = navigationController::popBackStack
                                )
                            }

                            is PlayerNavigationModel -> NavEntry(
                                key = navigationModel,
                                metadata = NavDisplay.predictivePopTransitionSpec {
                                    fadeIn() togetherWith scaleOut(
                                        targetScale = 0.9f,
                                        transformOrigin = TransformOrigin(1f, 0.5f)
                                    ) + fadeOut(
                                        animationSpec = tween(delayMillis = 100)
                                    )
                                }
                            ) {
                                PlayerScreen(
                                    navigationModel = navigationModel,
                                    onNavigate = navigationController::addToBackStack,
                                    onNavigateUp = navigationController::popBackStack
                                )
                            }

                            is BottomSheetNavigationModel -> BottomSheetNavEntry(navigationModel) {
                                BottomSheetScreen(
                                    navigationModel = navigationModel,
                                    onNavigate = navigationController::addToBackStack
                                )
                            }

                            is DialogNavigationModel -> DialogNavEntry(navigationModel) {
                                DialogScreen(
                                    navigationModel = navigationModel,
                                    onNavigate = navigationController::addToBackStack
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}
