package com.example.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.helper.Ultils.AUTO_PASS_KEY
import com.example.myapplication.helper.Ultils.SHAREDPREFERENCES_KEY
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.helper.Ultils.PASSWORD_APP_KEY

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_KEY, MODE_PRIVATE)
        val password = sharedPreferences.getString(PASSWORD_APP_KEY,"")
        when(password){
            "" -> startActivity(Intent(this, Content::class.java))
            else -> setContentView(binding.root)
        }

        addEventOnClickButton()
        binding.edtAppLock.addTextChangedListener {
           when(it?.length){
               6 -> {
                   when(password){
                       it.toString() -> startActivity(Intent(this, Content::class.java))
                       else -> {
                           binding.edtAppLock.setText("")
                           Toast.makeText(this, "Password wrong. Try again", Toast.LENGTH_SHORT).show()
                       }
                   }
               }
           }
        }

    }

    private fun addEventOnClickButton(){
        binding.tvNumber0.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}0")
        }
        binding.tvNumber1.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}1")
        }
        binding.tvNumber2.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}2")
        }
        binding.tvNumber3.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}3")
        }
        binding.tvNumber4.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}4")
        }
        binding.tvNumber5.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}5")
        }
        binding.tvNumber6.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}6")
        }
        binding.tvNumber7.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}7")
        }
        binding.tvNumber8.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}8")
        }
        binding.tvNumber9.setOnClickListener {
            binding.edtAppLock.setText("${binding.edtAppLock.text}9")
        }
    }
}