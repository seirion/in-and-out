package com.trueedu.inout.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [InOutRecord::class], version = 1)
@TypeConverters(InOutConverters::class)
abstract class InOutRecordBase : RoomDatabase() {
    abstract fun inOutRecordDao(): InOutRecordDao

    companion object {
        private var INSTANCE: InOutRecordBase? = null

        fun getInstance(context: Context): InOutRecordBase {
            if (INSTANCE == null) {
                synchronized(InOutRecordBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        InOutRecordBase::class.java, "InOutRecord.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
