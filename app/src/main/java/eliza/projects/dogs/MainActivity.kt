package eliza.projects.dogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import eliza.projects.dogs.ui.navigation.AppNavHost
import eliza.projects.dogs.ui.navigation.BottomNavBar
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                ) {
                    AppShell()
                }
            }
        }
    }
}

@Composable
fun AppShell() {
    val navController = rememberNavController()
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(
                PaddingValues(
                    top = 0.dp,
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    bottom = innerPadding.calculateBottomPadding()
                )
            )
        )
    }
}