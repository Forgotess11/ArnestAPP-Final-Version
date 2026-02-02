package com.arnest.scan.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arnest.scan.data.ProductRepository
import com.arnest.scan.ui.learning.LearningScreen
import com.arnest.scan.ui.saved.SavedScreen
import com.arnest.scan.ui.scanner.ScannerScreen
import com.arnest.scan.ui.theme.PrimaryBlue
import kotlinx.coroutines.delay

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Scanner : Screen("scanner", "Сканер", Icons.Outlined.CameraAlt)
    data object Learning : Screen("learning", "Обучение", Icons.Outlined.MenuBook)
    data object Saved : Screen("saved", "Сохранённое", Icons.Outlined.Favorite)
}

val bottomNavItems = listOf(Screen.Scanner, Screen.Learning, Screen.Saved)

@Composable
fun AppNavigation(repository: ProductRepository) {
    var splashVisible by remember { mutableStateOf(true) }
    var splashStarted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        splashStarted = true
        delay(1400)
        splashVisible = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content — always composed, fades in under the splash
        MainContent(repository = repository)

        // Splash overlay
        AnimatedVisibility(
            visible = splashVisible,
            exit = fadeOut(animationSpec = tween(400))
        ) {
            SplashOverlay(started = splashStarted)
        }
    }
}

@Composable
private fun SplashOverlay(started: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (started) 1f else 0.5f,
        animationSpec = tween(700, easing = EaseOutCubic),
        label = "splashScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(500),
        label = "splashAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .alpha(alpha)
        ) {
            Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Арнест",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Проверка безопасности косметики",
                fontSize = 14.sp,
                color = PrimaryBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MainContent(repository: ProductRepository) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Scanner.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Scanner.route) {
                ScannerScreen(repository = repository)
            }
            composable(Screen.Learning.route) {
                LearningScreen()
            }
            composable(Screen.Saved.route) {
                SavedScreen(repository = repository)
            }
        }
    }
}
