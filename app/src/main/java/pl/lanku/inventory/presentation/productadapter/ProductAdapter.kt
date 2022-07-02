package pl.lanku.inventory.presentation.productadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product

class ProductAdapter(private val productList:LiveData<List<Product>>) : ListAdapter<Product,ProductAdapter.ProductViewHolder>(ProductDiffCallback) {
    private var numbersOfProducts :Int = updateProductCount(productList)

    class ProductViewHolder(itemView:View,private val productList:LiveData<List<Product>>):RecyclerView.ViewHolder(itemView)
    {
        val eanIR:TextView = itemView.findViewById(R.id.recycler_ean)
        val nameIR:TextView = itemView.findViewById(R.id.recycler_name)
        val descriptionIR:TextView = itemView.findViewById(R.id.recycler_description)
        val categoryIR:TextView = itemView.findViewById(R.id.recycler_category)
        var currentProduct: Product? = null

        fun bind(product: Product){
            currentProduct = product
            eanIR.text = product.ean
            nameIR.text = product.name
            descriptionIR.text = product.description
            categoryIR.text = product.description
        }
    }

    fun updateProductCount(productList:LiveData<List<Product>>): Int {
        return if(productList.value==null){
            0
        } else {
            productList.value!!.size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ProductViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item,parent,false)
        return ProductViewHolder(view,productList)
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