package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.helper.Ultils
import com.example.myapplication.activity.DetailsDiary
import com.example.myapplication.databinding.ItemDiaryBinding
import com.example.myapplication.model.Diary
import java.text.SimpleDateFormat

class AdapterListDiary(private val context: Context,private val listDiary:MutableList<Diary>):RecyclerView.Adapter<AdapterListDiary.ViewHolder>() {

    class ViewHolder(binding:ItemDiaryBinding):RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.tvTitleItem
        val tvContent = binding.tvContentItem
        val tvDate = binding.tvDateItem
        val foreground = binding.layoutForeground
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDiaryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = listDiary[position].titleDiary
        holder.tvContent.text = listDiary[position].contentDiary
        holder.tvDate.text = listDiary[position].dateDiary

        holder.itemView.setOnClickListener {
            val diary = listDiary[position]
            val intent = Intent(context, DetailsDiary::class.java)
            val bundle = Bundle()
            bundle.putString(Ultils.DIARY_ID,diary.idDiary.toString())
            bundle.putString(Ultils.DIARY_TITLE,diary.titleDiary)
            bundle.putString(Ultils.DIARY_CONTENT,diary.contentDiary)
            bundle.putString(Ultils.DIARY_DATE,diary.dateDiary)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listDiary.size
    }
}