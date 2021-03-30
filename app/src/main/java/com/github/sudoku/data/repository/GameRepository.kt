package com.github.sudoku.data.repository

import com.github.sudoku.data.local.db.dao.GameDao
import com.github.sudoku.data.local.db.entities.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameDao: GameDao
) {
    suspend fun getGameSaved(): Game? {
        return withContext(Dispatchers.IO) {
            return@withContext gameDao.getGame()
        }
    }

    suspend fun saveGame(game: Game) {
        return withContext(Dispatchers.IO) {
            gameDao.clearGame()
            gameDao.insert(game)
        }
    }

    suspend fun deleteGame() {
        return withContext(Dispatchers.IO) {
            gameDao.clearGame()
        }
    }
}