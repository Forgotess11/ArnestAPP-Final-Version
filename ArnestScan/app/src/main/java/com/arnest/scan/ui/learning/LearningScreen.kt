package com.arnest.scan.ui.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BabyChangingStation
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnest.scan.ui.theme.TextSecondary

data class LearningCategory(
    val title: String,
    val description: String,
    val cardCount: Int,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color
)

private val categories = listOf(
    LearningCategory(
        title = "Основные аллергены",
        description = "Узнайте о распространённых аллергенах в косметике",
        cardCount = 3,
        icon = Icons.Default.Warning,
        backgroundColor = Color(0xFFFFF3CD),
        iconColor = Color(0xFFF9A825)
    ),
    LearningCategory(
        title = "Уход за чувствительной кожей",
        description = "Рекомендации дерматологов для чувствительной кожи",
        cardCount = 3,
        icon = Icons.Default.Spa,
        backgroundColor = Color(0xFFD5EDDB),
        iconColor = Color(0xFF2E7D32)
    ),
    LearningCategory(
        title = "Детская косметика",
        description = "Безопасные средства для детей",
        cardCount = 3,
        icon = Icons.Default.BabyChangingStation,
        backgroundColor = Color(0xFFE3D5ED),
        iconColor = Color(0xFF7B1FA2)
    ),
    LearningCategory(
        title = "Натуральные средства",
        description = "Безопасные натуральные альтернативы",
        cardCount = 3,
        icon = Icons.Default.Eco,
        backgroundColor = Color(0xFFD5EDDB),
        iconColor = Color(0xFF2E7D32)
    )
)

@Composable
fun LearningScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Обучающие материалы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Узнайте больше о безопасной косметике",
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(categories) { category ->
            CategoryCard(category)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFF9A825),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Совет дня",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Всегда читайте состав на обратной стороне упаковки. Ингредиенты перечислены в порядке убывания концентрации — первые 5 компонентов составляют основу продукта.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun CategoryCard(category: LearningCategory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(category.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    category.icon,
                    contentDescription = null,
                    tint = category.iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = category.description,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "${category.cardCount} карточек",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
