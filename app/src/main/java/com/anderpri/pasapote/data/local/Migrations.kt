package com.anderpri.pasapote.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM konpartsa")
        db.execSQL("ALTER TABLE konpartsa ADD COLUMN posX REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE konpartsa ADD COLUMN posY REAL NOT NULL DEFAULT 0.0")
    }
}