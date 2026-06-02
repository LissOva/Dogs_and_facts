package eliza.projects.dogs.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eliza.projects.dogs.ui.screens.catalog.CatalogScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import eliza.projects.dogs.ui.screens.random_dog.RandomDogScreen
import eliza.projects.dogs.ui.screens.random_fact.RandomFactScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalog.route,
        modifier = modifier
    ) {
        composable(Screen.RandomDog.route) { RandomDogScreen() }
        composable(Screen.Catalog.route) { CatalogScreen(viewModel = viewModel()) }
        composable(Screen.RandomFact.route) { RandomFactScreen() }
    }
}