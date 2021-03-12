package com.example.cmplacesapp.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Notes::class), version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}
