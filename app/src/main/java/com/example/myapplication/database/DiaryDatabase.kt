package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.helper.Ultils.DATABASE_NAME
import com.example.myapplication.dao.DiaryDao
import com.example.myapplication.model.Diary

@Database(entities = [Diary::class], version = 2, exportSchema = false)
abstract class DiaryDatabase() : RoomDatabase() {
    abstract val diaryDao: DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, DiaryDatabase::class.java, DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}