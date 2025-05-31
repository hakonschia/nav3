package no.nrk.radio.nav3.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.nrk.radio.nav3.navigation.BottomSheetNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel

@Composable
fun BottomSheetScreen(
    navigationModel: BottomSheetNavigationModel,
    onNavigate: (NavigationModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(0.4f)
    ) {
        Text(navigationModel.title)

        Button(
            onClick = {
                onNavigate(BottomSheetNavigationModel("nested bottom sheet???"))
            }
        ) {
            Text("New bottom sheet??")
        }
    }
}
