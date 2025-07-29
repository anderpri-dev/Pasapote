package com.anderpri.pasapote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anderpri.pasapote.data.local.entity.KonpartsaImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KonpartsaImageDao {
    @Query("SELECT * FROM konpartsa_image WHERE konpartsaId = :konpartsaId")
    fun getImagesForKonpartsa(konpartsaId: String): Flow<List<KonpartsaImageEntity>>

    @Query("SELECT * FROM konpartsa_image WHERE konpartsaId = :konpartsaId AND year = :year LIMIT 1")
    fun getImageForKonpartsaInYear(konpartsaId: String, year: String): Flow<KonpartsaImageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: KonpartsaImageEntity)

    @Delete
    suspend fun delete(image: KonpartsaImageEntity)

    @Query("DELETE FROM konpartsa_image WHERE konpartsaId = :konpartsaId AND year = :year")
    suspend fun deleteByKonpartsaIdAndYear(konpartsaId: String, year: String)
}