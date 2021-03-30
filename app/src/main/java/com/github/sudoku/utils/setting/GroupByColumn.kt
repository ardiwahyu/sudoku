package com.github.sudoku.utils.setting

import com.github.sudoku.databinding.BoardFieldBinding

class GroupByColumn(binding: BoardFieldBinding) {
    val column1 = arrayListOf(
        binding.field11,
        binding.field12,
        binding.field13,
        binding.field21,
        binding.field22,
        binding.field23,
        binding.field31,
        binding.field32,
        binding.field33
    )

    val column2 = arrayListOf(
        binding.field14,
        binding.field15,
        binding.field16,
        binding.field24,
        binding.field25,
        binding.field26,
        binding.field34,
        binding.field35,
        binding.field36
    )

    val column3 = arrayListOf(
        binding.field17,
        binding.field18,
        binding.field19,
        binding.field27,
        binding.field28,
        binding.field29,
        binding.field37,
        binding.field38,
        binding.field39
    )

    val column4 = arrayListOf(
        binding.field41,
        binding.field42,
        binding.field43,
        binding.field51,
        binding.field52,
        binding.field53,
        binding.field61,
        binding.field62,
        binding.field63
    )

    val column5 = arrayListOf(
        binding.field44,
        binding.field45,
        binding.field46,
        binding.field54,
        binding.field55,
        binding.field56,
        binding.field64,
        binding.field65,
        binding.field66
    )

    val column6 = arrayListOf(
        binding.field47,
        binding.field48,
        binding.field49,
        binding.field57,
        binding.field58,
        binding.field59,
        binding.field67,
        binding.field68,
        binding.field69
    )

    val column7 = arrayListOf(
        binding.field71,
        binding.field72,
        binding.field73,
        binding.field81,
        binding.field82,
        binding.field83,
        binding.field91,
        binding.field92,
        binding.field93
    )

    val column8 = arrayListOf(
        binding.field74,
        binding.field75,
        binding.field76,
        binding.field84,
        binding.field85,
        binding.field86,
        binding.field94,
        binding.field95,
        binding.field96
    )

    val column9 = arrayListOf(
        binding.field77,
        binding.field78,
        binding.field79,
        binding.field87,
        binding.field88,
        binding.field89,
        binding.field97,
        binding.field98,
        binding.field99
    )
}