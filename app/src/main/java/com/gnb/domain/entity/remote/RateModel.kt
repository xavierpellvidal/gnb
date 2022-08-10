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



fun solution(T: Array<String>, R: Array<String>): Int {
    // write your code in Kotlin 1.3.11 (Linux)
    var score: Int = 0
    var result: List<Test> = T.zip(R) {n, f -> Test(n, f)}

    return score;
}

class Test(
    var name: String,
    var result: String
)



