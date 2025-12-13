package com.example.mycallendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycallendar.ui.theme.MyCallendarTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import kotlin.math.abs


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

@RequiresApi(Build.VERSION_CODES.O)
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
                modifier = Modifier
                    .padding(top = 50.dp)
                    .size(150.dp),
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
                DaysUntil(selectedDate!!)
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysUntil(
    selectedDateMillis: Long
) {
    val instant = Instant.ofEpochMilli(selectedDateMillis)
    val zoneId = ZoneId.of("Europe/Moscow")
    val localDate = instant.atZone(zoneId).toLocalDate()
    val daysUntil = ChronoUnit.DAYS.between(localDate, LocalDate.now())
    val dateText = formatDate(selectedDateMillis)
    val daysUntilPositive = abs(daysUntil.toInt())
    val text = when {
        daysUntil.toInt() == 0 -> "Сегодня"
        daysUntil.toInt() < 0 -> "До этого дня $daysUntilPositive дней"
        else -> "Этот день был $daysUntilPositive дней назад"
    }

    Column {
        Text("Выбранная дата: $dateText")
        Text(text=text)
    }


}
fun formatDate(dateMillis : Long) : String {
    val date = Date(dateMillis)
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return format.format(date)
}