package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Diary
import com.example.myapplication.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DiaryViewModel(private val repo: DiaryRepository) : ViewModel() {

    suspend fun insertDiary(diary: Diary): Long {
        return repo.insertDiary(diary)

    }

    fun updateDiary(diary: Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateDiary(diary)
        }
    }

    fun deleteDiary(diary: Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteDiary(diary)
        }
    }

    fun getAllDiary(): LiveData<MutableList<Diary>> {
        return repo.getAllDiary()
    }

    fun selectDiaryByTitleOrContent(someThing: String): LiveData<MutableList<Diary>> {
        return repo.selectDiaryByTitleOrContent("%$someThing%")
    }

    fun getDiaryByDate(date: String): LiveData<MutableList<Diary>> {
        return repo.getDiaryByDate(date)
    }

    fun deleteAllDiary() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            repo.deleteAllDiary()
        }
    }


    class DiaryViewModelProvide(val repo: DiaryRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
                return DiaryViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}