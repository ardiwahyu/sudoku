package com.github.sudoku.utils.setting

import com.github.sudoku.databinding.BoardFieldBinding

class BoardGrouping(binding: BoardFieldBinding) {
    private val groupByColumn = GroupByColumn(binding)
    private val groupByHorizontal = GroupByHorizontal(binding)
    private val groupByVertical = GroupByVertical(binding)

    val group = arrayListOf(
        groupByColumn.column1,
        groupByColumn.column2,
        groupByColumn.column3,
        groupByColumn.column4,
        groupByColumn.column5,
        groupByColumn.column6,
        groupByColumn.column7,
        groupByColumn.column8,
        groupByColumn.column9,
        groupByHorizontal.horizontal1,
        groupByHorizontal.horizontal2,
        groupByHorizontal.horizontal3,
        groupByHorizontal.horizontal4,
        groupByHorizontal.horizontal5,
        groupByHorizontal.horizontal6,
        groupByHorizontal.horizontal7,
        groupByHorizontal.horizontal8,
        groupByHorizontal.horizontal9,
        groupByVertical.vertical1,
        groupByVertical.vertical2,
        groupByVertical.vertical3,
        groupByVertical.vertical4,
        groupByVertical.vertical5,
        groupByVertical.vertical6,
        groupByVertical.vertical7,
        groupByVertical.vertical8,
        groupByVertical.vertical9,
    )
}