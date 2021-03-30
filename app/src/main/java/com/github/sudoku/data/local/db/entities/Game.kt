package com.github.sudoku.data.local.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "game")
class Game (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "time") var time: Long?,
    @ColumnInfo(name = "grid") val grid: String?,
    @ColumnInfo(name = "level") var level: Int?,
    @ColumnInfo(name = "original_grid") val originalGrid: String?
): Parcelable