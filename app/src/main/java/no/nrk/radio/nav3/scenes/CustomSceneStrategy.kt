package no.nrk.radio.nav3.scenes

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.Scene
import androidx.navigation3.ui.SceneStrategy
import no.nrk.radio.nav3.navigation.NavigationModel

class CustomSceneStrategy<T : NavigationModel> : SceneStrategy<T> {
    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit
    ): Scene<T>? {
        if (entries.size < 2) {
            return null
        }

        // Not sure if we should rely on metadata being completely empty for main entries
        // It's probably safer to force usage of something like DefaultNavEntry() that sets metadata that signals it is a main entry
        // and not a bottom sheet or dialog
        // Getting the main entry like this supports going from bottom sheets to dialogs, nested bottom sheets, and nested dialogs
        // Nested dialogs is definitely interesting, but going between the two is probably not
        val mainEntry = entries.last { it.metadata.isEmpty() }
        val lastEntry = entries.last()

        return if (lastEntry.metadata.containsKey(DialogScene.DIALOG_ENTRY_KEY)) {
            DialogScene(
                key = mainEntry.key to lastEntry.key,
                // I don't think we need to let this scene know about previous entries as it seems to just be for predictive back
                // which dialogs don't have
                previousEntries = emptyList(),
                mainEntry = mainEntry,
                dialogEntry = lastEntry,
                onBack = {
                    onBack(1)
                }
            )
        } else if (lastEntry.metadata.containsKey(BottomSheetScene.BOTTOM_SHEET_ENTRY_KEY)) {
            BottomSheetScene(
                key = mainEntry.key to lastEntry.key,
                // I don't think we need to let this scene know about previous entries as it seems to just be for predictive back
                // which the bottom sheet handles itself
                previousEntries = emptyList(),
                mainEntry = mainEntry,
                bottomSheetEntry = lastEntry,
                onBack = {
                    onBack(1)
                }
            )
        } else {
            null
        }
    }
}
