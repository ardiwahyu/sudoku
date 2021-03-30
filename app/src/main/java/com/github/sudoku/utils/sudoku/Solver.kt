package com.github.sudoku.utils.sudoku

import java.util.*

class Solver {
    private val values: IntArray

    fun solve(grid: Grid) {
        val solvable = solve(grid, grid.firstEmptyCell)
        check(solvable) { "Grid tidak bisa diselesaikan" }
    }

    private fun solve(grid: Grid, cell: Optional<Grid.Cell>): Boolean {
        if (!cell.isPresent) {
            return true
        }
        for (value in values) {
            if (grid.isValidValueForCell(cell.get(), value)) {
                cell.get().value = value
                if (solve(grid, grid.getNextEmptyCellOf(cell.get()))) return true
                cell.get().value = EMPTY
            }
        }
        return false
    }

    private fun generateRandomValues(): IntArray {
        val values = intArrayOf(EMPTY, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val random = Random()
        var i = 0
        var j = random.nextInt(9)
        var tmp = values[j]
        while (i < values.size) {
            if (i == j) {
                i++
                j = random.nextInt(9)
                tmp = values[j]
                continue
            }
            values[j] = values[i]
            values[i] = tmp
            i++
            j = random.nextInt(9)
            tmp = values[j]
        }
        return values
    }

    companion object {
        private const val EMPTY = 0
    }

    init {
        values = generateRandomValues()
    }
}