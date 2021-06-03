package com.gmail.ivan.morozyk.moviesearch.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TitleRoomDto::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun titleDao(): TitleDao
}