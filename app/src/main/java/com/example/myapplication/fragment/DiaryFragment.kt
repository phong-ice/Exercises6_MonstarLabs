package com.example.myapplication.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.helper.Ultils.DIARY_CONTENT
import com.example.myapplication.helper.Ultils.DIARY_DATE
import com.example.myapplication.helper.Ultils.DIARY_ID
import com.example.myapplication.helper.Ultils.DIARY_TITLE
import com.example.myapplication.activity.DetailsDiary
import com.example.myapplication.adapter.AdapterListDiary
import com.example.myapplication.callback.MyButtonClickListener
import com.example.myapplication.databinding.FragmentDiaryBinding
import com.example.myapplication.helper.MySwipeHelper
import com.example.myapplication.model.Diary
import com.example.myapplication.repository.DiaryRepository
import com.example.myapplication.viewmodel.DiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DiaryFragment : Fragment(),MyButtonClickListener {

    companion object {
        const val NAME_FRAGMENT: String = "diary_fragment"
    }

    private val binding: FragmentDiaryBinding by lazy { FragmentDiaryBinding.inflate(layoutInflater) }
    private val repo: DiaryRepository by lazy { DiaryRepository(requireActivity().application) }
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelProvide(repo)
        )[DiaryViewModel::class.java]
    }
    private var adapterListDiary: AdapterListDiary? = null
    private lateinit var listDiary: MutableList<Diary>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listDiary = mutableListOf()
        diaryViewModel.getAllDiary().observe(this, {
            listDiary.clear()
            listDiary.addAll(it)
            adapterListDiary = AdapterListDiary(requireContext(), listDiary)
            binding.lvDairy.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterListDiary
            }
        })
        binding.btnAddDiary.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val id = diaryViewModel.insertDiary(Diary(0, "", "", ""))
                val intent = Intent(requireContext(), DetailsDiary::class.java)
                val bundle = Bundle()
                bundle.putString(DIARY_ID, id.toString())
                bundle.putString(DIARY_TITLE, "")
                bundle.putString(DIARY_CONTENT, "")
                bundle.putString(DIARY_DATE, "")
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        binding.edtSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                when (s.toString()) {
                    "" -> diaryViewModel.getAllDiary().observe(this@DiaryFragment, androidx.lifecycle.Observer {
                        listDiary.clear()
                        listDiary.addAll(it)
                        adapterListDiary?.notifyDataSetChanged()
                    })
                    else -> diaryViewModel.selectDiaryByTitleOrContent(s.toString()).observe(this@DiaryFragment,
                        androidx.lifecycle.Observer {
                            listDiary.clear()
                            listDiary.addAll(it)
                            adapterListDiary?.notifyDataSetChanged()
                        })
                }
            }

        })
        val itemTouchHelper:ItemTouchHelper.SimpleCallback = MySwipeHelper(this)
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.lvDairy)
    }

    override fun onClick(pos: Int) {
        val diary = listDiary[pos]
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton("Delete") { dialog, which ->
            diaryViewModel.deleteDiary(diary)
            adapterListDiary?.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel"){dialog,which ->
            adapterListDiary?.notifyDataSetChanged()
            dialog.dismiss()
        }
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        adapterListDiary?.let { it.notifyDataSetChanged() }
    }
}