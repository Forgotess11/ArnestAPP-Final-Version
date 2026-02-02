package com.arnest.scan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arnest.scan.data.ProductRepository
import com.arnest.scan.model.Product
import com.arnest.scan.model.SafetyStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ScannerUiState(
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val selectedProduct: Product? = null,
    val safetyStatus: SafetyStatus = SafetyStatus.MODERATE,
    val isSaved: Boolean = false,
    val showCamera: Boolean = false,
    val showProductCard: Boolean = false,
    val testProducts: List<Product> = emptyList()
)

class ScannerViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState

    init {
        _uiState.value = _uiState.value.copy(testProducts = repository.getAllProducts().take(6))
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.length >= 2) {
            val results = repository.searchByName(query)
            _uiState.value = _uiState.value.copy(searchResults = results)
        } else {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    fun onBarcodeScanned(barcode: String) {
        val product = repository.searchByBarcode(barcode)
        _uiState.value = _uiState.value.copy(
            selectedProduct = product,
            safetyStatus = product?.let { stubSafety(it) } ?: SafetyStatus.MODERATE,
            showCamera = false,
            showProductCard = product != null
        )
        if (product != null) {
            checkIfSaved(product.barcode)
        }
    }

    fun onProductSelected(product: Product) {
        _uiState.value = _uiState.value.copy(
            selectedProduct = product,
            safetyStatus = stubSafety(product),
            searchResults = emptyList(),
            searchQuery = "",
            showProductCard = true
        )
        checkIfSaved(product.barcode)
    }

    private fun stubSafety(product: Product): SafetyStatus {
        val hash = product.barcode.hashCode().let { if (it < 0) -it else it }
        return when (hash % 3) {
            0 -> SafetyStatus.SAFE
            1 -> SafetyStatus.MODERATE
            else -> SafetyStatus.RISKY
        }
    }

    fun onScanButtonClicked() {
        _uiState.value = _uiState.value.copy(showCamera = true)
    }

    fun onCameraDismissed() {
        _uiState.value = _uiState.value.copy(showCamera = false)
    }

    fun onDismissProduct() {
        _uiState.value = _uiState.value.copy(
            showProductCard = false,
            selectedProduct = null,
            isSaved = false
        )
    }

    fun saveProduct() {
        val product = _uiState.value.selectedProduct ?: return
        viewModelScope.launch {
            repository.saveProduct(product, _uiState.value.safetyStatus)
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }

    private fun checkIfSaved(barcode: String) {
        viewModelScope.launch {
            val saved = repository.isSaved(barcode)
            _uiState.value = _uiState.value.copy(isSaved = saved)
        }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScannerViewModel(repository) as T
        }
    }
}
