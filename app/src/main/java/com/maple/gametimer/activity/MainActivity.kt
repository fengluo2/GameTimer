package com.maple.gametimer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maple.gametimer.R
import com.maple.gametimer.adapter.MainActivityListAdapter
import com.maple.gametimer.adapter.entity.Game


class MainActivity : AppCompatActivity() {
    private val listData = listOf(
        Game(
            "chineseChess",
            "中国象棋",
            ChineseChessActivity::class.java,
            R.layout.popup_windows_setting_chinese_chess
        )
    )
    private lateinit var listView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.mainActivityList)

        initListView()
    }

    private fun initListView() {
        listView.layoutManager = LinearLayoutManager(this)
        val mainActivityListAdapter = MainActivityListAdapter(this, listData)
        mainActivityListAdapter.setOnItemClickListener(object :
            MainActivityListAdapter.OnItemClickListener {
            override fun onNameClick(context: Context, view: View, item: Game, position: Int) {
                startActivity(Intent().setClass(context, item.cls))
            }

            override fun onSettingClick(context: Context, view: View, item: Game, position: Int) {
                val popupWindowView: View =
                    LayoutInflater.from(context).inflate(item.settingResource, null)
                val popupWindow = PopupWindow(
                    popupWindowView,
                    resources.displayMetrics.widthPixels * 2 / 3,
                    resources.displayMetrics.heightPixels * 1 / 2,
                    true
                )
                popupWindow.isOutsideTouchable = true //设置点击外部区域可以取消popupWindow
                popupWindow.animationStyle = android.R.style.Animation_Activity
                popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.white))
                popupWindow.showAtLocation(view.rootView, Gravity.CENTER, 0, 0)
                popupWindow.setOnDismissListener { setAlpha(1.0f) }
                setAlpha(0.5f)

                when (item.uid) {
                    "chineseChess" -> {
                        com.maple.gametimer.activity.config.ChineseChessConfig(
                            context
                        ).init(popupWindowView, item, popupWindow)
                    }
                }
            }
        })
        listView.adapter = mainActivityListAdapter
    }

    private fun setAlpha(f: Float) {
        val lp = window.attributes
        lp.alpha = f
        window.attributes = lp
    }
}
