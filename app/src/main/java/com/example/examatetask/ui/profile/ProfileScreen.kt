package com.example.examatetask.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfilesRoute(modifier: Modifier = Modifier) {
    ProfileScreen(modifier = modifier)
}

@Composable
fun ProfileScreen(modifier: Modifier) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(modifier = Modifier.fillMaxSize())
}