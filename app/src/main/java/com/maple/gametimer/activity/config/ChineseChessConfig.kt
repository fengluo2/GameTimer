package com.maple.gametimer.activity.config

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import com.maple.gametimer.Constant
import com.maple.gametimer.R
import com.maple.gametimer.adapter.entity.Game


class ChineseChessConfig(
    private val context: Context
) {
    private val TAG: String = "ChineseChess"
    private lateinit var time: EditText
    private lateinit var save: Button
    private lateinit var close: Button
    private val sp: SharedPreferences = context.getSharedPreferences("chineseChess", MODE_PRIVATE)

    fun init(view: View, item: Game, popupWindow: PopupWindow) {
        time = view.findViewById(R.id.popup_windows_chinese_chess_setting_time_input)
        save = view.findViewById(R.id.popup_windows_chinese_chess_setting_save)
        close = view.findViewById(R.id.popup_windows_chinese_chess_setting_close)

        time.setText(sp.getInt("time", Constant.DEFAULT_CHINESE_CHESS_TIME).toString())

        save.setOnClickListener {
            val editor = sp.edit()
            try {
                editor.putInt("time", time.text.toString().toInt())
            } catch (e: java.lang.Exception) {
                e.message?.let { it1 -> Log.e(TAG, it1) }
                editor.putInt("time", Constant.DEFAULT_CHINESE_CHESS_TIME)
            } finally {
                editor.apply()
            }
            close(popupWindow)
        }
        close.setOnClickListener {
            close(popupWindow)
        }
    }

    fun getConfig(): Map<String, String> {
        val map = HashMap<String, String>()
        map["time"] = sp.getInt("time", 30).toString()
        return map
    }

    private fun close(popupWindow: PopupWindow) {
        popupWindow.dismiss()
    }
}