package pl.lanku.inventory.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    val ean: String,
    val name: String,
    val description: String,
    val category: String
)
