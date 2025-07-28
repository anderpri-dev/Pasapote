package com.anderpri.pasapote.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anderpri.pasapote.data.local.AppDatabase
import com.anderpri.pasapote.data.local.dao.KonpartsaDao
import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import com.anderpri.pasapote.domain.model.Konpartsa
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: RoomDatabase.Callback): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_db")
            .addCallback(callback)
            .build()

    @Provides
    fun provideKonpartsaDao(db: AppDatabase): KonpartsaDao = db.konpartsaDao()

    @Provides
    @Singleton
    fun provideRoomCallback(app: Application): RoomDatabase.Callback =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val database = Room.databaseBuilder(
                    app,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                val dao = database.konpartsaDao()
                CoroutineScope(Dispatchers.IO).launch {
                    val json =
                        app.assets.open("konpartsak.json").bufferedReader().use { it.readText() }
                    val konpartsak = Json.decodeFromString<List<Konpartsa>>(json)
                    Log.d(
                        "DatabaseModule",
                        "Inserting ${konpartsak.size} konpartsak into the database"
                    )
                    Log.d("DatabaseModule", "Konpartsak: $konpartsak")
                    dao.insertAll(
                        konpartsak.map {
                            KonpartsaEntity(
                                id = it.id,
                                number = it.number,
                                name = it.name,
                                year = it.year,
                                place = it.place,
                                txupineras = it.txupineras.joinToString("|"),
                                color = it.color,
                                imagePath = it.imagePath
                            )
                        }
                    )
                }
            }
        }
}