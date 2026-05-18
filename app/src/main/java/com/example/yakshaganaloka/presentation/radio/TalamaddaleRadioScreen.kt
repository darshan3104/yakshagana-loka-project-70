package com.example.yakshaganaloka.presentation.radio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.yakshaganaloka.data.model.AudioClip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalamaddaleRadioScreen(
    onNavigateBack: () -> Unit,
    viewModel: RadioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentPlaying by viewModel.currentPlaying.collectAsState()
    val explanation by viewModel.explanation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Talamaddale Radio") },
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
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is RadioUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is RadioUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.clips) { clip ->
                            AudioClipCard(
                                clip = clip, 
                                onPlay = { viewModel.playClip(clip) },
                                onExplain = { viewModel.explainDialogue(clip) }
                            )
                        }
                        item {
                            explanation?.let {
                                Card(
                                    modifier = Modifier.padding(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                                ) {
                                    Text(text = it, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
                is RadioUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}")
                    }
                }
            }
        }

        // Mini Player
        currentPlaying?.let { clip ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                MiniPlayer(clip = clip)
            }
        }
    }
}

@Composable
fun AudioClipCard(clip: AudioClip, onPlay: () -> Unit, onExplain: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onPlay
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = clip.thumbnail_url,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = clip.title_english, fontWeight = FontWeight.Bold)
                    Text(text = clip.artist_name, style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = onPlay) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }
            }
            TextButton(onClick = onExplain, modifier = Modifier.align(Alignment.End)) {
                Text("AI Explain Dialogue")
            }
        }
    }
}

@Composable
fun MiniPlayer(clip: AudioClip) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = clip.thumbnail_url,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = clip.title_english, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                Text(text = "Playing now", style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = { /* TODO: Pause */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play/Pause")
            }
        }
    }
}
