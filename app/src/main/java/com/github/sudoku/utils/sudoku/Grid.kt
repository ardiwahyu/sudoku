package com.github.sudoku.utils.sudoku

import android.util.Log
import timber.log.Timber
import java.util.*

@Suppress("NAME_SHADOWING")
class Grid private constructor(private val grid: Array<Array<Cell?>>) {
    val size: Int
        get() = grid.size

    fun getCell(row: Int, column: Int): Cell? {
        return grid[row][column]
    }

    fun isValidValueForCell(cell: Cell, value: Int): Boolean {
        return isValidInRow(cell, value) && isValidInColumn(cell, value) && isValidInBox(
            cell,
            value
        )
    }

    private fun isValidInRow(cell: Cell, value: Int): Boolean {
        return !getRowValuesOf(cell).contains(value)
    }

    private fun isValidInColumn(cell: Cell, value: Int): Boolean {
        return !getColumnValuesOf(cell).contains(value)
    }

    private fun isValidInBox(cell: Cell, value: Int): Boolean {
        return !getBoxValuesOf(cell).contains(value)
    }

    private fun getRowValuesOf(cell: Cell): Collection<Int> {
        val rowValues: MutableList<Int> = ArrayList()
        for (neighbor in cell.rowNeighbors!!) rowValues.add(neighbor.value)
        return rowValues
    }

    private fun getColumnValuesOf(cell: Cell): Collection<Int> {
        val columnValues: MutableList<Int> = ArrayList()
        for (neighbor in cell.columnNeighbors!!) columnValues.add(neighbor.value)
        return columnValues
    }

    private fun getBoxValuesOf(cell: Cell): Collection<Int> {
        val boxValues: MutableList<Int> = ArrayList()
        for (neighbor in cell.boxNeighbors!!) boxValues.add(neighbor.value)
        return boxValues
    }

    val firstEmptyCell: Optional<Cell>
        get() {
            val firstCell = grid[0][0]
            return if (firstCell!!.isEmpty) {
                Optional.of(firstCell)
            } else getNextEmptyCellOf(firstCell)
        }

    fun getNextEmptyCellOf(cell: Cell?): Optional<Cell> {
        var cell = cell
        var nextEmptyCell: Cell? = null
        while (cell!!.nextCell.also { cell = it } != null) {
            if (!cell!!.isEmpty) {
                continue
            }
            nextEmptyCell = cell
            break
        }
        return Optional.ofNullable(nextEmptyCell)
    }

    override fun toString(): String {
        return StringConverter.toString(this)
    }

    fun isEmptyCell(): Boolean {
        for (i in 0..8) {
            for (j in 0..8) {
                if (this.getCell(i,j)?.value == 0) {
                    return true
                }
            }
        }
        return false
    }

    class Cell(var value: Int) {
        var rowNeighbors: Collection<Cell>? = null
        var columnNeighbors: Collection<Cell>? = null
        var boxNeighbors: Collection<Cell>? = null
        var nextCell: Cell? = null
        val isEmpty: Boolean
            get() = value == 0
    }

    private object StringConverter {
        fun toString(grid: Grid): String {
            val builder = StringBuilder()
            val size = grid.size
            printTopBorder(builder)
            for (row in 0 until size) {
                printRowBorder(builder)
                for (column in 0 until size) {
                    printValue(builder, grid, row, column)
                    printRightColumnBorder(builder, column + 1, size)
                }
                printRowBorder(builder)
                builder.append("\n")
                printBottomRowBorder(builder, row + 1, size)
            }
            printBottomBorder(builder)
            return builder.toString()
        }

        private fun printTopBorder(builder: StringBuilder) {
            builder.append("╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗\n")
        }

        private fun printRowBorder(builder: StringBuilder) {
            builder.append("║")
        }

        private fun printValue(builder: StringBuilder, grid: Grid, row: Int, column: Int) {
            val value = grid.getCell(row, column)!!.value
            val output = if (value != 0) value.toString() else " "
            builder.append(" $output ")
        }

        private fun printRightColumnBorder(builder: StringBuilder, column: Int, size: Int) {
            if (column == size) {
                return
            }
            if (column % 3 == 0) {
                builder.append("║")
            } else {
                builder.append("│")
            }
        }

