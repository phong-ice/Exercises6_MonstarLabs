package com.example.myapplication.helper

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterListDiary
import com.example.myapplication.callback.MyButtonClickListener

class MySwipeHelper(private val listener:MyButtonClickListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onClick(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(viewHolder != null){
            val holder: AdapterListDiary.ViewHolder = viewHolder as AdapterListDiary.ViewHolder
            val foreground:View = holder.foreground
            getDefaultUIUtil().onSelected(foreground)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val holder: AdapterListDiary.ViewHolder = viewHolder as AdapterListDiary.ViewHolder
        val foreground:View = holder.foreground
        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive)
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val holder: AdapterListDiary.ViewHolder = viewHolder as AdapterListDiary.ViewHolder
        val foreground:View = holder.foreground
        getDefaultUIUtil().onDrawOver(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val holder: AdapterListDiary.ViewHolder = viewHolder as AdapterListDiary.ViewHolder
        val foreground:View = holder.foreground
        getDefaultUIUtil().clearView(foreground)
    }

}