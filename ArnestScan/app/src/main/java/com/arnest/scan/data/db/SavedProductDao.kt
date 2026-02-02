package com.arnest.scan.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedProductDao {
    @Query("SELECT * FROM saved_products")
    fun getAllSaved(): Flow<List<SavedProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: SavedProductEntity)

    @Query("DELETE FROM saved_products WHERE barcode = :barcode")
    suspend fun deleteByBarcode(barcode: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_products WHERE barcode = :barcode)")
    suspend fun isSaved(barcode: String): Boolean
}
