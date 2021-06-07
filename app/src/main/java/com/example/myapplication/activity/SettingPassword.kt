package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySettingPasswordBinding
import com.example.myapplication.helper.Ultils
import com.example.myapplication.helper.Ultils.PASSWORD_APP_KEY
import com.example.myapplication.helper.Ultils.SHAREDPREFERENCES_KEY

class SettingPassword : AppCompatActivity() {

    private val binding: ActivitySettingPasswordBinding by lazy {
        ActivitySettingPasswordBinding.inflate(
            layoutInflater
        )
    }
    private var password: String = ""
    private var setPassword: String = ""
    private var isAgain = false
    private var isSetPassword = false
    private var isDeletePassword = false

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Setting password"
        addEventOnClickButton()
        val sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_KEY, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        sharedPreferences.getString(PASSWORD_APP_KEY, "")?.let {
            password = it
        }
        when (password) {
            "" -> {
                binding.layoutPassword.visibility = View.GONE
                binding.btnDeletePassword.visibility = View.VISIBLE
                binding.btnSettingPassword.visibility = View.VISIBLE
            }
            else -> {
                binding.tvAlert.text = "Enter password"
                binding.layoutPassword.visibility = View.VISIBLE
                binding.btnDeletePassword.visibility = View.GONE
                binding.btnSettingPassword.visibility = View.GONE
            }
        }

        binding.btnSettingPassword.setOnClickListener {
            binding.layoutPassword.visibility = View.VISIBLE
            binding.btnDeletePassword.visibility = View.GONE
            binding.btnSettingPassword.visibility = View.GONE
            isSetPassword = true
        }

        binding.btnDeletePassword.setOnClickListener {
            password = ""
            editor.putString(PASSWORD_APP_KEY, "")
            editor.apply()
        }


        binding.edtAppLock.addTextChangedListener {
            when (it?.length) {
                6 -> {
                    when {
                        isSetPassword -> {
                            when (setPassword) {
                                "" -> {
                                    setPassword = it.toString()
                                    binding.tvAlert.text = "Enter password again"
                                    binding.edtAppLock.setText("")
                                }
                                else -> when (setPassword) {
                                    it.toString() -> {
                                        editor.putString(PASSWORD_APP_KEY, setPassword)
                                        editor.apply()
                                        Toast.makeText(
                                            this,
                                            "Setting password successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.edtAppLock.setText("")
                                        binding.layoutPassword.visibility = View.GONE
                                        binding.btnDeletePassword.visibility = View.VISIBLE
                                        binding.btnSettingPassword.visibility = View.VISIBLE
                                    }
                                    else -> {
                                        binding.edtAppLock.setText("")
                                        Toast.makeText(
                                            this,
                                            "Password wrong. Try again",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                        else -> {
                            when (it.toString()) {
                                password -> {
                                    binding.layoutPassword.visibility = View.GONE
                                    binding.edtAppLock.setText("")
                                    password = ""
                                    binding.layoutPassword.visibility = View.GONE
                                    binding.btnDeletePassword.visibility = View.VISIBLE
                                    binding.btnSettingPassword.visibility = View.VISIBLE
                                }
                                else -> {
                                    binding.edtAppLock.setText("")
                                    Toast.makeText(
                                        this,
                                        "Password wrong. Try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addEventOnClickButton() {
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