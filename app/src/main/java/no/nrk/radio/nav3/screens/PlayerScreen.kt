package no.nrk.radio.nav3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.navigation.PlayerNavigationModel

@Composable
fun PlayerScreen(
    navigationModel: PlayerNavigationModel,
    onNavigate: (NavigationModel) -> Unit,
    onNavigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Text("Player :))) ${navigationModel.title}")

        Button(
            onClick = {
                onNavigate(PlayerNavigationModel("double player???"))
            }
        ) {
            Text("Go to new player???")
        }


        Button(
            onClick = onNavigateUp
        ) {
            Text("Go back")
        }
    }
}
