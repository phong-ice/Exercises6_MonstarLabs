package com.example.myapplication.adapter

import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.helper.Ultils
import com.example.myapplication.callback.CommunicationCalendarFragment
import com.example.myapplication.databinding.ItemListDateNomalBinding
import com.example.myapplication.databinding.ItemListDateNowBinding
import com.example.myapplication.fragment.MonthFragment
import com.example.myapplication.viewmodel.DiaryViewModel
import java.text.SimpleDateFormat
import java.util.*

class AdapterListDate(
    val fragment: MonthFragment,
    private val listDate: MutableList<String>,
    private val listener: CommunicationCalendarFragment,
    private val diaryViewModel: DiaryViewModel,
    private val yearMonth:String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_1 = 1
    val TYPE_2 = 2
    val TYPE_3 = 3
    private var isClicked = false
    private var pos = 0
    private val green = Color.argb(255, 9, 255, 0)
    private val yellow = Color.argb(255, 255, 238, 0)
    private val red = Color.argb(255, 255, 0, 0)

    class ViewHolder(binding: ItemListDateNomalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvDay = binding.tvDate
        val tvAmount = binding.tvAmount
        val layoutItem = binding.layoutItem
    }

    class ViewHolder2(binding: ItemListDateNowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvDay = binding.tvDate
        val tvAmount = binding.tvAmount
        val layoutItem = binding.layoutItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_1 -> ViewHolder(
                ItemListDateNomalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_2 -> ViewHolder2(
                ItemListDateNowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ViewHolder(
                ItemListDateNomalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_1 -> {
                val holder1: ViewHolder = holder as ViewHolder
                holder1.tvDay.text = listDate[position]
                diaryViewModel.getDiaryByDate("${listDate[position]}$yearMonth").observe(fragment, androidx.lifecycle.Observer {
                    when{
                        it.size == 0 -> { }
                        it.size < 4 -> holder1.tvAmount.setTextColor(green)
                        it.size < 7 -> holder1.tvAmount.setTextColor(yellow)
                        else -> holder1.tvAmount.setTextColor(red)
                    }
                })
                holder1.layoutItem.isSelected = false
                if (listener.getDate(listDate[position]) == Ultils.DATE_FOCUS) {
                    holder1.layoutItem.isSelected = true
                }
                holder1.itemView.setOnClickListener {
                    when {
                        isClicked && pos == position -> {
                            listener.itemDoubleClick(listDate[position])
                        }
                        else -> {
                            isClicked = true
                            pos = position
                            Handler().postDelayed(Runnable {
                                isClicked = false
                                listener.itemOnClick(listDate[position])
                            }, 500)
                        }
                    }
                }
            }
            TYPE_2 -> {
                val holder2: ViewHolder2 = holder as ViewHolder2
                holder2.tvDay.text = listDate[position]
                diaryViewModel.getDiaryByDate("${listDate[position]}$yearMonth").observe(fragment, androidx.lifecycle.Observer {
                    when{
                        it.size == 0 -> { }
                        it.size < 4 -> holder2.tvAmount.setTextColor(green)
                        it.size < 7 -> holder2.tvAmount.setTextColor(yellow)
                        else -> holder2.tvAmount.setTextColor(red)
                    }
                    Log.i("test123","$it")
                })
                holder2.itemView.setOnClickListener {
                    when {
                        isClicked && pos == position -> {
                            listener.itemDoubleClick(listDate[position])
                        }
                        else -> {
                            isClicked = true
                            pos = position
                            Handler().postDelayed(Runnable {
                                isClicked = false
                                listener.itemOnClick(listDate[position])
                            }, 500)
                        }
                    }
                }
            }
            TYPE_3 -> {
                val holder3: ViewHolder = holder as ViewHolder
                holder3.tvDay.text = ""
                holder3.tvAmount.text = ""
            }
        }
    }

    override fun getItemCount(): Int {
        return listDate.size
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            listDate[position] == "" -> TYPE_3
            listener.getDate(listDate[position]) == SimpleDateFormat("d/M/yyyy").format(Calendar.getInstance().time) -> TYPE_2
            else -> TYPE_1
        }
    }

}