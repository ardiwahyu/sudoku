package com.github.sudoku.ui.board

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sudoku.data.local.db.entities.Game
import com.github.sudoku.data.repository.GameRepository
import kotlinx.coroutines.launch

class BoardViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
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