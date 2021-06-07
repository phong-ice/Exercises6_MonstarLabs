package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.helper.Ultils.TABLE_NAME_DIARY
import java.sql.Date
import java.util.*

@Entity(tableName = TABLE_NAME_DIARY)
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val idDiary:Int,
    val titleDiary:String,
    val contentDiary:String,
    val dateDiary:String,
)