package com.maple.gameTimer.adapter.entity

import androidx.annotation.LayoutRes

data class Game(
    var uid: String,
    var name: String,
    var describe:String,
    val cls: Class<*>,
    @LayoutRes val settingResource: Int
)