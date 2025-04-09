package com.example.compose_ta09.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// Extension function untuk menjalankan coroutine di dalam CoroutineScope
fun CoroutineScope.launch(block: suspend CoroutineScope.() -> Unit) {
    launch(Dispatchers.Main, block = block)
}

@Composable
fun <T : Any> OnEvent(
    events: Flow<T>,
    handleEvent: (T) -> Unit,
) {
    require(events !is StateFlow<*>) { "Events flow cannot be StateFlow" }

    LaunchedEffect(Unit, events) {
        events.onEach { event ->
            handleEvent(event)
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }
}
