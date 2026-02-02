package com.arnest.scan.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arnest.scan.data.ProductRepository
import com.arnest.scan.model.Product
import com.arnest.scan.model.SafetyStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

data class SavedUiState(
    val savedProducts: List<Pair<Product, SafetyStatus>> = emptyList(),
    val safeCount: Int = 0,
    val moderateCount: Int = 0,
    val riskyCount: Int = 0
)

class SavedViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedUiState())
    val uiState: StateFlow<SavedUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.getSavedProducts().collect { products ->
                _uiState.value = SavedUiState(
                    savedProducts = products,
                    safeCount = products.count { it.second == SafetyStatus.SAFE },
                    moderateCount = products.count { it.second == SafetyStatus.MODERATE },
                    riskyCount = products.count { it.second == SafetyStatus.RISKY }
                )
            }
        }
    }

    fun deleteProduct(barcode: String) {
        viewModelScope.launch {
            repository.removeProduct(barcode)
        }
    }

    fun exportList(context: Context) {
        val products = _uiState.value.savedProducts
        if (products.isEmpty()) return

        val sb = StringBuilder()
        sb.appendLine("Сохранённые средства — ArnestScan")
        sb.appendLine("=" .repeat(40))
        sb.appendLine()

        for ((product, status) in products) {
            val statusText = when (status) {
                SafetyStatus.SAFE -> "Безопасно"
                SafetyStatus.MODERATE -> "Умеренно"
                SafetyStatus.RISKY -> "Рискованно"
            }
            sb.appendLine("${product.name}")
            sb.appendLine("Штрих-код: ${product.barcode}")
            sb.appendLine("Статус: $statusText")
            sb.appendLine("Состав: ${product.composition}")
            sb.appendLine("-".repeat(40))
            sb.appendLine()
        }

        val file = File(context.cacheDir, "arnest_saved_list.txt")
        file.writeText(sb.toString())

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Экспортировать список"))
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SavedViewModel(repository) as T
        }
    }
}
