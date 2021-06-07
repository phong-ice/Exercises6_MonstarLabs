package com.example.myapplication.adapter

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.myapplication.callback.CommunicationCalendarFragmentTwo
import com.example.myapplication.helper.Ultils
import com.example.myapplication.fragment.MonthFragment

class AdapterViewPager(fr:FragmentManager,private val listener: CommunicationCalendarFragmentTwo):FragmentStatePagerAdapter(fr,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return Ultils.COUNT_PAG
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItem(position: Int): Fragment {
        val fragment = MonthFragment(listener)
        val bundle = Bundle()
        bundle.putString(MonthFragment.ARG_PARAM1,"$position")
        fragment.arguments = bundle
        return fragment
    }
}