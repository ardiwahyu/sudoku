package com.github.sudoku.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.sudoku.R
import com.github.sudoku.databinding.ActivityMainBinding
import com.github.sudoku.ui.board.BoardActivity
import com.github.sudoku.utils.setting.Level.EASY
import com.github.sudoku.utils.setting.Level.HARD
import com.github.sudoku.utils.setting.Level.MEDIUM
import com.toaster.Toaster
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var time: Long? = null
    private var level: Int? = null
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    override fun onResume() {
        super.onResume()
        resetLevel()
        resetTime()
        enableBtnNewGame()
        mainViewModel.getGameSaved()
    }

    private fun initUi() {
        binding.btn10m.setOnClickListener {
            resetTime()
            setBackground(it, R.drawable.bg_green_rouded)
            time = 10
            enableBtnNewGame()
        }
        binding.btn30m.setOnClickListener {
            resetTime()
            setBackground(it, R.drawable.bg_green_rouded)
            time = 30
            enableBtnNewGame()
        }
        binding.btn1h.setOnClickListener {
            resetTime()
            setBackground(it, R.drawable.bg_green_rouded)
            time = 60
            enableBtnNewGame()
        }
        binding.btnEasy.setOnClickListener {
            resetLevel()
            setBackground(it, R.drawable.bg_green_rouded)
            level = EASY
            enableBtnNewGame()
        }
        binding.btnMedium.setOnClickListener {
            resetLevel()
            setBackground(it, R.drawable.bg_green_rouded)
            level = MEDIUM
            enableBtnNewGame()
        }
        binding.btnHard.setOnClickListener {
            resetLevel()
            setBackground(it, R.drawable.bg_green_rouded)
            level = HARD
            enableBtnNewGame()
        }
        binding.btnNewGame.setOnClickListener {
            startActivity(intentFor<BoardActivity>(
                BoardActivity.EXTRA_TIME to time,
                BoardActivity.EXTRA_LEVEL to level
            ))
        }

        mainViewModel.game.observe(this, { game ->
            if (game == null) {
                binding.btnResume.setOnClickListener {
                    Toaster.defaultToast(this, "No games saved")
                }
            } else {
                binding.btnResume.setOnClickListener {
                    startActivity(intentFor<BoardActivity>(
                        BoardActivity.EXTRA_GAME to game
                    ))
                }
            }
        })
    }

    private fun enableBtnNewGame() {
        binding.btnNewGame.isEnabled = time != null && level != null
    }

    private fun resetTime() {
        time = null
        setBackground(binding.btn10m, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btn30m, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btn1h, R.drawable.bg_transparent_border_white_rouded)
    }

    private fun resetLevel() {
        level = null
        setBackground(binding.btnEasy, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btnMedium, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btnHard, R.drawable.bg_transparent_border_white_rouded)
    }

    private fun setBackground(view: View, int: Int) {
        view.background = ContextCompat.getDrawable(this, int)
    }
}