package pl.lanku.productinventory.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    val ean: String,
    val name: String,
    val description: String,
    val category: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
