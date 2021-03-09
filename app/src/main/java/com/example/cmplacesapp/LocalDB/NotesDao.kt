package com.example.cmplacesapp.LocalDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
   @Query("SELECT * FROM Notes")
    fun getAll(): List<Notes>
    @Insert
      fun insertAll(vararg note: Notes)
    @Delete
      fun Delete(vararg note: Notes)


}