package com.anderpri.pasapote.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("DELETE FROM konpartsa")
        connection.execSQL("ALTER TABLE konpartsa ADD COLUMN posX REAL NOT NULL DEFAULT 0.0")
        connection.execSQL("ALTER TABLE konpartsa ADD COLUMN posY REAL NOT NULL DEFAULT 0.0")
    }
}
