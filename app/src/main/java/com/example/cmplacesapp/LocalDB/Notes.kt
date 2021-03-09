package com.example.cmplacesapp.LocalDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "Title") val Title: String?,
    @ColumnInfo(name = "Description") val Description: String?,
    @ColumnInfo(name = "DateAdd") val DateAdd: String?,
    @ColumnInfo(name = "DateMod") val DateMod: String?

)