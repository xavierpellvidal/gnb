package com.gnb.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gnb.databinding.ItemTransactionBinding
import com.gnb.domain.entity.ui.TransactionUI
import java.text.SimpleDateFormat
import java.util.*

class LaunchListAdapter : ListAdapter<TransactionUI, LaunchListAdapter.LaunchViewHolder>(LaunchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LaunchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindStyle(item)
    }

    class LaunchViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindStyle(transactions: TransactionUI): Unit = with(binding) {
            // Date formatter
            val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()

            // Fill ui
            txtAmount.text = transactions.amount + " " + transactions.currency
            txtConvertedAmount.text = transactions.convertedAmount + " " + transactions.convertedCurrency
        }

        companion object {
            fun from(parent: ViewGroup): LaunchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTransactionBinding.inflate(layoutInflater, parent, false)
                return LaunchViewHolder(binding)
            }
        }
    }
}

class LaunchDiffCallback : DiffUtil.ItemCallback<TransactionUI>() {

    override fun areItemsTheSame(oldItem: TransactionUI, newItem: TransactionUI): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TransactionUI, newItem: TransactionUI): Boolean {
        return oldItem == newItem
    }

}
