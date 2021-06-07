package com.example.myapplication.activity

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.helper.Ultils.DIARY_CONTENT
import com.example.myapplication.helper.Ultils.DIARY_DATE
import com.example.myapplication.helper.Ultils.DIARY_ID
import com.example.myapplication.helper.Ultils.DIARY_TITLE
import com.example.myapplication.databinding.ActivityDetailsDiaryBinding
import com.example.myapplication.model.Diary
import com.example.myapplication.repository.DiaryRepository
import com.example.myapplication.viewmodel.DiaryViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailsDiary : AppCompatActivity() {

    private val binding: ActivityDetailsDiaryBinding by lazy {
        ActivityDetailsDiaryBinding.inflate(
            layoutInflater
        )
    }
    private val repo: DiaryRepository by lazy { DiaryRepository(application) }
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelProvide(repo)
        )[DiaryViewModel::class.java]
    }
    private lateinit var diaryId: String
    lateinit var setListener: DatePickerDialog.OnDateSetListener

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Detail"

        val calendar = Calendar.getInstance()
        val yearNow = calendar.get(Calendar.YEAR)
        val monthNow = calendar.get(Calendar.MONTH)
        val dayNow = calendar.get(Calendar.DAY_OF_MONTH)

        binding.tvDate.text = SimpleDateFormat("d/M/yyyy").format(Calendar.getInstance().time)
        intent.getStringExtra(DIARY_ID)?.let {
            diaryId = it
        }
        val bundle = intent.extras
        binding.edtTitle.setText(bundle?.getString(DIARY_TITLE))
        binding.edtContentDiary.setText(bundle?.getString(DIARY_CONTENT))
        when (bundle?.getString(DIARY_DATE)) {
            "" -> {}
            else -> binding.tvDate.text = bundle?.getString(DIARY_DATE)
        }

        binding.edtTitle.addTextChangedListener {
            val content = binding.edtContentDiary.text.toString()
            val date = binding.tvDate.text.toString()
            it?.let {
                diaryViewModel.updateDiary(
                    Diary(
                        diaryId.toInt(),
                        it.toString(),
                        content,
                        date
                    )
                )
            }
        }

        binding.edtContentDiary.addTextChangedListener {
            val title = binding.edtTitle.text.toString()
            val date = binding.tvDate.text.toString()
            it?.let {
                diaryViewModel.updateDiary(
                    Diary(
                        diaryId.toInt(),
                        title,
                        it.toString(),
                        date
                    )
                )
            }
        }

        binding.tvDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.Theme_Holo_Light_Dialog_MinWidth,
                setListener,
                yearNow,
                monthNow,
                dayNow
            )
            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.show()
        }
        setListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val month = month + 1
            binding.tvDate.text = "$dayOfMonth/$month/$year"
            val title = binding.edtTitle.text.toString()
            val content = binding.edtContentDiary.text.toString()
                diaryViewModel.updateDiary(
                    Diary(
                        diaryId.toInt(),
                        title,
                        content,
                        "$dayOfMonth/$month/$year"
                    )
                )
        }
    }


    override fun onPause() {
        super.onPause()
        val content = binding.edtContentDiary.text.toString()
        val title = binding.edtTitle.text.toString()
        val date = binding.tvDate.text.toString()
        if (content == "" && title == "") diaryViewModel.deleteDiary(
            Diary(
                diaryId.toInt(),
                title,
                content,
                date
            )
        )
    }
}