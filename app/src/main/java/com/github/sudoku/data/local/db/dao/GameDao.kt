package com.github.sudoku.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.sudoku.data.local.db.entities.Game

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Query("SELECT * FROM game")
    suspend fun getGame(): Game?

    @Query("DELETE FROM game")
    suspend fun clearGame()
}