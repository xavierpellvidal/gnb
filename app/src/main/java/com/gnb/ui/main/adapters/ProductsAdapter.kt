package com.gnb.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gnb.R
import com.gnb.databinding.ItemProductBinding
import com.gnb.domain.entity.ui.ProductUI

class ProductsListAdapter (private val onProductClick: (ProductUI) -> Unit) : ListAdapter<ProductUI, ProductsListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent, onProductClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindStyle(item)
    }

    class ProductViewHolder private constructor(private val binding: ItemProductBinding, var context: Context, val onProductClick: (ProductUI) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindStyle(products: ProductUI): Unit = with(binding) {
            //  Fill ui
            binding.txtSku.text = products.sku
            binding.txtTransactions.text = context.resources.getQuantityString(
                R.plurals.products_list_transactions, products.amount.toInt(), products.amount.toInt())

            // Item clicked
            itemView.setOnClickListener {
                onProductClick(products)
            }
        }

        companion object {
            fun from(parent: ViewGroup, onProductClick: (ProductUI) -> Unit): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
                return ProductViewHolder(binding, parent.context, onProductClick)
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductUI>() {

    override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem.sku == newItem.sku
    }

    override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem == newItem
    }

}