package com.example.shoppinglist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.viewmodel.ShoppingViewModel

private val Orange = Color(0xFFFF9500)
private val ScreenBackground = Color(0xFFF2F2F7)
private val FieldBackground = Color(0xFFF2F2F7)
private val DividerGray = Color(0xFFE5E5EA)
private val SecondaryText = Color(0xFF8E8E93)

@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel = viewModel()) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val remainingCount by viewModel.remainingCount.collectAsStateWithLifecycle()

    val toBuy = items.filter { !it.isBought }
    val bought = items.filter { it.isBought }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        //Заголовок з лічильником
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "🛒 Список покупок",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = buildAnnotatedString {
                    append("Залишилось купити: ")
                    withStyle(SpanStyle(color = Orange, fontWeight = FontWeight.Bold)) {
                        append("$remainingCount")
                    }
                },
                fontSize = 16.sp,
                color = SecondaryText
            )
        }

        HorizontalDivider(color = DividerGray)

        //Поле введення та кнопка Додати
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = viewModel::onInputChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Назва товару") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = FieldBackground,
                    unfocusedContainerColor = FieldBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Orange
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = viewModel::addItem,
                enabled = inputText.isNotBlank(),
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Text(text = "Додати", fontSize = 17.sp)
            }
        }

        HorizontalDivider(color = DividerGray)

        //Список товарів
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (toBuy.isNotEmpty()) {
                item(key = "header_to_buy") {
                    SectionHeader(text = "ПОТРІБНО КУПИТИ")
                }
                items(toBuy, key = { it.id }) { item ->
                    ShoppingItemCard(
                        item = item,
                        onClick = { viewModel.toggleBought(item.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }

            if (bought.isNotEmpty()) {
                item(key = "header_bought") {
                    SectionHeader(
                        text = "КУПЛЕНО",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
                items(bought, key = { it.id }) { item ->
                    ShoppingItemCard(
                        item = item,
                        onClick = { viewModel.toggleBought(item.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = SecondaryText
    )
}
