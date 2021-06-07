package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.model.Diary

@Dao
interface DiaryDao {

    @Insert
    fun insertDiary(diary:Diary):Long

    @Update
    fun updateDiary(diary: Diary)

    @Delete
    fun deleteDiary(diary: Diary)

    @Query("SELECT * FROM table_name_diary")
    fun getAllDiary():LiveData<MutableList<Diary>>

    @Query("SELECT * FROM table_name_diary WHERE titleDiary LIKE :someThing OR contentDiary LIKE :someThing")
    fun selectDiaryByTitleOrContent(someThing:String):LiveData<MutableList<Diary>>

    @Query("SELECT * FROM table_name_diary WHERE dateDiary = :date")
    fun getDiaryByDate(date:String):LiveData<MutableList<Diary>>

    @Query("DELETE FROM table_name_diary")
    fun deleteAllDiary()
}