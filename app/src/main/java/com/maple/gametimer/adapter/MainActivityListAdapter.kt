package com.maple.gametimer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maple.gametimer.R
import com.maple.gametimer.adapter.entity.Game

class MainActivityListAdapter(
    private var context: Context,
    private var data: List<Game>
) : RecyclerView.Adapter<MainActivityListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.mainActivityListItemName)
        var setting: ImageButton = view.findViewById(R.id.mainActivityListItemSetting)
    }

    interface OnItemClickListener {
        fun onNameClick(context: Context, view: View, item: Game, position: Int)
        fun onSettingClick(context: Context, view: View, item: Game, position: Int)
    }

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_activity_main, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        //单机事件
        holder.name.setOnClickListener {
            onItemClickListener.onNameClick(context, holder.itemView, item, position)
        }

        holder.setting.setOnClickListener {
            onItemClickListener.onSettingClick(context, holder.itemView, item, position)
        }
    }
}