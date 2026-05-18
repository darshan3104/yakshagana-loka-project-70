package com.example.yakshaganaloka.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yakshaganaloka.ui.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToArtistDirectory: () -> Unit,
    onNavigateToRadio: () -> Unit,
    onNavigateToMelaSchedule: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Yakshagana Loka", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = onNavigateToArtistDirectory) {
                        Icon(Icons.Default.Search, contentDescription = "Artists")
                    }
                    IconButton(onClick = onNavigateToMap) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Map")
                    }
                    IconButton(onClick = onNavigateToMelaSchedule) {
                        Icon(Icons.Default.List, contentDescription = "Schedule")
                    }
                    IconButton(onClick = onNavigateToRadio) {
                        Icon(Icons.Default.Info, contentDescription = "Radio")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HomeHeader()

            Text(
                text = "Tonight's Shows",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is HomeUiState.Success -> {
                    if (state.events.isEmpty()) {
                        EmptyShowsView()
                    } else {
                        LazyColumn {
                            items(state.events) { event ->
                                EventCard(event = event, onClick = { /* TODO: Navigate to detail */ })
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {
                    ErrorView(message = state.message, onRetry = { viewModel.loadTonightShows() })
                }
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Digital Stage of the Coast",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "ಯಕ್ಷಗಾನ ಲೋಕ",
                color = MaterialTheme.colorScheme.primaryContainer,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmptyShowsView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No shows scheduled for tonight.", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error: $message", color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
