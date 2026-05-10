package com.example.focusguard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focusguard.ui.FocusViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: FocusViewModel, onNavigateToColorPicker: () -> Unit) {
    val blockedCount by viewModel.blockedAppsCount.collectAsState()
    val requests by viewModel.allRequests.collectAsState()
    val studyModeActive by viewModel.studyModeActive.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Focus Guard Dashboard",
                        fontWeight = FontWeight.Bold
                    ) 
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToColorPicker) {
                Icon(Icons.Default.Palette, contentDescription = "Colors")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Study Mode", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(if (studyModeActive) "Active" else "Inactive", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$blockedCount Apps Blocked", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Recent Permission Requests", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(requests.take(5)) { request ->
                ListItem(
                    headlineContent = { Text(request.appName) },
                    supportingContent = { Text("${request.durationMinutes} mins - ${request.status}") },
                    trailingContent = { Text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(request.timestamp))) }
                )
                HorizontalDivider()
            }
        }
    }
}
