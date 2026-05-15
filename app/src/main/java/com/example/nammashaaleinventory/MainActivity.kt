package com.example.nammashaaleinventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nammashaaleinventory.ui.theme.NammaShaaleInventoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NammaShaaleInventoryTheme {
                InventoryApp()
            }
        }
    }
}

@Composable
fun InventoryApp() {
    val navController = rememberNavController()
    val assetViewModel: AssetViewModel = viewModel()
    
    // Simple Admin Simulation State
    var isAdmin by remember { mutableStateOf(value = false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    viewModel = assetViewModel,
                    isAdmin = isAdmin,
                    onRoleChange = { role -> isAdmin = role },
                    onAdd = { navController.navigate("add") },
                    onList = { navController.navigate("list") },
                    onRepair = { navController.navigate("repair") }
                )
            }
            composable("add") {
                AddAssetScreen(
                    viewModel = assetViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("list") {
                AssetListScreen(
                    viewModel = assetViewModel,
                    isAdmin = isAdmin,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("repair") {
                RepairRequestScreen(
                    viewModel = assetViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
