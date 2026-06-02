package eliza.projects.dogs.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiNature
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Screen {
    val route: String
    val title: String
    val icon: ImageVector

    data object RandomDog : Screen {
        override val route = "random_breed"
        override val title = "Breed"
        override val icon = Icons.Default.EmojiNature
    }

    data object Catalog : Screen {
        override val route = "catalog"
        override val title = "Breeds"
        override val icon = Icons.Default.Pets
    }

    data object RandomFact : Screen {
        override val route = "random_fact"
        override val title = "Fact"
        override val icon = Icons.Default.EmojiObjects
    }

    companion object {
        val all = listOf(RandomDog, Catalog, RandomFact)
    }
}