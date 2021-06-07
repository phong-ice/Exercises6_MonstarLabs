package com.example.myapplication.activity

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.helper.Ultils
import com.example.myapplication.adapter.AdapterViewPager
import com.example.myapplication.callback.CommunicationCalendarFragmentTwo
import com.example.myapplication.databinding.FragmentCalendarragmentBinding
import com.example.myapplication.fragment.DiaryByCalendarFragment

class CalendarActivity : AppCompatActivity(),CommunicationCalendarFragmentTwo {

    private val binding: FragmentCalendarragmentBinding by lazy {
        FragmentCalendarragmentBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var pagerAdapter:AdapterViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Calendar"
        pagerAdapter = AdapterViewPager(supportFragmentManager,this)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.currentItem = Ultils.COUNT_PAG / 2
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun addFragment(date: String) {
        val fragment = DiaryByCalendarFragment()
        val bundle = Bundle()
        bundle.putString(DiaryByCalendarFragment.ARGUMENTS_1,date)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.content_fragment_calendar,fragment)
        transaction.addToBackStack(DiaryByCalendarFragment.FRAGMENT_NAME)
        transaction.commit()
    }


}