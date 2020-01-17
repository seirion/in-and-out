package com.trueedu.inout.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface InOutRecordDao {
    @Query("SELECT * FROM InOutRecord")
    fun getAll(): List<InOutRecord>

    @Insert(onConflict = REPLACE)
    fun insert(record: InOutRecord)

    @Delete
    fun delete(record: InOutRecord)

    @Query("DELETE from InOutRecord where id = :id")
    fun delete(id: Long)

    @Query("DELETE from InOutRecord")
    fun deleteAll()
}