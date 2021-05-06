package com.example.cmplacesapp.LocalDB

import androidx.room.*

@Dao
interface NotesDao {
   @Query("SELECT * FROM Notes")
    fun getAll(): List<Notes>
    @Insert
      fun insertAll(vararg note: Notes)
    @Delete
      fun Delete(vararg note: Notes)
    @Update
     fun Update(vararg note: Notes)


}