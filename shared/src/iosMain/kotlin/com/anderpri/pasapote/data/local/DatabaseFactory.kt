package com.anderpri.pasapote.data.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getIosDatabase(): AppDatabase {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!
    val dbFilePath = "${documentDirectory.path}/app_db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
        .setDriver(BundledSQLiteDriver())
        .addMigrations(MIGRATION_1_2)
        .build()
}
