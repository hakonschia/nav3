package no.nrk.radio.nav3.scenes

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.Scene
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.noAnimationMetadata

@Suppress("FunctionName")
fun <T : Any> DialogNavEntry(key: T, content: @Composable (T) -> Unit): NavEntry<T> {
    return NavEntry(
        key = key,
        content = content,
        metadata = DialogScene.dialogSceneMetadata() +
                // The current scene being shown (the current navigation) and the dialog scene should not animate between themselves
                // as it just causes a flash
                noAnimationMetadata
    )
}

data class DialogScene<T : NavigationModel>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val mainEntry: NavEntry<T>,
    val dialogEntry: NavEntry<T>,
    val onBack: () -> Unit
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(mainEntry, dialogEntry)

    override val content: @Composable (() -> Unit) = {
        mainEntry.content.invoke(mainEntry.key)

        Dialog(
            onDismissRequest = {
                onBack()
            }
        ) {
            dialogEntry.content.invoke(dialogEntry.key)
        }
    }

    companion object {
        internal const val DIALOG_ENTRY_KEY = "DialogEntryKey"

        fun dialogSceneMetadata() = mapOf(DIALOG_ENTRY_KEY to true)
    }
}
