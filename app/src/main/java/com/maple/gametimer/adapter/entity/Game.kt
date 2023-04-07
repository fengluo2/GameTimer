package com.maple.gametimer.adapter.entity

import androidx.annotation.LayoutRes

data class Game(
    var uid: String,
    var name: String,
    val cls: Class<*>,
    @LayoutRes val settingResource: Int
)