package com.nooro.weathertracker.util

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nooro.weathertracker.ui.theme.Opal

/**
 * Created by Jeetesh Surana.
 */

@Composable
fun LoaderDialog(isVisible: Boolean) {
    if (isVisible) {
        Dialog(onDismissRequest = { /* Do nothing to prevent dismissal */ }) {
            Card(modifier = Modifier.wrapContentSize()) {
                CircularProgressIndicator(
                    color = Opal,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}