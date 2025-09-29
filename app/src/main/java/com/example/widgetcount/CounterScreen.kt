package com.example.widgetcount

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CounterScreen(context: Context) {
    val countFlow by CountPreferences.countFlow(context).collectAsState(initial = 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("현재 카운트: $countFlow", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val newCount = countFlow + 1
                //  앱화면 DataStore 업데이트
                CountPreferences.setCount(context, newCount)

                //  Glance 위젯 상태 업데이트
                updateWidgetCount(context, newCount)
            }
        }) {
            Text("카운트 증가")
        }
    }
}

suspend fun updateWidgetCount(context: Context, count: Int) {
    val ids = GlanceAppWidgetManager(context).getGlanceIds(CounterWidget::class.java)

    ids.forEach { id ->
        updateAppWidgetState(context, id) { prefs ->
            prefs[CountPreferences.COUNT_KEY] = count
        }
        CounterWidget().update(context, id)
    }
}