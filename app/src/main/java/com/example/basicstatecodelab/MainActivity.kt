package com.example.basicstatecodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.basicstatecodelab.ui.theme.BasicStateCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicStateCodelabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WellnessScreen()
                }
            }
        }
    }
}

// Data class за задачи
data class WellnessTask(val id: Int, val label: String)

// Листа на задачи
fun getWellnessTasks() = List(30) { i ->
    WellnessTask(id = i, label = "Задача број $i")
}

// Главен Wellness екран
@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        StatefulWaterCounter()
        WellnessTasksList(
            list = remember { getWellnessTasks().toMutableStateList() }
        )
    }
}

// Stateful WaterCounter
@Composable
fun StatefulWaterCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }

    StatelessWaterCounter(
        count = count,
        onIncrement = { if (count < 10) count++ },
        onReset = { count = 0 },
        modifier = modifier
    )
}

// Stateless WaterCounter
@Composable
fun StatelessWaterCounter(
    count: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "💧 Вода денес",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (count > 0) {
            Text(
                text = "Изпив $count чаши вода!",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onIncrement,
                enabled = count < 10
            ) {
                Text("Додај чаша")
            }

            if (count > 0) {
                OutlinedButton(onClick = onReset) {
                    Text("Ресетирај")
                }
            }
        }

        if (count >= 8) {
            Text(
                text = "🎉 Цел постигната! Одлично!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Листа на задачи
@Composable
fun WellnessTasksList(
    list: MutableList<WellnessTask>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = list,
            key = { it.id }
        ) { task ->
            WellnessTaskItem(
                taskName = task.label,
                onClose = { list.remove(task) }
            )
        }
    }
}

// Stateful WellnessTaskItem
@Composable
fun WellnessTaskItem(
    taskName: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var checked by rememberSaveable { mutableStateOf(false) }

    WellnessTaskItemStateless(
        taskName = taskName,
        checked = checked,
        onCheckedChange = { checked = it },
        onClose = onClose,
        modifier = modifier
    )
}

// Stateless WellnessTaskItem
@Composable
fun WellnessTaskItemStateless(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),  // малку повеќе vertical padding за подобар изглед
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = taskName,
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Избриши задача"
            )
        }
    }
}