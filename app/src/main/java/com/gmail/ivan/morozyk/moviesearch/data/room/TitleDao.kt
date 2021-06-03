package com.gmail.ivan.morozyk.moviesearch.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TitleDao {

    @Query("SELECT * FROM titles WHERE `query` = :query")
    suspend fun getTitles(query: String) : List<TitleRoomDto>

    @Query("SELECT * FROM titles WHERE  id = :titleId")
    suspend fun getTitle(titleId: String): TitleRoomDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitles(titles: List<TitleRoomDto>)
}