package no.nrk.radio.nav3

import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nrk.radio.nav3.navigation.SingleNavigationEntry

@Composable
inline fun <reified T : NavKey> rememberNavigationController(
    snackbarHostState: SnackbarHostState,
    startDestinations: List<T>
): NavigationController<T> {
    val backStack = rememberNavBackStack(*startDestinations.toTypedArray())
    val coroutineScope = rememberCoroutineScope()

    return remember(snackbarHostState, backStack, coroutineScope) {
        // NavigationController should expose the backStack as T, not something that extends NavKey, this makes NavDisplay's
        // entryProvider lambda provide a value that is T, not NavKey (which is annoying)
        // https://issuetracker.google.com/issues/420443609
        @Suppress("UNCHECKED_CAST")
        NavigationController(
            backStack = backStack as SnapshotStateList<T>,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        )
    }
}

class NavigationController<T : NavKey>(
    val backStack: SnapshotStateList<T>,
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun addToBackStack(navigationModel: T) {
        // navigation is marked as single navigation, don't add it if the back stack already contains a navigation of the same type
        if (navigationModel is SingleNavigationEntry && backStack.any { it.javaClass == navigationModel.javaClass} ) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Blocked $navigationModel", duration = SnackbarDuration.Short)
            }
            return
        }

        backStack.add(navigationModel)
    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    fun clearBackStackAndNavigate(navigations: List<T>) {
        backStack.clear()
        backStack.addAll(navigations)
    }
}
