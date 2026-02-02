package com.arnest.scan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arnest.scan.data.ProductRepository
import com.arnest.scan.ui.navigation.AppNavigation
import com.arnest.scan.ui.theme.ArnestScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = ProductRepository(applicationContext)

        setContent {
            ArnestScanTheme {
                AppNavigation(repository = repository)
            }
        }
    }
}
