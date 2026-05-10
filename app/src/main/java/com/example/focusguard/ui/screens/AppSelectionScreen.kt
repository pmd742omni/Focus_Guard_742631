package com.example.focusguard.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.focusguard.ui.FocusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(viewModel: FocusViewModel) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val blockedApps by viewModel.allBlockedApps.collectAsState()
    
    val installedApps = remember {
        packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
            .map { it.packageName to packageManager.getApplicationLabel(it).toString() }
            .sortedBy { it.second }
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredApps = installedApps.filter { it.second.contains(searchQuery, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Apps to Block") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Apps") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(filteredApps) { (packageName, appName) ->
                    val isBlocked = blockedApps.any { it.packageName == packageName }
                    ListItem(
                        headlineContent = { Text(appName) },
                        supportingContent = { Text(packageName) },
                        trailingContent = {
                            Checkbox(
                                checked = isBlocked,
                                onCheckedChange = { viewModel.toggleBlockApp(packageName, appName, it) }
                            )
                        }
                    )
                }
            }
        }
    }
}
