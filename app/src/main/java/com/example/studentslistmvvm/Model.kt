package com.example.studentslistmvvm

import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class Model<T> {
    private var index: Int = 0
    private var scope = CoroutineScope(Dispatchers.IO)
    private var context: MutableList<Pair<Int, T>> = mutableListOf()

    private val mutableFlow = MutableStateFlow(context.toList())

    val notifyFlow = mutableFlow.asStateFlow()

    private fun notifyListeners() {
        scope.launch {
            mutableFlow.emit(context.toList())
        }
    }

    open fun add(entity: T) {
        context.add(++index to entity)
        notifyListeners()
    }

    open fun remove(entity: Pair<Int, T>) {
        context.remove(entity)
        notifyListeners()

    }

    open fun remove(id: Int) {
        var entity = context.firstOrNull { it.first == index }
        if (entity != null) {
            remove(entity)
            notifyListeners()
        }
    }

    fun get(): List<Pair<Int, T>> = context.toList()
}
