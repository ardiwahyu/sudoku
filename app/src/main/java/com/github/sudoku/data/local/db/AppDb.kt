package com.github.sudoku.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.sudoku.data.local.db.dao.GameDao
import com.github.sudoku.data.local.db.entities.Game

@Database(
    entities = [Game::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb: RoomDatabase() {
    abstract fun gameDao(): GameDao
}