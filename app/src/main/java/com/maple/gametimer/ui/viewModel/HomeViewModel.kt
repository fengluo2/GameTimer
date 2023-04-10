package com.maple.gameTimer.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maple.gameTimer.R
import com.maple.gameTimer.adapter.entity.Game
import com.maple.gameTimer.ui.activity.ModeFTTPRActivity

class HomeViewModel : ViewModel() {

    private val _listData = MutableLiveData<List<Game>>().apply {
        value = listOf(
            Game(
                "FTTPR",
                "固定回合时长",
                "固定到每回合每人时间,适合中国象棋等",
                ModeFTTPRActivity::class.java,
                R.layout.popup_windows_setting_mod_fttpr
            )
        )

    }
    val listData: LiveData<List<Game>> = _listData
}