        private fun printBottomRowBorder(builder: StringBuilder, row: Int, size: Int) {
            if (row == size) {
                return
            }
            if (row % 3 == 0) {
                builder.append("╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣\n")
            } else {
                builder.append("╟───┼───┼───╫───┼───┼───╫───┼───┼───╢\n")
            }
        }

        private fun printBottomBorder(builder: StringBuilder) {
            builder.append("╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝\n")
        }
    }

    companion object {
        fun of(grid: Array<IntArray>): Grid {
            verifyGrid(grid)
            val cells = Array(9) { arrayOfNulls<Cell>(9) }
            val rows: MutableList<MutableList<Cell>> = ArrayList()
            val columns: MutableList<MutableList<Cell>> = ArrayList()
            val boxes: MutableList<MutableList<Cell>> = ArrayList()
            for (i in 0..8) {
                rows.add(ArrayList())
                columns.add(ArrayList())
                boxes.add(ArrayList())
            }
            var lastCell: Cell? = null
            for (row in grid.indices) {
                for (column in grid[row].indices) {
                    val cell = Cell(grid[row][column])
                    cells[row][column] = cell
                    rows[row].add(cell)
                    columns[column].add(cell)
                    boxes[row / 3 * 3 + column / 3].add(cell)
                    if (lastCell != null) {
                        lastCell.nextCell = cell
                    }
                    lastCell = cell
                }
            }
            for (i in 0..8) {
                val row: List<Cell> = rows[i]
                for (cell in row) {
                    val rowNeighbors: MutableList<Cell> = ArrayList(row)
                    rowNeighbors.remove(cell)
                    cell.rowNeighbors = rowNeighbors
                }
                val column: List<Cell> = columns[i]
                for (cell in column) {
                    val columnNeighbors: MutableList<Cell> = ArrayList(column)
                    columnNeighbors.remove(cell)
                    cell.columnNeighbors = columnNeighbors
                }
                val box: List<Cell> = boxes[i]
                for (cell in box) {
                    val boxNeighbors: MutableList<Cell> = ArrayList(box)
                    boxNeighbors.remove(cell)
                    cell.boxNeighbors = boxNeighbors
                }
            }
            return Grid(cells)
        }

        fun of(string: String): Grid {
            val row1 = IntArray(9)
            val row2 = IntArray(9)
            val row3 = IntArray(9)
            val row4 = IntArray(9)
            val row5 = IntArray(9)
            val row6 = IntArray(9)
            val row7 = IntArray(9)
            val row8 = IntArray(9)
            val row9 = IntArray(9)
            for (i in 0..8) {
                row1[i] = if (string[i] == '.') 0 else string[i].toString().toInt()
                row2[i] = if (string[9 + i] == '.') 0 else string[9 + i].toString().toInt()
                row3[i] = if (string[2 * 9 + i] == '.') 0 else string[2 * 9 + i].toString().toInt()
                row4[i] = if (string[3 * 9 + i] == '.') 0 else string[3 * 9 + i].toString().toInt()
                row5[i] = if (string[4 * 9 + i] == '.') 0 else string[4 * 9 + i].toString().toInt()
                row6[i] = if (string[5 * 9 + i] == '.') 0 else string[5 * 9 + i].toString().toInt()
                row7[i] = if (string[6 * 9 + i] == '.') 0 else string[6 * 9 + i].toString().toInt()
                row8[i] = if (string[7 * 9 + i] == '.') 0 else string[7 * 9 + i].toString().toInt()
                row9[i] = if (string[8 * 9 + i] == '.') 0 else string[8 * 9 + i].toString().toInt()
            }
            val array: Array<IntArray> = arrayOf(row1, row2, row3, row4, row5, row6, row7, row8, row9)
            return of(array)
        }

        fun emptyGrid(): Grid {
            val emptyGrid = Array(9) { IntArray(9) }
            return of(emptyGrid)
        }

        private fun verifyGrid(grid: Array<IntArray>?) {
            requireNotNull(grid) { "grid tidak boleh null" }
            require(grid.size == 9) { "grid harus 9 baris" }
            for (row in grid) {
                require(row.size == 9) { "grid harus 9 kolom" }
                for (value in row) {
                    require(!(value < 0 || value > 9)) { "isian grid harus antara 0-9" }
                }
            }
        }
    }
}