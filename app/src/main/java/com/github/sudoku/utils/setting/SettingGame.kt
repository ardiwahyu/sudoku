package com.github.sudoku.utils.setting

import android.app.Activity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.github.sudoku.R
import com.github.sudoku.data.local.db.entities.Game
import com.github.sudoku.databinding.BoardFieldBinding
import com.github.sudoku.databinding.BoardInputBinding
import com.github.sudoku.ui.board.OnCompleteListener
import com.github.sudoku.utils.sudoku.Generator
import com.github.sudoku.utils.sudoku.Grid
import com.github.sudoku.utils.sudoku.Solver
import org.jetbrains.anko.backgroundDrawable

class SettingGame(private val activity: Activity, private val binding: BoardFieldBinding, private val inputBinding: BoardInputBinding) {
    private val boardGrouping = BoardGrouping(binding)
    private val groupInput = GroupInput(inputBinding)
    private lateinit var gridGenerate: Grid
    private var invalid = true

    fun setGame(level: Int) {
        gridGenerate = Generator().generate(level)
        val groupByHorizontal = GroupByHorizontal(binding)
        for ((i, array) in groupByHorizontal.arrayHorizon.withIndex()) {
            for ((j, textView) in array.withIndex()) {
                val value = gridGenerate.getCell(i, j)?.value.toString()
                if (value != "0") {
                    textView.text = value
                    textView.typeface = ResourcesCompat.getFont(textView.context, R.font.nunito_black)
                } else {
                    textView.text = ""
                    textView.typeface = ResourcesCompat.getFont(textView.context, R.font.nunito_regular)
                    textView.setOnClickListener {
                        removeAllBackgroundFromBoard()
                        setBackground(textView)
                        if (textView.text.isNullOrBlank()) {
                            removeAllBackgroundFromInput()
                        } else {
                            markInput(textView.text.toString().toInt())
                        }
                        requestInput(textView)
                    }
                }
            }
        }
        removeAllBackgroundFromBoard()
        removeAllBackgroundFromInput()
    }

    fun setGame(game: Game) {
        val groupByHorizontal = GroupByHorizontal(binding)
        val gridOrigin = game.originalGrid
        val grid = game.grid
        var i = 0
        groupByHorizontal.arrayHorizon.forEach {
            it.forEach { textView ->
                if (gridOrigin!![i] == '.') {
                    if (grid!![i] != '.') {
                        textView.text = grid[i].toString()
                    }
                    textView.setOnClickListener {
                        removeAllBackgroundFromBoard()
                        setBackground(textView)
                        if (textView.text.isNullOrBlank()) {
                            removeAllBackgroundFromInput()
                        } else {
                            markInput(textView.text.toString().toInt())
                        }
                        requestInput(textView)
                    }
                } else {
                    textView.text = gridOrigin[i].toString()
                    textView.typeface = ResourcesCompat.getFont(textView.context, R.font.nunito_black)
                }
                i++
            }
        }
        gridGenerate = Grid.of(gridOrigin!!)
    }

    fun resolveGame() {
        while (gridGenerate.isEmptyCell()) {
            Solver().solve(gridGenerate)
        }
        val groupByHorizontal = GroupByHorizontal(binding)
        resetColorCell()
        for ((i, array) in groupByHorizontal.arrayHorizon.withIndex()) {
            for ((j, textView) in array.withIndex()) {
                val value = gridGenerate.getCell(i, j)?.value.toString()
                textView.text = value
            }
        }
        removeAllOnClickListener()
    }

    private fun removeAllBackgroundFromBoard() {
        boardGrouping.group.forEach { textViewArray ->
            textViewArray.forEach {
                it.background = null
            }
        }
    }

    private fun requestInput(textView: TextView) {
        groupInput.input.forEach { tvInput ->
            tvInput.setOnClickListener {
                invalid = false
                if (tvInput.backgroundDrawable == null) {
                    removeAllBackgroundFromInput()
                    setBackground(tvInput)
                    resetColorCell()
                    textView.text = tvInput.text
                    checkInvalidBoard()
                    if (!invalid) {
                        if (checkAllBoardIsComplete()) {
                            val listener = activity as OnCompleteListener
                            listener.onComplete()
                            removeAllOnClickListener()
                        }
                    }
                } else {
                    textView.text = ""
                    tvInput.backgroundDrawable = null
                    resetColorCell()
                    checkInvalidBoard()
                }
            }
        }
    }

    private fun checkAllBoardIsComplete(): Boolean {
        boardGrouping.group.forEach {
            it.forEach { textView ->
                if (textView.text.isNullOrEmpty()) {
                    return false
                }
            }
        }
        return true
    }

    private fun removeAllBackgroundFromInput() {
        groupInput.input.forEach {
            it.background = null
        }
    }

    private fun markInput(int: Int) {
        removeAllBackgroundFromInput()
        when(int) {
            1 -> setBackground(inputBinding.input1)
            2 -> setBackground(inputBinding.input2)
            3 -> setBackground(inputBinding.input3)
            4 -> setBackground(inputBinding.input4)
            5 -> setBackground(inputBinding.input5)
            6 -> setBackground(inputBinding.input6)
            7 -> setBackground(inputBinding.input7)
            8 -> setBackground(inputBinding.input8)
            9 -> setBackground(inputBinding.input9)
        }
    }

    private fun setBackground(textView: TextView) {
        textView.background = ContextCompat.getDrawable(textView.context, R.drawable.bg_transparent_border_rouded)
    }

    private fun checkInvalidBoard() {
        boardGrouping.group.forEach {
            it.forEach { textView ->
                getInvalidCell(textView)
            }
        }
    }

    private fun getInvalidCell(textView: TextView) {
        val group = arrayListOf<ArrayList<TextView>>()
        val cell = arrayListOf<TextView>()
        boardGrouping.group.forEach {
            if (it.contains(textView)) group.add(it)
        }
        group.forEach { arrayListTextView ->
            arrayListTextView.forEach {
                if (it.text == textView.text && it != textView) {
                    cell.add(it)
                }
            }
        }
        if (cell.size > 0) {
            invalid = true
            cell.forEach {
                it.setTextColor(ContextCompat.getColor(textView.context, R.color.design_default_color_error))
            }
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.design_default_color_error))
        }
    }

    private fun resetColorCell() {
        boardGrouping.group.forEach {
            it.forEach { textView ->
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorWhite))
            }
        }
    }

    fun getStringField(): String {
        var string = ""
        val groupByHorizontal = GroupByHorizontal(binding)
        groupByHorizontal.arrayHorizon.forEach {
            it.forEach { textView ->
                string += if (textView.text.isEmpty()) "." else textView.text.toString()
            }
        }
        return string
    }

    fun getStringOrigin(): String {
        var string = ""
        for (i in 0..8) {
            for (j in 0..8) {
                string += if (gridGenerate.getCell(i,j)!!.isEmpty) "." else gridGenerate.getCell(i,j)!!.value.toString()
            }
        }
        return string
    }

    private fun removeAllOnClickListener() {
        boardGrouping.group.forEach {
            it.forEach { textView ->
                textView.setOnClickListener(null)
            }
        }
        groupInput.input.forEach {
            it.setOnClickListener(null)
        }
    }
}