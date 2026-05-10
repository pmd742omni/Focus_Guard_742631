package com.example.focusguard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.focusguard.ui.FocusViewModel
import com.example.focusguard.ui.screens.*
import com.example.focusguard.ui.theme.FocusGuardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Request necessary permissions
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }

        setContent {
            val viewModel: FocusViewModel = viewModel()
            val colorIndex by viewModel.colorIndex.collectAsState()
            
            FocusGuardTheme(colorIndex = colorIndex) {
                MainApp(viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: FocusViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = { navController.navigate("dashboard") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Lock, "Apps") },
                    label = { Text("Apps") },
                    selected = false,
                    onClick = { navController.navigate("app_selection") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.SupervisorAccount, "Admin") },
                    label = { Text("Admin") },
                    selected = false,
                    onClick = { navController.navigate("admin_panel") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { navController.navigate("settings") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(viewModel, onNavigateToColorPicker = { navController.navigate("color_picker") })
            }
            composable("app_selection") {
                AppSelectionScreen(viewModel)
            }
            composable("admin_panel") {
                AdminPanelScreen(viewModel)
            }
            composable("settings") {
                SettingsScreen(viewModel)
            }
            composable("color_picker") {
                ColorPickerScreen(viewModel, onBack = { navController.popBackStack() })
            }
        }
    }
}
