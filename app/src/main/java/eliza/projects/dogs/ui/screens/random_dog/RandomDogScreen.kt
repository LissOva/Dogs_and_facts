package eliza.projects.dogs.ui.screens.random_dog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eliza.projects.dogs.R
import eliza.projects.dogs.data.model.BreedAttributes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomDogScreen(
    viewModel: RandomDogViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        // Убедитесь, что добавили эту строку в res/values/strings.xml
                        text = stringResource(R.string.TopNameRandomDog),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayMedium, // Как в CatalogScreen
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = uiState) {
                is RandomDogUiState.Idle -> {
                    Text(
                        text = "Press the button to discover a random breed!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    RandomButton(onClick = { viewModel.fetchRandomBreed() })
                }

                is RandomDogUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is RandomDogUiState.Success -> {
                    BreedDetailCard(state.breed.attributes)
                    Spacer(modifier = Modifier.height(24.dp))
                    RandomButton(onClick = { viewModel.fetchRandomBreed() })
                }

                is RandomDogUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RandomButton(onClick = { viewModel.fetchRandomBreed() })
                }
            }
        }
    }
}

@Composable
private fun RandomButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(0.8f),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Get Random Breed", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun BreedDetailCard(attrs: BreedAttributes) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "🐕" + attrs.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (attrs.hypoallergenic) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "🌿 Hypoallergenic",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            attrs.description?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(12.dp))
            }

            attrs.life?.let {
                InfoRow(icon = "🧬", label = "Life span", value = "${it.min}–${it.max} years")
            }
            attrs.maleWeight?.let {
                InfoRow(icon = "♂️", label = "Male weight", value = "${it.min}–${it.max} kg")
            }
            attrs.femaleWeight?.let {
                InfoRow(icon = "♀️", label = "Female weight", value = "${it.min}–${it.max} kg")
            }
        }
    }
}

@Composable
private fun InfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$icon $label", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}