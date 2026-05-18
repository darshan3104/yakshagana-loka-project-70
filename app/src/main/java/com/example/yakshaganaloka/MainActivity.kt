package com.example.yakshaganaloka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yakshaganaloka.presentation.artist.ArtistDirectoryScreen
import com.example.yakshaganaloka.presentation.artist.ArtistProfileScreen
import com.example.yakshaganaloka.presentation.home.HomeScreen
import com.example.yakshaganaloka.presentation.map.PerformanceMapScreen
import com.example.yakshaganaloka.presentation.mela.MelaScheduleScreen
import com.example.yakshaganaloka.presentation.radio.TalamaddaleRadioScreen
import com.example.yakshaganaloka.ui.theme.YakshaganaLokaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Trigger data seeding
        val seedRequest = OneTimeWorkRequestBuilder<com.example.yakshaganaloka.data.worker.SeedDataWorker>()
            .addTag("seed_data")
            .build()
        WorkManager.getInstance(this).enqueue(seedRequest)

        setContent {
            YakshaganaLokaTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onNavigateToMap = { navController.navigate("map") },
                            onNavigateToArtistDirectory = { navController.navigate("artist_directory") },
                            onNavigateToRadio = { navController.navigate("radio") },
                            onNavigateToMelaSchedule = { navController.navigate("mela_schedule") }
                        )
                    }
                    composable("artist_directory") {
                        ArtistDirectoryScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToProfile = { id -> navController.navigate("artist_profile/$id") }
                        )
                    }
                    composable(
                        route = "artist_profile/{artistId}",
                        arguments = listOf(navArgument("artistId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val artistId = backStackEntry.arguments?.getString("artistId") ?: ""
                        ArtistProfileScreen(
                            artistId = artistId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("map") { 
                        PerformanceMapScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("radio") { 
                        TalamaddaleRadioScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("mela_schedule") { 
                        MelaScheduleScreen(onNavigateBack = { navController.popBackStack() }) 
                    }
                }
            }
        }
    }
}
