package no.nrk.radio.nav3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import no.nrk.radio.nav3.navigation.BottomSheetNavigationModel
import no.nrk.radio.nav3.navigation.DialogNavigationModel
import no.nrk.radio.nav3.navigation.FrontPageNavigationModel
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
import no.nrk.radio.nav3.screens.SeriesViewModel
import no.nrk.radio.nav3.ui.theme.Nav3Theme
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinApplication(
                application = {
                    modules(
                        listOf(
                            module {
                                viewModelOf(::SeriesViewModel)
                            }
                        )
                    )
                }
            ) {
                Nav3Theme {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val navigationController = rememberNavigationController<NavigationModel>(
                        snackbarHostState,
                        FrontPageNavigationModel
                    )

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

                                is PlayerNavigationModel -> NavEntry(navigationModel) {
                                    PlayerScreen(
                                        navigationModel = navigationModel,
                                        onNavigate = navigationController::addToBackStack
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
}
