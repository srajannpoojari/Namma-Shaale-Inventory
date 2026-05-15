package com.example.nammashaaleinventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val serialNumber: String,
    val condition: String,
    val imageUri: String? = null,
    val issueDescription: String? = null,
    val issueDate: String? = null
)
