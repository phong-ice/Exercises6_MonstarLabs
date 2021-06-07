package com.example.myapplication.fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.AdapterListDiary
import com.example.myapplication.callback.MyButtonClickListener
import com.example.myapplication.databinding.FragmentDiaryBinding
import com.example.myapplication.helper.MySwipeHelper
import com.example.myapplication.model.Diary
import com.example.myapplication.repository.DiaryRepository
import com.example.myapplication.viewmodel.DiaryViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class DiaryByCalendarFragment : Fragment(), MyButtonClickListener {

    companion object {
        const val FRAGMENT_NAME = "fragment_name"
        const val ARGUMENTS_1 = "argument_1"
    }

    private val binding: FragmentDiaryBinding by lazy { FragmentDiaryBinding.inflate(layoutInflater) }
    private val repo: DiaryRepository by lazy { DiaryRepository(requireActivity().application) }
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelProvide(repo)
        )[DiaryViewModel::class.java]
    }
    private lateinit var adapterListDiary: AdapterListDiary
    private lateinit var listDiary: MutableList<Diary>
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        date = arguments!!.getString(ARGUMENTS_1).toString()
        binding.edtSearch.visibility = View.GONE
        binding.btnAddDiary.visibility = View.GONE
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listDiary = mutableListOf()
        diaryViewModel.getDiaryByDate(date).observe(this, {
            listDiary.clear()
            listDiary.addAll(it)
            adapterListDiary = AdapterListDiary(requireContext(), listDiary)
            binding.lvDairy.apply {
                adapter = adapterListDiary
                layoutManager = LinearLayoutManager(requireContext())
            }
        })
        val itemTouchHelper: ItemTouchHelper.SimpleCallback = MySwipeHelper(this)
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.lvDairy)
    }

    override fun onClick(pos: Int) {
        val diary = listDiary[pos]
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton("Delete") { dialog, which ->
            diaryViewModel.deleteDiary(diary)
            adapterListDiary.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel"){dialog,which ->
            adapterListDiary.notifyDataSetChanged()
            dialog.dismiss()
        }
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
    }
}