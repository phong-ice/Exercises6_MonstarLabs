package com.example.myapplication.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.helper.Ultils
import com.example.myapplication.helper.Ultils.DIARY_CONTENT
import com.example.myapplication.helper.Ultils.DIARY_DATE
import com.example.myapplication.helper.Ultils.DIARY_ID
import com.example.myapplication.helper.Ultils.DIARY_TITLE
import com.example.myapplication.activity.DetailsDiary
import com.example.myapplication.adapter.AdapterListDate
import com.example.myapplication.adapter.AdapterListDiary
import com.example.myapplication.callback.CommunicationCalendarFragment
import com.example.myapplication.callback.CommunicationCalendarFragmentTwo
import com.example.myapplication.databinding.FragmentMonthBinding
import com.example.myapplication.model.Diary
import com.example.myapplication.repository.DiaryRepository
import com.example.myapplication.viewmodel.DiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class MonthFragment(private val listener: CommunicationCalendarFragmentTwo) : Fragment(),
    CommunicationCalendarFragment {

    companion object {
        const val ARG_PARAM1 = "pos"
    }

    private var position: String? = null
    private val binding: FragmentMonthBinding by lazy { FragmentMonthBinding.inflate(layoutInflater) }
    private lateinit var selectedDate: LocalDate
    private val haftPag = Ultils.COUNT_PAG / 2
    private lateinit var adapterListDate: AdapterListDate
    private lateinit var listDate: MutableList<String>
    private lateinit var listDiary: MutableList<Diary>
    private val repo: DiaryRepository by lazy { DiaryRepository(requireActivity().application) }
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelProvide(repo)
        )[DiaryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDate = LocalDate.now()
        arguments?.let {
            position = it.getString(ARG_PARAM1)
        }
        position?.let {
            when {
                it.toInt() > haftPag -> {
                    selectedDate = selectedDate.plusMonths((it.toInt() - haftPag).toLong())
                }
                it.toInt() < haftPag -> {
                    selectedDate = selectedDate.minusMonths((haftPag - it.toInt()).toLong())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvMonth.text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM"))
        binding.tvYear.text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy"))
        listDate = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            listDate.addAll(getMonthArr())
            withContext(Dispatchers.Main) { adapterListDate.notifyDataSetChanged() }
        }
        adapterListDate = AdapterListDate(this, listDate, this, diaryViewModel, getDate(""))
        binding.lvDate.apply {
            adapter = adapterListDate
            layoutManager = GridLayoutManager(requireContext(), 7)
        }
        listDiary = mutableListOf()
    }

    private fun getMonthArr(): MutableList<String> {
        val daysInMonthArray: MutableList<String> = mutableListOf()
        val yearMonth = YearMonth.from(selectedDate)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            when {
                i <= dayOfWeek && dayOfWeek != 7 -> daysInMonthArray.add("")
                i > daysInMonth + dayOfWeek -> {
                }
                else -> {
                    if (i > dayOfWeek) daysInMonthArray.add("${i - dayOfWeek}")
                }
            }
        }
        return daysInMonthArray
    }

    override fun getDate(day: String): String {
        return "$day/${selectedDate.format(DateTimeFormatter.ofPattern("M/yyyy"))}"
    }

    override fun itemOnClick(date: String) {
        listener.addFragment(getDate(date))
    }

    override fun itemDoubleClick(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = diaryViewModel.insertDiary(Diary(0, "", "", getDate(date)))
            val intent = Intent(requireContext(), DetailsDiary::class.java)
            val bundle = Bundle()
            bundle.putString(DIARY_ID, id.toString())
            bundle.putString(DIARY_TITLE, "")
            bundle.putString(DIARY_CONTENT, "")
            bundle.putString(DIARY_DATE, getDate(date))
            intent.putExtras(bundle)
            requireContext().startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("test123", "111111")
    }
}