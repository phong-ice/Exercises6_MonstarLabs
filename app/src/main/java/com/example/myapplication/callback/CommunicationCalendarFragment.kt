package com.example.myapplication.callback

interface CommunicationCalendarFragment {
    fun getDate(day:String):String
    fun itemOnClick(date:String)
    fun itemDoubleClick(date:String)
}