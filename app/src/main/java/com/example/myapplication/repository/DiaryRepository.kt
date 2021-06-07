package com.example.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myapplication.dao.DiaryDao
import com.example.myapplication.database.DiaryDatabase
import com.example.myapplication.model.Diary

class DiaryRepository(application: Application) {
    private val diaryDao: DiaryDao

    init {
        val diaryDatabase: DiaryDatabase = DiaryDatabase.getInstance(application)
        diaryDao = diaryDatabase.diaryDao
    }

    suspend fun insertDiary(diary: Diary):Long = diaryDao.insertDiary(diary)
    suspend fun updateDiary(diary: Diary) = diaryDao.updateDiary(diary)
    suspend fun deleteDiary(diary: Diary) = diaryDao.deleteDiary(diary)
    fun getAllDiary(): LiveData<MutableList<Diary>> = diaryDao.getAllDiary()
    fun getDiaryByDate(date: String): LiveData<MutableList<Diary>> = diaryDao.getDiaryByDate(date)
    fun selectDiaryByTitleOrContent(someThing: String): LiveData<MutableList<Diary>> =
        diaryDao.selectDiaryByTitleOrContent(someThing)
    suspend fun deleteAllDiary() = diaryDao.deleteAllDiary()

}