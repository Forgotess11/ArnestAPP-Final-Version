package com.arnest.scan.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arnest.scan.model.SafetyStatus

@Entity(tableName = "saved_products")
data class SavedProductEntity(
    @PrimaryKey val barcode: String,
    val name: String,
    val imageUrls: String, // stored as " / " separated
    val composition: String,
    val safetyStatus: String // SAFE, MODERATE, RISKY
)
