package com.anderpri.pasapote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KonpartsaDao {
    @Query("SELECT * FROM konpartsa")
    fun getAll(): Flow<List<KonpartsaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(konpartsak: List<KonpartsaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(konpartsa: KonpartsaEntity)

    @Update
    suspend fun update(konpartsa: KonpartsaEntity)

    @Delete
    suspend fun delete(konpartsa: KonpartsaEntity)
}