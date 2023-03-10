package com.github.sudoku.di

import android.content.Context
import androidx.room.Room
import com.github.sudoku.data.local.db.AppDb
import com.github.sudoku.data.local.db.dao.GameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "Sudoku.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideGameDao(db: AppDb): GameDao {
        return db.gameDao()
    }
}