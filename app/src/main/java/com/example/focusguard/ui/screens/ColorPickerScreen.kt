package com.example.focusguard.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.focusguard.ui.FocusViewModel
import com.example.focusguard.util.ColorProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerScreen(viewModel: FocusViewModel, onBack: () -> Unit) {
    val selectedIndex by viewModel.colorIndex.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Choose Theme Color") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Select one of the 256 color profiles:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyHorizontalGrid(
                rows = GridCells.Fixed(4), // 4 rows to make it look like a grid
                modifier = Modifier.height(240.dp).fillMaxWidth(),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(ColorProvider.colors) { index, color ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedIndex == index) 3.dp else 0.dp,
                                color = if (selectedIndex == index) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.setColorIndex(index) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Done")
            }
        }
    }
}

import androidx.compose.foundation.background
