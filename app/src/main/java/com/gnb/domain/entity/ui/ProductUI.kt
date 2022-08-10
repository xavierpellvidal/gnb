package com.gnb.domain.entity.ui

import com.gnb.domain.entity.IConvertibleTo
import java.io.Serializable

data class ProductUI(

    var sku: String,
    var amount: String,
    var currency: String

) : Serializable, IConvertibleTo<TransactionUI> {
    override fun convertTo() = TransactionUI(sku, amount, currency, amount, currency)
}
