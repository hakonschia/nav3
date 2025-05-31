package no.nrk.radio.nav3.scenes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.Scene
import kotlinx.coroutines.launch
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.noAnimationMetadata

@Suppress("FunctionName")
fun <T : Any> BottomSheetNavEntry(key: T, content: @Composable (T) -> Unit): NavEntry<T> {
    return NavEntry(
        key = key,
        content = content,
        metadata = BottomSheetScene.bottomSheetSceneMetadata() +
                // The current scene being shown (the current navigation) and the bottom sheet scene should not animate between themselves
                // as it just causes a flash
                noAnimationMetadata
    )
}

data class BottomSheetScene<T : NavigationModel>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val mainEntry: NavEntry<T>,
    val bottomSheetEntry: NavEntry<T>,
    val onBack: () -> Unit
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(mainEntry, bottomSheetEntry)

    @OptIn(ExperimentalMaterial3Api::class)
    override val content: @Composable (() -> Unit) = {
        val bottomSheetState = rememberModalBottomSheetState()
        val coroutineScope = rememberCoroutineScope()

        mainEntry.content.invoke(mainEntry.key)

        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    onBack()
                }
            }
        ) {
            bottomSheetEntry.content.invoke(bottomSheetEntry.key)
        }
    }

    companion object {
        internal const val BOTTOM_SHEET_ENTRY_KEY = "BottomSheetEntryKey"

        fun bottomSheetSceneMetadata() = mapOf(BOTTOM_SHEET_ENTRY_KEY to true)
    }
}
