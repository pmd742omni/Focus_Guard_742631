package com.example.focusguard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.focusguard.data.RequestStatus
import com.example.focusguard.ui.FocusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(viewModel: FocusViewModel) {
    val requests by viewModel.allRequests.collectAsState()
    val pendingRequests = requests.filter { it.status == RequestStatus.PENDING }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Panel") })
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            item {
                Text(
                    "Pending Requests",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(pendingRequests) { request ->
                Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("App: ${request.appName}")
                        Text("Package: ${request.packageName}")
                        Text("Requested Duration: ${request.durationMinutes} minutes")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = { viewModel.updateRequestStatus(request, RequestStatus.DENIED) }) {
                                Text("Deny")
                            }
                            Button(onClick = { viewModel.updateRequestStatus(request, RequestStatus.APPROVED) }) {
                                Text("Approve")
                            }
                        }
                    }
                }
            }
        }
    }
}
