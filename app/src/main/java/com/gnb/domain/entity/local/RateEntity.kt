package com.gnb.domain.entity.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.gnb.domain.entity.IConvertibleTo
import com.gnb.domain.entity.ui.RateUI
import java.io.Serializable

@Entity(tableName = "rates", primaryKeys = ["from", "to"])
data class RateEntity(

    @ColumnInfo(name = "from")
    var from: String,

    @ColumnInfo(name = "to")
    var to: String,

    @ColumnInfo(name = "rate")
    var rate: String

) : Serializable, IConvertibleTo<RateUI> {
    override fun convertTo() = RateUI(from, to, rate)
}



