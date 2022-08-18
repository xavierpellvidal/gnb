package com.gnb.domain.entity.remote

import com.gnb.domain.entity.IConvertibleTo
import com.gnb.domain.entity.local.RateEntity
import java.io.Serializable

data class RateModel(

    var from: String,
    var to: String,
    var rate: String

) : Serializable, IConvertibleTo<RateEntity> {
    override fun convertTo() = RateEntity(from, to, rate)
}






