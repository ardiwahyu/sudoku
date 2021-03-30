package com.github.sudoku.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sudoku.data.local.db.entities.Game
import com.github.sudoku.data.repository.GameRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository
): ViewModel() {

    val game = MutableLiveData<Game?>()
    fun getGameSaved() {
        viewModelScope.launch {
            game.postValue(gameRepository.getGameSaved())
        }
    }
}