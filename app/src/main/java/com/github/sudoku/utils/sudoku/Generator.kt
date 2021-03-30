package com.github.sudoku.utils.sudoku

import java.util.*

class Generator {
    private val solver: Solver = Solver()

    fun generate(numberOfEmptyCells: Int): Grid {
        val grid = generate()
        eraseCells(grid, numberOfEmptyCells)
        return grid
    }

    private fun eraseCells(grid: Grid, numberOfEmptyCells: Int) {
        val random = Random()
        var i = 0
        while (i < numberOfEmptyCells) {
            val randomRow = random.nextInt(9)
            val randomColumn = random.nextInt(9)
            val cell = grid.getCell(randomRow, randomColumn)
            if (!cell?.isEmpty!!) {
                cell.value = 0
            } else {
                i--
            }
            i++
        }
    }

    private fun generate(): Grid {
        val grid = Grid.emptyGrid()
        solver.solve(grid)
        return grid
    }

}