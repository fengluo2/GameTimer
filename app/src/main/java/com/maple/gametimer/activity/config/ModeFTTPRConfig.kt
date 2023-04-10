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
    private lateinit var soundPerSecondSwitch: SwitchCompat
    private lateinit var paintedEggshellSwitch: SwitchCompat
    private lateinit var save: Button
    private val sp: SharedPreferences = context.getSharedPreferences("chineseChess", MODE_PRIVATE)

    fun init(view: View, item: Game, popupWindow: PopupWindow) {
        timeEditText = view.findViewById(R.id.popup_windows_chinese_chess_setting_time_input)
        strictModeSwitch =
            view.findViewById(R.id.popup_windows_chinese_chess_setting_strict_mode_switch)
        soundPerSecondSwitch =
            view.findViewById(R.id.popup_windows_chinese_chess_setting_second_tick_sound_switch)
        paintedEggshellSwitch =
            view.findViewById(R.id.popup_windows_chinese_chess_setting_painted_eggshell_switch)
        save = view.findViewById(R.id.popup_windows_chinese_chess_setting_save)

        timeEditText.setText(sp.getInt("time", Constant.DEFAULT_MODE_FTTPR_TIME).toString())
        strictModeSwitch.isChecked =
            sp.getBoolean("strictMode", Constant.DEFAULT_MODE_FTTPR_STRICT_MODE)
        soundPerSecondSwitch.isChecked =
            sp.getBoolean("soundPerSecond", Constant.DEFAULT_MODE_FTTPR_SOUND_PER_SECOND)
        paintedEggshellSwitch.isChecked =
            sp.getBoolean("paintedEggshell", Constant.DEFAULT_MODE_FTTPR_PAINTED_EGGSHELL)

        save.setOnClickListener {
            val editor = sp.edit()
            try {
                editor.putInt("time", timeEditText.text.toString().toInt())
                editor.putBoolean("strictMode", strictModeSwitch.isChecked)
                editor.putBoolean("soundPerSecond", soundPerSecondSwitch.isChecked)
                editor.putBoolean("paintedEggshell", paintedEggshellSwitch.isChecked)
            } catch (e: java.lang.Exception) {
                e.message?.let { it1 -> Log.e(TAG, it1) }
                editor.putInt("time", Constant.DEFAULT_MODE_FTTPR_TIME)
                editor.putBoolean("strictMode", Constant.DEFAULT_MODE_FTTPR_STRICT_MODE)
                editor.putBoolean("soundPerSecond", Constant.DEFAULT_MODE_FTTPR_SOUND_PER_SECOND)
                editor.putBoolean("paintedEggshell", Constant.DEFAULT_MODE_FTTPR_PAINTED_EGGSHELL)
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
        map["soundPerSecond"] = sp.getBoolean("soundPerSecond", true).toString()
        map["paintedEggshell"] = sp.getBoolean("paintedEggshell", false).toString()
        return map
    }

    private fun close(popupWindow: PopupWindow) {
        popupWindow.dismiss()
    }
}