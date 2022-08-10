package com.gnb.domain.entity.ui

import com.gnb.domain.entity.IConvertibleTo
import com.gnb.domain.entity.local.RateEntity
import java.io.Serializable

data class RateUI(

    var from: String,
    var to: String,
    var rate: String

) : Serializable, IConvertibleTo<RateEntity> {
    override fun convertTo() = RateEntity(from, to, rate)
}



