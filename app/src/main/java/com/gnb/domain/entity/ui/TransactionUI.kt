package com.gnb.domain.entity.ui

import java.io.Serializable

data class TransactionUI(

    var sku: String,
    var amount: String,
    var currency: String,
    var convertedAmount: String,
    var convertedCurrency: String

) : Serializable
