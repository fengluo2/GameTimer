package com.maple.gametimer.activity.config

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.appcompat.widget.SwitchCompat
import com.maple.gametimer.Constant
import com.maple.gametimer.R
import com.maple.gametimer.adapter.entity.Game


@Suppress("UNUSED_ANONYMOUS_PARAMETER", "UNUSED_PARAMETER")
class ModeFTTPRConfig(
    context: Context
) {
    @Suppress("PrivatePropertyName")
    private val TAG: String = "ChineseChess"
    private lateinit var timeEditText: EditText
    private lateinit var strictModeSwitch: SwitchCompat
    private lateinit var save: Button
    private val sp: SharedPreferences = context.getSharedPreferences("chineseChess", MODE_PRIVATE)

    fun init(view: View, item: Game, popupWindow: PopupWindow) {
        timeEditText = view.findViewById(R.id.popup_windows_chinese_chess_setting_time_input)
        strictModeSwitch =
            view.findViewById(R.id.popup_windows_chinese_chess_setting_strict_mode_switch)
        save = view.findViewById(R.id.popup_windows_chinese_chess_setting_save)

        timeEditText.setText(sp.getInt("time", Constant.DEFAULT_MODE_FTTPR_TIME).toString())
        strictModeSwitch.isChecked =
            sp.getBoolean("strictMode", Constant.DEFAULT_MODE_FTTPR_STRICT_MODE)

        save.setOnClickListener {
            val editor = sp.edit()
            try {
                editor.putInt("time", timeEditText.text.toString().toInt())
                editor.putBoolean("strictMode", strictModeSwitch.isChecked)
            } catch (e: java.lang.Exception) {
                e.message?.let { it1 -> Log.e(TAG, it1) }
                editor.putInt("time", Constant.DEFAULT_MODE_FTTPR_TIME)
                editor.putBoolean("strictMode", Constant.DEFAULT_MODE_FTTPR_STRICT_MODE)
            } finally {
                editor.apply()
            }
            close(popupWindow)
        }
    }

    fun getConfig(): Map<String, String> {
        val map = HashMap<String, String>()
        map["time"] = sp.getInt("time", 30).toString()
        map["strictMode"] = sp.getBoolean("strictMode", false).toString()
        return map
    }

    private fun close(popupWindow: PopupWindow) {
        popupWindow.dismiss()
    }
}