package com.example.yakshaganaloka.presentation.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.yakshaganaloka.data.model.Vesha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistProfileScreen(
    artistId: String,
    onNavigateBack: () -> Unit,
    viewModel: ArtistViewModel = hiltViewModel()
) {
    val uiState by viewModel.profileUiState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadArtistProfile(artistId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is ArtistProfileUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ArtistProfileUiState.Success -> {
                val artist = state.profile.artist
                val veshas = state.profile.veshas

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero Section
                    Box(modifier = Modifier.height(350.dp)) {
                        AsyncImage(
                            model = artist.profile_image_url,
                            contentDescription = artist.name_english,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                        startY = 200f
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = artist.name_kannada,
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = artist.name_english,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (artist.is_legendary) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text("Legendary Artist", modifier = Modifier.padding(2.dp))
                                }
                            }
                        }
                    }

                    // Bio Section
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Specialization: ${artist.specialization}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "From: ${artist.district}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Years Active: ${artist.years_active}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Biography",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = artist.bio_english,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = artist.bio_kannada,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Vesha Gallery
                        Text(
                            text = "Vesha Gallery",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        VeshaGallery(veshas = veshas)
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
            is ArtistProfileUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}")
                }
            }
        }
    }
}

@Composable
fun VeshaGallery(veshas: List<Vesha>) {
    if (veshas.isEmpty()) {
        Card(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(150.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("More coming soon", style = MaterialTheme.typography.bodyMedium)
            }
        }
    } else {
        LazyRow(
            contentPadding = PaddingValues(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(veshas) { vesha ->
                VeshaCard(vesha = vesha)
            }
            if (veshas.size < 3) {
                item {
                    ComingSoonVeshaCard()
                }
            }
        }
    }
}

@Composable
fun VeshaCard(vesha: Vesha) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = vesha.images.firstOrNull() ?: "",
                contentDescription = vesha.role_name_english,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = vesha.role_name_english,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Text(
                    text = vesha.character_type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun ComingSoonVeshaCard() {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("More coming soon", style = MaterialTheme.typography.bodySmall)
        }
    }
}
