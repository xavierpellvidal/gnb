package com.gnb.domain.entity.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gnb.domain.entity.IConvertibleTo
import com.gnb.domain.entity.ui.ProductUI
import java.io.Serializable

@Entity(tableName = "transactions")
data class TransactionEntity(

    @ColumnInfo(name = "sku")
    var sku: String,

    @ColumnInfo(name = "amount")
    var amount: String,

    @ColumnInfo(name = "currency")
    var currency: String

) : Serializable, IConvertibleTo<ProductUI> {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun convertTo() = ProductUI(sku, amount, currency)
}
