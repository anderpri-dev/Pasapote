package com.anderpri.pasapote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anderpri.pasapote.data.local.dao.KonpartsaDao
import com.anderpri.pasapote.data.local.dao.KonpartsaImageDao
import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import com.anderpri.pasapote.data.local.entity.KonpartsaImageEntity

@Database(entities = [KonpartsaEntity::class, KonpartsaImageEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun konpartsaDao(): KonpartsaDao
    abstract fun konpartsaImageDao(): KonpartsaImageDao
}