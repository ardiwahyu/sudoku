package com.github.sudoku.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.sudoku.R
import com.github.sudoku.databinding.ActivityMainBinding
import com.github.sudoku.ui.board.BoardActivity
import com.github.sudoku.utils.setting.Level
import com.github.sudoku.utils.setting.Level.EASY
import com.github.sudoku.utils.setting.Level.HARD
import com.github.sudoku.utils.setting.Level.MEDIUM
import com.toaster.Toaster
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var time: Int? = null
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
        setTime(null, null)
        setLevel(null, null)
        mainViewModel.getGameSaved()
    }

    private fun initUi() {
        binding.btn10m.setOnClickListener { setTime(10, it) }
        binding.btn30m.setOnClickListener { setTime(30, it) }
        binding.btn1h.setOnClickListener { setTime(60, it) }
        binding.btnEasy.setOnClickListener { setLevel(EASY, it) }
        binding.btnMedium.setOnClickListener { setLevel(MEDIUM, it) }
        binding.btnHard.setOnClickListener { setLevel(HARD, it) }
        binding.btnNewGame.setOnClickListener {
            startActivity(intentFor<BoardActivity>(
                BoardActivity.EXTRA_TIME to time,
                BoardActivity.EXTRA_LEVEL to level
            ))
        }

        mainViewModel.game.observe(this) { game ->
            binding.btnResume.setOnClickListener {
                if (game == null) Toaster.defaultToast(this, "No games saved")
                else startActivity(intentFor<BoardActivity>(BoardActivity.EXTRA_GAME to game ))
            }
        }
    }

    private fun setTime(time: Int?, view: View?) {
        this.time = time
        setBackground(binding.btn10m, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btn30m, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btn1h, R.drawable.bg_transparent_border_white_rouded)
        view?.let { setBackground(it, R.drawable.bg_green_rouded) }
        binding.btnNewGame.isEnabled = time != null && level != null
    }

    private fun setLevel(level: Int?,view: View?) {
        this.level = level
        setBackground(binding.btnEasy, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btnMedium, R.drawable.bg_transparent_border_white_rouded)
        setBackground(binding.btnHard, R.drawable.bg_transparent_border_white_rouded)
        view?.let { setBackground(it, R.drawable.bg_green_rouded) }
        binding.btnNewGame.isEnabled = time != null && level != null
    }

    private fun setBackground(view: View, int: Int) {
        view.background = ContextCompat.getDrawable(this, int)
    }
}