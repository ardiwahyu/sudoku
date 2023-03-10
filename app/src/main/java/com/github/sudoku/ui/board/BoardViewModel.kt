package com.github.sudoku.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sudoku.data.local.db.entities.Game
import com.github.sudoku.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    fun saveGame(game: Game) {
        viewModelScope.launch {
            gameRepository.saveGame(game)
        }
    }

    fun deleteGame() {
        viewModelScope.launch {
            gameRepository.deleteGame()
        }
    }
}