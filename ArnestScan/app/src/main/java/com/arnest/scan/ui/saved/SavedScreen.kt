package com.arnest.scan.ui.saved

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arnest.scan.data.ProductRepository
import com.arnest.scan.model.Product
import com.arnest.scan.model.SafetyStatus
import com.arnest.scan.ui.theme.ModerateYellow
import com.arnest.scan.ui.theme.ModerateYellowText
import com.arnest.scan.ui.theme.PrimaryBlue
import com.arnest.scan.ui.theme.RiskyRed
import com.arnest.scan.ui.theme.RiskyRedText
import com.arnest.scan.ui.theme.SafeGreen
import com.arnest.scan.ui.theme.SafeGreenText
import com.arnest.scan.ui.theme.TextSecondary
import com.arnest.scan.viewmodel.SavedViewModel

@Composable
fun SavedScreen(
    repository: ProductRepository,
    viewModel: SavedViewModel = viewModel(factory = SavedViewModel.Factory(repository))
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Сохранённые средства",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Всего: ${uiState.savedProducts.size}",
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }


        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCounter(
                    count = uiState.safeCount,
                    label = "Безопасных",
                    backgroundColor = SafeGreen,
                    textColor = SafeGreenText,
                    modifier = Modifier.weight(1f)
                )
                StatusCounter(
                    count = uiState.moderateCount,
                    label = "Умеренных",
                    backgroundColor = ModerateYellow,
                    textColor = ModerateYellowText,
                    modifier = Modifier.weight(1f)
                )
                StatusCounter(
                    count = uiState.riskyCount,
                    label = "Рискованных",
                    backgroundColor = RiskyRed,
                    textColor = RiskyRedText,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (uiState.savedProducts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Пока ничего не сохранено",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Отсканируйте товар и нажмите «Сохранить»",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            val grouped = uiState.savedProducts.groupBy { it.second }

            for (status in listOf(SafetyStatus.RISKY, SafetyStatus.MODERATE, SafetyStatus.SAFE)) {
                val group = grouped[status] ?: continue
                val (borderColor, label) = when (status) {
                    SafetyStatus.SAFE -> SafeGreen to "Безопасные"
                    SafetyStatus.MODERATE -> ModerateYellow to "Требуют осторожности"
                    SafetyStatus.RISKY -> RiskyRed to "Рискованные"
                }

                item {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(group) { (product, st) ->
                    SavedProductCard(
                        product = product,
                        safetyStatus = st,
                        borderColor = borderColor,
                        onDelete = { viewModel.deleteProduct(product.barcode) }
                    )
                }
            }
        }


        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Офлайн-доступ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Сохраните список безопасных средств для использования без интернета",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.exportList(context) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        enabled = uiState.savedProducts.isNotEmpty()
                    ) {
                        Text(
                            "Экспортировать список",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun StatusCounter(count: Int, label: String, backgroundColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$count",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = textColor
            )
        }
    }
}

@Composable
fun SavedProductCard(
    product: Product,
    safetyStatus: SafetyStatus,
    borderColor: Color,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val (safetyBg, safetyTextColor, safetyLabel, safetyIcon) = savedSafetyVisuals(safetyStatus)
    val ingredientCount = product.composition
        .split(",")
        .count { it.isNotBlank() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = borderColor.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 3.dp else 0.dp)
    ) {
        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageUrls.first())
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            safetyIcon,
                            contentDescription = null,
                            tint = safetyTextColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = if (expanded) 3 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 3.dp)
                    ) {
                        Icon(
                            safetyIcon,
                            contentDescription = null,
                            tint = safetyTextColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = safetyLabel,
                            fontSize = 12.sp,
                            color = safetyTextColor,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = " · ${product.barcode}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Свернуть" else "Развернуть",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = "Удалить",
                        tint = TextSecondary
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ) + fadeIn(animationSpec = tween(200)),
                exit = shrinkVertically(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ) + fadeOut(animationSpec = tween(150))
            ) {
                Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 14.dp)) {

                    if (product.imageUrls.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            product.imageUrls.forEach { url ->
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(url)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .height(140.dp)
                                        .aspectRatio(0.75f)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }


                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(safetyBg)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(safetyIcon, contentDescription = null, tint = safetyTextColor, modifier = Modifier.size(16.dp))
                            Text(safetyLabel, color = safetyTextColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE8EAFF))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(Icons.Default.Science, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
                            Text("$ingredientCount комп.", color = PrimaryBlue, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        text = "Состав",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = product.composition,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

private fun savedSafetyVisuals(status: SafetyStatus): SavedSafetyVisuals {
    return when (status) {
        SafetyStatus.SAFE -> SavedSafetyVisuals(
            SafeGreen, SafeGreenText, "Безопасно", Icons.Default.VerifiedUser
        )
        SafetyStatus.MODERATE -> SavedSafetyVisuals(
            ModerateYellow, ModerateYellowText, "Умеренно", Icons.Default.Shield
        )
        SafetyStatus.RISKY -> SavedSafetyVisuals(
            RiskyRed, RiskyRedText, "Рискованно", Icons.Default.Warning
        )
    }
}

private data class SavedSafetyVisuals(
    val bg: Color,
    val textColor: Color,
    val label: String,
    val icon: ImageVector
)
