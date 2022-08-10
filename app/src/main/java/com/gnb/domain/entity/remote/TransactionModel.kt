package com.gnb.domain.entity.remote

import com.gnb.domain.entity.IConvertibleTo
import com.gnb.domain.entity.local.TransactionEntity
import java.io.Serializable

data class TransactionModel(

    var sku: String,
    var amount: String,
    var currency: String

) : Serializable, IConvertibleTo<TransactionEntity> {
    override fun convertTo() = TransactionEntity(sku, amount, currency)
}