package com.example.cmplacesapp.LocalDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes")
    fun getAll(): List<Notes>

    @Query("SELECT * FROM Notes WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Notes>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Notes

    @Insert
    fun insertAll(vararg users: Notes)

    @Delete
    fun delete(user: Notes)
}