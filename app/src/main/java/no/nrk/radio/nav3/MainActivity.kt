package no.nrk.radio.nav3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import no.nrk.radio.nav3.navigation.BottomSheetNavigationModel
import no.nrk.radio.nav3.navigation.DialogNavigationModel
import no.nrk.radio.nav3.navigation.FrontPageNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.navigation.SeriesNavigationModel
import no.nrk.radio.nav3.scenes.BottomSheetNavEntry
import no.nrk.radio.nav3.scenes.CustomSceneStrategy
import no.nrk.radio.nav3.scenes.DialogNavEntry
import no.nrk.radio.nav3.screens.FrontPageScreen
import no.nrk.radio.nav3.screens.SeriesScreen
import no.nrk.radio.nav3.screens.SeriesViewModel
import no.nrk.radio.nav3.ui.theme.Nav3Theme
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@Composable
fun <T : NavKey> rememberTypedNavBackStack(vararg startDestinations: T): SnapshotStateList<T> {
    // Cast this so the lambda value sent to NavDisplay's entryProvider is T and not a NavKey, otherwise we have to cast it in there which is annoying
    // rememberNavBackStack() complains if we send in a vararg instead of a single value (startDestination), not sure why
    @Suppress("UNCHECKED_CAST")
    return rememberNavBackStack<T>(*startDestinations) as SnapshotStateList<T>
}

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
                    val backStack = rememberTypedNavBackStack<NavigationModel>(FrontPageNavigationModel)

                    NavDisplay(
                        backStack = backStack,
                        entryDecorators = listOf(
                            rememberSceneSetupNavEntryDecorator(),
                            rememberSavedStateNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        sceneStrategy = CustomSceneStrategy()
                    ) { key ->
                        when (key) {
                            is FrontPageNavigationModel -> NavEntry(key) {
                                FrontPageScreen(
                                    onNavigate = {
                                        backStack.add(it)
                                    }
                                )
                            }

                            is SeriesNavigationModel -> NavEntry(key) {
                                SeriesScreen(
                                    navigationModel = key,
                                    onNavigate = {
                                        backStack.add(it)
                                    },
                                    onNavigateUp = {
                                        backStack.removeAt(backStack.lastIndex)
                                    }
                                )
                            }

                            is BottomSheetNavigationModel -> BottomSheetNavEntry(key) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(0.4f)
                                ) {
                                    Text(key.title)

                                    Button(
                                        onClick = {
                                            backStack.add(BottomSheetNavigationModel("nested bottom sheet???"))
                                        }
                                    ) {
                                        Text("New bottom sheet??")
                                    }
                                }
                            }

                            is DialogNavigationModel -> DialogNavEntry(key) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(0.4f)
                                ) {
                                    Text(key.dialogTitle)

                                    Button(
                                        onClick = {
                                            backStack.add(BottomSheetNavigationModel("nested dialog???"))
                                        }
                                    ) {
                                        Text("New dialog sheet??")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
