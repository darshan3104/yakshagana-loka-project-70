package com.example.yakshaganaloka.presentation.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yakshaganaloka.ui.components.EventCard
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceMapScreen(
    onNavigateBack: () -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val karnataka = LatLng(14.5, 75.7)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(karnataka, 7f)
    }

    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Map") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
               Icon(Icons.Default.Menu, contentDescription = "List")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false) // Permission needed
            ) {
                if (uiState is MapUiState.Success) {
                    val events = (uiState as MapUiState.Success).events
                    events.forEach { event ->
                        Marker(
                            state = MarkerState(position = LatLng(event.latitude, event.longitude)),
                            title = event.mela_name,
                            snippet = "${event.venue_name} - ${event.main_story}"
                        )
                    }
                }
            }

            if (uiState is MapUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                if (uiState is MapUiState.Success) {
                    val events = (uiState as MapUiState.Success).events
                    if (events.isEmpty()) {
                        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No shows tonight")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            items(events) { event ->
                                EventCard(event = event, onClick = {
                                    showSheet = false
                                    cameraPositionState.move(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(LatLng(event.latitude, event.longitude), 12f))
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}
