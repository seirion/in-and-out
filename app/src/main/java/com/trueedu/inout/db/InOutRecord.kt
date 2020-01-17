package com.trueedu.inout.db

import androidx.room.*

@Entity
data class InOutRecord(
    var inOut: InOut,
    var timestamp: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}

enum class InOut(val value: Int) {
    IN(0),
    OUT(1),
}

class InOutConverters {
    @TypeConverter
    fun toInOut(value: Int): InOut {
        return when (value) {
            0 -> InOut.IN
            else -> InOut.OUT
        }
    }

    @TypeConverter
    fun fromInOut(inOut: InOut): Int {
        return inOut.value
    }
}