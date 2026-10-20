package com.example.shoppinglist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.model.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ShoppingViewModel : ViewModel() {

    private val _items = MutableStateFlow(
        listOf(
            ShoppingItem(name = "Молоко 1л"),
            ShoppingItem(name = "Хліб чорний", quantity = 2),
            ShoppingItem(name = "Яблука"),
            ShoppingItem(name = "Сир твердий"),
            ShoppingItem(name = "Яйця", quantity = 10, isBought = true),
            ShoppingItem(name = "Масло вершкове", isBought = true)
        )
    )
    val items: StateFlow<List<ShoppingItem>> = _items.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    val remainingCount: StateFlow<Int> = items
        .map { list -> list.count { !it.isBought } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun onInputChange(text: String) {
        _inputText.value = text
    }

    fun addItem() {
        val name = _inputText.value.trim()
        if (name.isEmpty()) return
        _items.update { it + ShoppingItem(name = name) }
        _inputText.value = ""
    }

    fun toggleBought(id: String) {
        _items.update { list ->
            list.map { if (it.id == id) it.copy(isBought = !it.isBought) else it }
        }
    }
}
