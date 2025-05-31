package no.nrk.radio.nav3.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.nrk.radio.nav3.navigation.BottomSheetNavigationModel
import no.nrk.radio.nav3.navigation.DialogNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel

@Composable
fun DialogScreen(
    navigationModel: DialogNavigationModel,
    onNavigate: (NavigationModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(0.4f)
    ) {
        Text(navigationModel.dialogTitle)

        Button(
            onClick = {
                onNavigate(BottomSheetNavigationModel("bottom sheet from dialog???"))
            }
        ) {
            Text("New dialog sheet??")
        }
    }
}
