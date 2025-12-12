package com.example.mycallendar

import android.R
import android.app.DatePickerDialog
import android.os.Build
import java.time.LocalDate
import java.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.style.TextAlign
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.mycallendar.ui.theme.MyCallendarTheme
import java.text.DateFormat
import java.time.temporal.ChronoUnit
import java.util.Date

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCallendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyCallendar()
                }
            }
        }
    }
}

@Composable
fun MyCallendar() {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
        ) {
            Button(
                onClick = {
                    showDatePicker = true
                },
                modifier = Modifier.padding(top = 50.dp).size(150.dp),
                shape = CircleShape
            ) {
                Text(
                    text = if (selectedDate != null) {
                        "Изменить дату"
                    } else {
                        "Выбрать дату"
                    },
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
            if (showDatePicker) {
                MyDatePicker(
                    onDismiss = {
                        showDatePicker = false
                    },
                    onDataSelected = {
                        millis -> selectedDate = millis
                    },
                    initialDate = selectedDate
                )
            }
            if (selectedDate != null) {
                Text(text = formatDate(selectedDate!!))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    onDismiss : () -> Unit,
    onDataSelected : (Long?) -> Unit,
    initialDate : Long?
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDataSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(
                    text = "OK"
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }

    ) {
        DatePicker(
            state = datePickerState
        )
    }

}

fun formatDate(dateMillis : Long) : String {
    val date = Date(dateMillis)
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return format.format(date)
}