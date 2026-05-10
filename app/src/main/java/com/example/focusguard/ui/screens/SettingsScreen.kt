package com.example.focusguard.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.focusguard.ui.FocusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: FocusViewModel) {
    val context = LocalContext.current
    val adminPin by viewModel.adminPin.collectAsState()
    val studyModeActive by viewModel.studyModeActive.collectAsState()
    
    var newPin by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Admin Settings", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = newPin,
                onValueChange = { newPin = it },
                label = { Text("Set New Admin PIN") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.setAdminPin(newPin) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Update PIN")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Study Mode Active")
                Switch(checked = studyModeActive, onCheckedChange = { viewModel.setStudyMode(it) })
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enable Accessibility Service")
            }
        }
    }
}
