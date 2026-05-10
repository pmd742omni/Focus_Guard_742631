package com.example.focusguard.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focusguard.data.AppDatabase
import com.example.focusguard.data.PermissionRequest
import com.example.focusguard.ui.theme.FocusGuardTheme
import kotlinx.coroutines.launch

class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: ""
        
        setContent {
            FocusGuardTheme {
                LockScreen(packageName = packageName, onAuthorized = { finish() })
            }
        }
    }
}

import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode

@Composable
fun LockScreen(packageName: String, onAuthorized: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var showRequestDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(LocalContext.current)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .blur(10.dp),
        contentAlignment = Alignment.Center
    ) {
        // We can't actually blur the content behind the activity easily without a screenshot 
        // but this adds the effect to the overlay itself.
        // For a full system overlay blur, it's more complex.
    }
    
    // UI elements on top of the blur
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
                .border(1.dp, Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text(
                "FocusGuard",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "This app ($packageName) is restricted.",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Admin PIN") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (password == "1234") onAuthorized()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Unlock")
            }
            
            TextButton(onClick = { showRequestDialog = true }) {
                Text("Request Access", color = Color.White)
            }
        }
    }

    if (showRequestDialog) {
        RequestDialog(
            packageName = packageName,
            onDismiss = { showRequestDialog = false },
            onSubmit = { duration ->
                scope.launch {
                    db.appDao().insertRequest(
                        PermissionRequest(
                            packageName = packageName,
                            appName = packageName,
                            durationMinutes = duration
                        )
                    )
                    showRequestDialog = false
                }
            }
        )
    }
}

@Composable
fun RequestDialog(packageName: String, onDismiss: () -> Unit, onSubmit: (Int) -> Unit) {
    var duration by remember { mutableStateOf("15") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Request Access") },
        text = {
            Column {
                Text("How many minutes do you need?")
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(duration.toIntOrNull() ?: 15) }) {
                Text("Submit Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
