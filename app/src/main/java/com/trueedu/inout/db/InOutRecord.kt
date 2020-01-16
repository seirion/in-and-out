package com.trueedu.inout.db

data class InOutRecord(
    val inOut: InOut,
    val timestamp: Long
)

enum class InOut(val value: Int) {
    IN(0),
    OUT(1),
}