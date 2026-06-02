package eliza.projects.dogs.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import eliza.projects.dogs.data.model.BreedItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: CatalogViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        stringResource(R.string.TopNameCatalog),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },


        ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ← Используем отступы Scaffold правильно
        ) {
            // Search field
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                placeholder = { Text("Search breed...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
                singleLine = true
            )

            // States: loading / error / content
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    BreedList(
                        breeds = uiState.filteredBreeds,
                        searchQuery = uiState.searchQuery,
                        expandedItemId = uiState.expandedItemId,
                        onToggleExpand = viewModel::toggleExpand
                    )
                }
            }
        }
    }
}

@Composable
private fun BreedList(
    breeds: List<BreedItem>,
    searchQuery: String,
    expandedItemId: String?,
    onToggleExpand: (String) -> Unit
) {
    if (breeds.isEmpty() && searchQuery.isNotBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Nothing found",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(breeds, key = { it.id }) { breed ->
            BreedCard(
                breed = breed,
                isExpanded = expandedItemId == breed.id,
                onToggleExpand = { onToggleExpand(breed.id) }
            )
        }
    }
}

@Composable
private fun BreedCard(
    breed: BreedItem,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val attrs = breed.attributes

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleExpand),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Card header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = attrs.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }

            // Hypoallergenic badge
            if (attrs.hypoallergenic) {
                Text(
                    text = "🌿 Hypoallergenic",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Expandable details
            if (isExpanded) {
                BreedDetails(attrs)
            }
        }
    }
}

@Composable
private fun BreedDetails(attrs: BreedAttributes) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        attrs.description?.takeIf { it.isNotBlank() }?.let {
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        attrs.life?.let {
            Text("🧬 Life span: ${it.min}–${it.max} years")
        }
        attrs.maleWeight?.let {
            Text("♂️ Male weight: ${it.min}–${it.max} kg")
        }
        attrs.femaleWeight?.let {
            Text("️♀️ Female weight: ${it.min}–${it.max} kg")
        }
    }
}