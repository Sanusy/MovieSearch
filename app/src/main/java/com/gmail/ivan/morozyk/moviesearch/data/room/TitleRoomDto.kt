package com.gmail.ivan.morozyk.moviesearch.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "titles")
data class TitleRoomDto(
    @PrimaryKey val id: String,
    val name: String,
    val type: String?,
    val year: String?,
    val image: String?,
    val rating: String?,
    val query: String
)