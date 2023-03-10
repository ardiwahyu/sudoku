package com.github.sudoku.ui.board

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sudoku.R
import com.github.sudoku.data.local.db.entities.Game
import com.github.sudoku.databinding.*
import com.github.sudoku.utils.setting.SettingGame
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardActivity : AppCompatActivity(), OnCompleteListener {
    companion object {
        const val EXTRA_TIME = "extra_time"
        const val EXTRA_LEVEL = "extra_level"
        const val EXTRA_GAME = "extra_game"
    }
    private lateinit var binding: ActivityBoardBinding
    private lateinit var timer: CountDownTimer
    private lateinit var settingGame: SettingGame
    private var milliLeft = 0L
    private val boardViewModel: BoardViewModel by viewModels()
    private var resolve = false
    private var time: Long = 0
    private var level: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        time = intent.getLongExtra(EXTRA_TIME, 10)

        val boardFieldBinding = binding.field
        val boardInputBinding = binding.input
        settingGame = SettingGame(this, boardFieldBinding, boardInputBinding)

        if (intent.hasExtra(EXTRA_GAME)) {
            val game = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_GAME, Game::class.java)
            } else {
                intent.getParcelableExtra(EXTRA_GAME)
            }
            settingGame.setGame(game!!)
            timerStart(game.time!!)
            level = game.level!!
        } else {
            level = intent.getIntExtra(EXTRA_LEVEL, 0)
            settingGame.setGame(level)
            timerStart(time * 60 * 1000)
        }

        binding.btnNewGame.setOnClickListener {
            if (!resolve) {
                timerPause()
                val dialog = Dialog(this)
                val bindingDialog = DialogNewGameBinding.inflate(layoutInflater)
                dialog.setContentView(bindingDialog.root)
                dialog.setCancelable(false)
                bindingDialog.tvCreate.setOnClickListener {
                    boardViewModel.deleteGame()
                    dialog.cancel()
                    resolve = false
                    settingGame.setGame(level)
                    timerStart(time * 60 * 1000)
                    setOnClickPause()
                    setOnClickSolve()
                }
                bindingDialog.btnCancel.setOnClickListener {
                    timerResume()
                    dialog.dismiss()
                }
                dialog.show()
            } else {
                boardViewModel.deleteGame()
                settingGame.setGame(level)
                resolve = false
                timerStart(time * 60 * 1000)
                setOnClickPause()
                setOnClickSolve()
            }
        }

        setOnClickPause()
        setOnClickSolve()
    }

    private fun createObjectGame(): Game {
        return Game(
            null,
            milliLeft,
            settingGame.getStringField(),
            level,
            settingGame.getStringOrigin()
        )
    }

    private fun timerStart(time: Long) {
        timer = object : CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(milliTillFinish: Long) {
                milliLeft = milliTillFinish
                val hour = milliTillFinish/(1000*60*60)
                val stringHour = if (hour.toString().length == 1) "0$hour" else hour
                val milliForMinute = milliTillFinish - hour*(1000*60*60)
                val min = milliForMinute/(1000*60)
                val stringMin = if (min.toString().length == 1) "0$min" else min
                val milliForSecond = milliForMinute - min*(1000*60)
                val sec = milliForSecond/1000
                val stringSec = if (sec.toString().length == 1) "0$sec" else sec
                binding.tvTimer.text = "$stringHour:$stringMin:$stringSec"
            }

            override fun onFinish() {
                val dialog = Dialog(this@BoardActivity)
                val bindingDialog = DialogTimeOutBinding.inflate(layoutInflater)
                dialog.setContentView(bindingDialog.root)
                dialog.setCancelable(false)
                bindingDialog.tvOk.setOnClickListener {
                    boardViewModel.deleteGame()
                    dialog.cancel()
                    finish()
                }
                dialog.show()
            }
        }
        timer.start()
    }

    private fun timerPause() {
        timer.cancel()
    }

    private fun timerResume() {
        timerStart(milliLeft)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onComplete() {
        timerPause()
        resolve = true
        val dialog = Dialog(this)
        val bindingDialog = DialogWinBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.setCancelable(false)
        binding.ivPause.setOnClickListener(null)
        binding.btnSolveMe.setOnClickListener(null)
        bindingDialog.tvClose.setOnClickListener {
            dialog.cancel()
        }
        bindingDialog.btnPlay.setOnClickListener {
            boardViewModel.deleteGame()
            dialog.cancel()
            resolve = false
            settingGame.setGame(level)
            timerStart(time * 60 * 1000)
            setOnClickPause()
            setOnClickSolve()
        }
        dialog.show()
    }

    private fun setOnClickPause() {
        binding.ivPause.setOnClickListener {
            timerPause()
            val bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
            val bindingBottomSheet = DialogBottomPausedBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(bindingBottomSheet.root)
            bottomSheetDialog.setCancelable(false)
            bindingBottomSheet.tvContinue.setOnClickListener {
                bottomSheetDialog.dismiss()
                timerResume()
            }
            bindingBottomSheet.tvSave.setOnClickListener {
                boardViewModel.saveGame(createObjectGame())
                bottomSheetDialog.cancel()
                finish()
            }
            bottomSheetDialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOnClickSolve() {
        binding.btnSolveMe.setOnClickListener {
            timerPause()
            val dialogSolve = Dialog(this)
            val bindingDialogSolve = DialogSolveMeBinding.inflate(layoutInflater)
            dialogSolve.setContentView(bindingDialogSolve.root)
            dialogSolve.setCancelable(false)
            bindingDialogSolve.tvSolve.setOnClickListener {
                timerPause()
                binding.tvTimer.text = "00:00:00"
                settingGame.resolveGame()
                binding.ivPause.setOnClickListener(null)
                binding.btnSolveMe.setOnClickListener(null)
                resolve = true
                dialogSolve.dismiss()
            }
            bindingDialogSolve.btnContinue.setOnClickListener {
                timerResume()
                dialogSolve.dismiss()
            }
            dialogSolve.show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (resolve) {
            super.onBackPressed()
        } else {
            timerPause()
            val bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
            val bindingBottomSheet = DialogBottomPausedBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(bindingBottomSheet.root)
            bottomSheetDialog.setCancelable(false)
            bindingBottomSheet.tvContinue.setOnClickListener {
                bottomSheetDialog.dismiss()
                timerResume()
            }
            bindingBottomSheet.tvSave.setOnClickListener {
                boardViewModel.saveGame(createObjectGame())
                bottomSheetDialog.cancel()
                finish()
            }
            bottomSheetDialog.show()
        }
    }
}