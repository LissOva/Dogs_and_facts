package eliza.projects.dogs.ui.screens.random_fact

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eliza.projects.dogs.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomFactScreen(
    viewModel: RandomFactViewModel = viewModel()
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
                        text = stringResource(R.string.TopNameRandomFact), // Не забудьте добавить в strings.xml
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
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
                is RandomFactUiState.Idle -> {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Fact icon",
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Press the button to learn\na random dog fact!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    FactButton(onClick = { viewModel.fetchRandomFact() })
                }

                is RandomFactUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Discovering wisdom...", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                is RandomFactUiState.Success -> {
                    FactCard(fact = state.fact)
                    Spacer(modifier = Modifier.height(24.dp))
                    FactButton(onClick = { viewModel.fetchRandomFact() })
                }

                is RandomFactUiState.Error -> {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error icon",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Oops! ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FactButton(onClick = { viewModel.fetchRandomFact() })
                }
            }
        }
    }
}

@Composable
private fun FactButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(0.8f),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Get Random Fact", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun FactCard(fact: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "💡 ",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Did you know?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = fact,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
            )
        }
    }
}