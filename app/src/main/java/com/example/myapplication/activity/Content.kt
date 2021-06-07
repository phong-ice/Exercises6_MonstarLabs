package com.example.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.fragment.DiaryFragment

class Content : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        addFragment(DiaryFragment(),DiaryFragment.NAME_FRAGMENT)
        supportActionBar?.title = "Diary"
    }

    private fun addFragment(fr: Fragment, tag:String){
       val transaction = supportFragmentManager.beginTransaction()
       transaction.replace(R.id.content_fragment,fr,tag)
       transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_menu_diary -> addFragment(DiaryFragment(),DiaryFragment.NAME_FRAGMENT)
            R.id.item_menu_calendar -> startActivity(Intent(this, CalendarActivity::class.java))
            R.id.item_menu_setting -> startActivity(Intent(this,Setting::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}