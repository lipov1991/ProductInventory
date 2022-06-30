package pl.lanku.inventory.presentation.productadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product

class ProductAdapter(private val onClick: (Product) -> Unit) : ListAdapter<Product,ProductAdapter.ProductViewHolder>(ProductDiffCallback) {
    private var numbersOfProducts :Int = 0

    class ProductViewHolder(itemView:View,val onClick: (Product) -> Unit):RecyclerView.ViewHolder(itemView)
    {
        private val eanIR:TextView = itemView.findViewById(R.id.recycler_ean)
        private val nameIR:TextView = itemView.findViewById(R.id.recycler_name)
        private val descriptionIR:TextView = itemView.findViewById(R.id.recycler_description)
        private val categoryIR:TextView = itemView.findViewById(R.id.recycler_category)
        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener{
                currentProduct?.let {
                    onClick(it)
                }
            }
        }

        fun bind(product: Product){
            currentProduct = product
            eanIR.text = product.ean
            nameIR.text = product.name
            descriptionIR.text = product.description
            categoryIR.text = product.description
        }
    }

    fun updateProductCount(updatedProductCount: Int) {
        numbersOfProducts = updatedProductCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ProductViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item,parent,false)
        return ProductViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder:ProductViewHolder, position: Int){
        val product = getItem(position)
        holder.bind(product)
    }
}

object ProductDiffCallback : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.ean == newItem.ean
    }
}