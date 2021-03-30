package com.github.sudoku.utils.setting

import com.github.sudoku.databinding.BoardInputBinding

class GroupInput(binding: BoardInputBinding) {
    val input = arrayListOf(
        binding.input1,
        binding.input2,
        binding.input3,
        binding.input4,
        binding.input5,
        binding.input6,
        binding.input7,
        binding.input8,
        binding.input9
    )
}