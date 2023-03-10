package com.github.sudoku.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sudoku.data.local.db.entities.Game
import com.github.sudoku.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    val game = MutableLiveData<Game?>()
    fun getGameSaved() {
        viewModelScope.launch {
            game.postValue(gameRepository.getGameSaved())
        }
    }
}