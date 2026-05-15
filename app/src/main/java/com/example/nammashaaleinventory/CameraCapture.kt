package com.example.nammashaaleinventory

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CameraCaptureScreen(onImageCaptured: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Camera feature temporarily disabled")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onImageCaptured("") }) {
            Text("Simulate Capture")
        }
    }
}
