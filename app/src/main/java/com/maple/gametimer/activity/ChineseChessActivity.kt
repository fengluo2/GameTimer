package com.maple.gametimer.activity

import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.maple.gametimer.Constant
import com.maple.gametimer.R
import com.maple.gametimer.activity.config.ChineseChessConfig
import com.maple.gametimer.activity.config.CountTimeStatus
import com.maple.gametimer.utils.ResourceUtil
import com.maple.gametimer.view.DonutProgress
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap


class ChineseChessActivity : AppCompatActivity() {
    private val TAG: String = "ChineseChessActivity"

    private lateinit var config: ChineseChessConfig

    private lateinit var startTime: Date
    private lateinit var pauseTime: Date
    private lateinit var endTime: Date

    private var countTimeStatus: CountTimeStatus = CountTimeStatus.INIT

    private var totalSecond: Int = Constant.DEFAULT_CHINESE_CHESS_TIME
    private var tempSecond: Int = 0

    private lateinit var layout: ConstraintLayout
    private lateinit var donutProgress: DonutProgress
    private lateinit var soundPool: SoundPool

    private lateinit var countDownTimer: CountDownTimer

    private val numberFormat: NumberFormat = NumberFormat.getInstance()

    private val soundList: HashMap<Int, Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chess_chinese)

        init()
        initConfig()
        initSound()
        initListen()
        initCountDownTime()

        startAlert()
    }

    override fun onDestroy() {
        stopCountTime()
        soundPool.release()
        super.onDestroy()
    }

    private fun startCountTime() {
        Log.d(TAG, "startCountTime: ")
        if (countTimeStatus != CountTimeStatus.READY) return
        countDownTimer.start()
        countTimeStatus = CountTimeStatus.RUNNING
    }

    private fun pauseCountTime() {
        Log.d(TAG, "pauseCountTime: ")
        when (countTimeStatus) {
            CountTimeStatus.INIT -> return
            CountTimeStatus.READY -> return
            CountTimeStatus.RUNNING -> {
                countTimeStatus = CountTimeStatus.PAUSE
                pauseTime = Date()
                countDownTimer.cancel()
                tempSecond++
                layout.setBackgroundColor(Color.GRAY)
            }
            CountTimeStatus.PAUSE -> {
                countTimeStatus = CountTimeStatus.RUNNING
                startTime = Date(Date().time - (pauseTime.time - startTime.time))
                pauseTime = Date(0)
                countDownTimer.start()
                layout.setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun stopCountTime() {
        Log.d(TAG, "stopCountTime: ")
        if (countTimeStatus == CountTimeStatus.RUNNING) countDownTimer.cancel()
        countTimeStatus = CountTimeStatus.READY
    }

    private fun init() {
        Log.d(TAG, "initWidget: ")
        numberFormat.maximumFractionDigits = 0

        donutProgress = findViewById(R.id.chinese_chess_activity_donut_progress)
        layout = findViewById(R.id.chinese_chess_activity)
        config = ChineseChessConfig(this)

        layout.setBackgroundColor(Color.WHITE)
    }

    private fun initSound() {
        val soundPoolBundle = SoundPool.Builder()
        soundPoolBundle.setMaxStreams(3)
        val audioAttributesBuilder = AudioAttributes.Builder()
        audioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        soundPoolBundle.setAudioAttributes(audioAttributesBuilder.build())
        soundPool = soundPoolBundle.build()

        for (i in 0..11) {
            try {
                soundList[i] = soundPool.load(this, ResourceUtil.getRawId(this, "count_down_$i"), 1)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "initSound: " + e.message)
            }
        }
    }

    private fun initConfig() {
        Log.d(TAG, "initConfig: ")
        if (countTimeStatus == CountTimeStatus.INIT) {
            totalSecond = config.getConfig()["time"]!!.toInt()
        }
        tempSecond = totalSecond
    }

    private fun resetConfig() {
        Log.d(TAG, "resetConfig: ")
        initConfig()
        startTime = Date()
        pauseTime = Date(0)
    }

    private fun initListen() {
        Log.d(TAG, "initListen: ")

        layout.setOnClickListener {
            if (countTimeStatus != CountTimeStatus.RUNNING) return@setOnClickListener
            initConfig()
            stopCountTime()
            startCountTime()
        }
        layout.setOnLongClickListener {
            pauseCountTime()
            true
        }
    }

    private fun initCountDownTime() {
        Log.d(TAG, "initCountDownTime: ")
        countDownTimer = object : CountDownTimer(tempSecond * 1000L, 1000) {
            override fun onFinish() {
                endTime = Date()
                countTimeStatus = CountTimeStatus.READY
                endAlert()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (tempSecond < 0) {
                    this.onFinish()
                    this.cancel()
                    return
                }
                donutProgress.setDonut_progress(numberFormat.format((tempSecond).toFloat() / totalSecond * 100))
                donutProgress.text = (tempSecond).toString()
                countDownPromptSound()
                tempSecond--
            }
        }
        countTimeStatus = CountTimeStatus.READY
    }

    private fun startAlert() {
        Log.d(TAG, "startAlert: ")
        AlertDialog.Builder(this).apply {
            setTitle("即将开始")//表示
            setMessage("每人每回合有${totalSecond}秒的时间")//内容
            setCancelable(false)
            setPositiveButton("开始") { dialog, which ->
                startTime = Date()
                pauseTime = Date(0)
                startCountTime()
            }
            show()
        }
    }

    private fun endAlert() {
        Log.d(TAG, "endAlert: ")
        AlertDialog.Builder(this).apply {
            setTitle("游戏结束")
            setMessage("游戏时长:" + formatDate(endTime.time - startTime.time))
            setCancelable(false)
            setPositiveButton("退出") { dialog, which ->
                dialog.cancel()
                finish()
            }
            setNegativeButton("再来一局") { dialog, which ->
                resetConfig()
                startCountTime()
            }
        }.show()
    }

    private fun countDownPromptSound() {
        if (tempSecond > 11 || tempSecond < 0) return
        soundList[tempSecond]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
    }

    private fun formatDate(date: Long): String {
        val second: Int = (date / 1000).toInt()
        val minute: Int = second / 60
        return minute.toString() + "分" + (second - (minute * 60)).toString() + "秒"
    }
}