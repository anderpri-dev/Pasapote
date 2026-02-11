package com.anderpri.pasapote.data.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getAndroidDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("app_db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .addMigrations(MIGRATION_1_2)
        .build()
}
