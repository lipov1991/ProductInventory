package pl.lanku.inventory.presentation.productadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product

class ProductAdapter(
    private var productList: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private lateinit var mListener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
//        fun onRemoveClick(position: Int)
    }

    fun setOnClickItemListener(listener:OnItemClickListener){
        mListener=listener
    }

    class ProductViewHolder(itemView: View, listener:OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val eanIR: TextView = itemView.findViewById(R.id.recycler_ean)
        private val nameIR: TextView = itemView.findViewById(R.id.recycler_name)
        private val descriptionIR: TextView = itemView.findViewById(R.id.recycler_description)
        private val categoryIR: TextView = itemView.findViewById(R.id.recycler_category)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
//                listener.onRemoveClick(adapterPosition)
            }
        }

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
        return ProductViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}