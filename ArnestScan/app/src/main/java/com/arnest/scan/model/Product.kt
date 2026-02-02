package com.arnest.scan.model

data class Product(
    val name: String,
    val barcode: String,
    val imageUrls: List<String>,
    val composition: String
)

enum class SafetyStatus {
    SAFE,
    MODERATE,
    RISKY
}
