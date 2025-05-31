package no.nrk.radio.nav3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.nrk.radio.nav3.navigation.DialogNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.navigation.SeriesNavigationModel

@Composable
fun FrontPageScreen(
    onNavigate: (NavigationModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .padding(top = 24.dp, start = 48.dp)
    ) {
        Text("Frontpage")

        Spacer(Modifier.size(148.dp))

        Button(
            onClick = {
                onNavigate(SeriesNavigationModel("Hest er best"))
            }
        ) {
            Text("Go to series")
        }

        Button(
            onClick = {
                onNavigate(DialogNavigationModel("Hest er best"))
            }
        ) {
            Text("Go to dialog")
        }
    }
}
