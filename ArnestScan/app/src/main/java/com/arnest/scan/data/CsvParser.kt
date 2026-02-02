package com.arnest.scan.data

import android.content.Context
import com.arnest.scan.model.Product
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvParser {

    fun parseProducts(context: Context): List<Product> {
        val products = mutableListOf<Product>()
        val inputStream = context.assets.open("arnest_db.csv")
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

        // Skip header
        reader.readLine()

        val lines = reader.readText()
        reader.close()

        // Parse CSV respecting quoted fields
        val records = parseCsvRecords(lines)
        for (fields in records) {
            if (fields.size >= 4) {
                val name = fields[0].trim()
                val barcode = fields[1].trim()
                val imageUrls = fields[2].split(" / ").map { it.trim() }.filter { it.isNotEmpty() }
                val composition = fields[3].trim()
                if (name.isNotEmpty()) {
                    products.add(Product(name, barcode, imageUrls, composition))
                }
            }
        }

        return products
    }

    private fun parseCsvRecords(text: String): List<List<String>> {
        val records = mutableListOf<List<String>>()
        val currentField = StringBuilder()
        val currentRecord = mutableListOf<String>()
        var inQuotes = false

        var i = 0
        while (i < text.length) {
            val ch = text[i]
            when {
                ch == '"' && !inQuotes -> {
                    inQuotes = true
                }
                ch == '"' && inQuotes -> {
                    if (i + 1 < text.length && text[i + 1] == '"') {
                        currentField.append('"')
                        i++
                    } else {
                        inQuotes = false
                    }
                }
                ch == ',' && !inQuotes -> {
                    currentRecord.add(currentField.toString())
                    currentField.clear()
                }
                ch == '\n' && !inQuotes -> {
                    currentRecord.add(currentField.toString())
                    currentField.clear()
                    if (currentRecord.any { it.isNotBlank() }) {
                        records.add(currentRecord.toList())
                    }
                    currentRecord.clear()
                }
                ch == '\r' && !inQuotes -> {
                    // skip
                }
                else -> {
                    currentField.append(ch)
                }
            }
            i++
        }

        // Last record
        if (currentField.isNotEmpty() || currentRecord.isNotEmpty()) {
            currentRecord.add(currentField.toString())
            if (currentRecord.any { it.isNotBlank() }) {
                records.add(currentRecord.toList())
            }
        }

        return records
    }
}
