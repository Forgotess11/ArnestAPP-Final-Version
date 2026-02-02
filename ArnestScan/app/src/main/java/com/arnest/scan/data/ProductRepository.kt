package com.arnest.scan.data

import android.content.Context
import com.arnest.scan.data.db.AppDatabase
import com.arnest.scan.data.db.SavedProductEntity
import com.arnest.scan.model.Product
import com.arnest.scan.model.SafetyStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(context: Context) {

    private val allProducts: List<Product> = CsvParser.parseProducts(context)
    private val dao = AppDatabase.getInstance(context).savedProductDao()

    fun searchByBarcode(barcode: String): Product? {
        return allProducts.find { it.barcode == barcode }
    }

    fun searchByName(query: String): List<Product> {
        val lower = query.lowercase()
        return allProducts.filter { it.name.lowercase().contains(lower) }
    }

    fun getAllProducts(): List<Product> = allProducts

    fun getSavedProducts(): Flow<List<Pair<Product, SafetyStatus>>> {
        return dao.getAllSaved().map { entities ->
            entities.map { entity ->
                val product = Product(
                    name = entity.name,
                    barcode = entity.barcode,
                    imageUrls = entity.imageUrls.split(" / ").filter { it.isNotBlank() },
                    composition = entity.composition
                )
                val status = try {
                    SafetyStatus.valueOf(entity.safetyStatus)
                } catch (_: Exception) {
                    SafetyStatus.MODERATE
                }
                product to status
            }
        }
    }

    suspend fun saveProduct(product: Product, status: SafetyStatus) {
        dao.insert(
            SavedProductEntity(
                barcode = product.barcode,
                name = product.name,
                imageUrls = product.imageUrls.joinToString(" / "),
                composition = product.composition,
                safetyStatus = status.name
            )
        )
    }

    suspend fun removeProduct(barcode: String) {
        dao.deleteByBarcode(barcode)
    }

    suspend fun isSaved(barcode: String): Boolean {
        return dao.isSaved(barcode)
    }
}
