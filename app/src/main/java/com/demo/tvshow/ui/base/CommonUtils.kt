package com.demo.tvshow.ui.base

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.demo.tvshow.R

@Composable
fun showAlert(alertDialog: MutableState<Boolean>, message: String?) {
    if (alertDialog.value) {
        AlertDialog(
            onDismissRequest = {
                alertDialog.value = false
            },
            title = {
                Text(text = stringResource(id = R.string.label_alert))
            },
            text = {
                Text(message ?: "")
            },
            confirmButton = {
            },
            dismissButton = {
                Button(

                    onClick = {
                        alertDialog.value = false
                    }) {
                    Text(stringResource(id = R.string.action_ok))
                }
            }
        )

    }
}