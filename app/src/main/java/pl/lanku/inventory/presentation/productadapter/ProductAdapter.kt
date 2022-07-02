package pl.lanku.inventory.presentation.productadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product

class ProductAdapter(private var productList:List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eanIR: TextView = itemView.findViewById(R.id.recycler_ean)
        private val nameIR: TextView = itemView.findViewById(R.id.recycler_name)
        private val descriptionIR: TextView = itemView.findViewById(R.id.recycler_description)
        private val categoryIR: TextView = itemView.findViewById(R.id.recycler_category)

        fun bind(product: Product) {
            eanIR.text = product.ean
            nameIR.text = product.name
            descriptionIR.text = product.description
            categoryIR.text = product.description
        }
    }

    fun updateProducts(productList: List<Product>) {
        this.productList = productList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = productList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}