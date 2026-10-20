package com.example.shoppinglist.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.model.ShoppingItem

private val Green = Color(0xFF34C759)
private val CircleGray = Color(0xFFD1D1D6)
private val BorderGray = Color(0xFFE5E5EA)
private val BadgeGray = Color(0xFFF2F2F7)
private val SecondaryText = Color(0xFF8E8E93)

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (item.isBought) 0.6f else 1f)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, BorderGray, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckCircle(checked = item.isBought)

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = item.name,
            modifier = Modifier.weight(1f),
            fontSize = 17.sp,
            color = if (item.isBought) SecondaryText else Color.Black,
            textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
        )

        Text(
            text = "×${item.quantity}",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(BadgeGray)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 14.sp,
            color = SecondaryText
        )
    }
}

@Composable
private fun CheckCircle(checked: Boolean) {
    Canvas(modifier = Modifier.size(26.dp)) {
        if (checked) {
            drawCircle(color = Green)
            val check = Path().apply {
                moveTo(size.width * 0.28f, size.height * 0.52f)
                lineTo(size.width * 0.44f, size.height * 0.67f)
                lineTo(size.width * 0.72f, size.height * 0.36f)
            }
            drawPath(
                path = check,
                color = Color.White,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        } else {
            val strokeWidth = 2.dp.toPx()
            drawCircle(
                color = CircleGray,
                radius = size.minDimension / 2 - strokeWidth / 2,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
