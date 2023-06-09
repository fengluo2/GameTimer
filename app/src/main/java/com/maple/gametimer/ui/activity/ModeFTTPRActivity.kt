package com.maple.gameTimer.ui.activity

import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.maple.gameTimer.constant.Constant
import com.maple.gameTimer.R
import com.maple.gameTimer.ui.activity.config.ModeFTTPRConfig
import com.maple.gameTimer.constant.CountTimeStatus
import com.maple.gameTimer.constant.GameStatus
import com.maple.gameTimer.utils.ResourceUtil
import com.maple.gameTimer.view.DonutProgress
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.HashMap


@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ModeFTTPRActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG: String = "ChineseChessActivity"

    private lateinit var config: ModeFTTPRConfig

    private var totalSecond: Int = Constant.DEFAULT_MODE_FTTPR_TIME
    private var strictMode: Boolean = Constant.DEFAULT_MODE_FTTPR_STRICT_MODE
    private var soundPerSecond: Boolean = Constant.DEFAULT_MODE_FTTPR_SOUND_PER_SECOND
    private var paintedEggshell: Boolean = Constant.DEFAULT_MODE_FTTPR_PAINTED_EGGSHELL

    private var tempSecond: Int = 0
    private var clickNumber: Int = 0
    private var recoveryMode: Boolean = false

    private lateinit var startTime: Date
    private lateinit var pauseTime: Date
    private lateinit var endTime: Date

    private var countTimeStatus: CountTimeStatus = CountTimeStatus.INIT
    private var gameStatus: GameStatus = GameStatus.INIT

    private lateinit var layout: ConstraintLayout
    private lateinit var donutProgress: DonutProgress

    private val soundMap: HashMap<String, Int> = HashMap()
    private lateinit var soundPool: SoundPool
    private lateinit var countDownTimer: CountDownTimer

    private val numberFormat: NumberFormat = NumberFormat.getInstance()
    private val random: Random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_fttpr)

        donutProgress = findViewById(R.id.chinese_chess_activity_donut_progress)
        layout = findViewById(R.id.chinese_chess_activity)

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pauseCountTime()
            pauseAlert()
            return true
        }
        return false
    }

    private fun init() {
        Log.d(TAG, "initWidget: ")
        numberFormat.maximumFractionDigits = 0
        config = ModeFTTPRConfig(this)
        layout.setBackgroundColor(Color.WHITE)
    }

    private fun initSound() {
        val soundPoolBundle = SoundPool.Builder()
        val audioAttributesBuilder = AudioAttributes.Builder()
        audioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        audioAttributesBuilder.setUsage(AudioAttributes.USAGE_GAME)
        soundPoolBundle.setAudioAttributes(audioAttributesBuilder.build())
        soundPoolBundle.setMaxStreams(3)
        soundPool = soundPoolBundle.build()


        soundMap["count_down_start"] =
            soundPool.load(this, ResourceUtil.getRawId(this, "count_down_start"), 1)
        for (i in 1..5) {
            try {
                soundMap["count_down_tick_$i"] =
                    soundPool.load(this, ResourceUtil.getRawId(this, "count_down_tick_$i"), 1)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "initSound: " + e.message)
            }
        }
        for (i in 0..11) {
            try {
                soundMap["count_down_$i"] =
                    soundPool.load(this, ResourceUtil.getRawId(this, "count_down_$i"), 1)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "initSound: " + e.message)
            }
        }
    }

    private fun initConfig() {
        Log.d(TAG, "initConfig: ")
        if (countTimeStatus == CountTimeStatus.INIT) {
            totalSecond = config.getConfig()["time"]!!.toInt()
            strictMode = config.getConfig()["strictMode"]!!.toBoolean()
        }
        tempSecond = totalSecond
        donutProgress.text = tempSecond.toString()
    }

    private fun resetConfig() {
        Log.d(TAG, "resetConfig: ")
        startTime = Date()
        pauseTime = Date(0)
    }

    private fun initListen() {
        Log.d(TAG, "initListen: ")

        layout.setOnClickListener {
            startCountTime()
        }
        layout.setOnLongClickListener {
            if (countTimeStatus == CountTimeStatus.RUNNING) pauseCountTime()
            else if (countTimeStatus == CountTimeStatus.PAUSE) recoveryCountTime()
            true
        }
    }

    private fun startAlert() {
        Log.d(TAG, "startAlert: ")
        AlertDialog.Builder(this).apply {
            setTitle("即将开始")//表示
            setMessage("每人每回合有${totalSecond}秒的时间")//内容
            setCancelable(true)
            setPositiveButton("开始") { dialog, which ->
                startTime = Date()
                pauseTime = Date(0)
                gameStatus = GameStatus.RUNNING
                startCountTime()
            }
            setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.cancel()
                }
                true
            }
            show()
        }
    }

    private fun pauseAlert() {
        endTime = Date()
        AlertDialog.Builder(this).apply {
            setTitle("结束游戏?")
            setMessage("回合数:" + ((clickNumber + 1) / 2) + "\n游戏时长:" + formatDate(endTime.time - startTime.time))
            setCancelable(false)
            setPositiveButton("确认结束") { dialog, which ->
                dialog.cancel()
                finish()
            }
            setNegativeButton("继续") { dialog, which ->
                recoveryCountTime()
            }
            setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    dialog.cancel()
                recoveryCountTime()
                true
            }
        }.show()
    }

    private fun endAlert() {
        Log.d(TAG, "endAlert: ")
        endTime = Date()
        countTimeStatus = CountTimeStatus.READY
        AlertDialog.Builder(this).apply {
            setTitle("游戏结束")
            setMessage("游戏时长:" + formatDate(endTime.time - startTime.time))
            setCancelable(false)
            setPositiveButton("再来一局") { dialog, which ->
                initConfig()
                resetConfig()
                startCountTime()
            }
            if (!strictMode) setNegativeButton("继续") { dialog, which ->
                initConfig()
                startCountTime()
            }

            setNeutralButton("退出") { dialog, which ->
                dialog.cancel()
                finish()
            }
            setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    dialog.cancel()
                finish()
                true
            }
        }.show()
    }


    private fun initCountDownTime() {
        Log.d(TAG, "initCountDownTime: ")
        countDownTimer = object : CountDownTimer(tempSecond * 1000L, 1000) {
            override fun onFinish() {
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
        gameStatus = GameStatus.READY
    }

    private fun startCountTime() {
        Log.d(TAG, "startCountTime: ")
        if (gameStatus == GameStatus.READY) {
            gameStatus = GameStatus.RUNNING
            startTime = Date()
            pauseTime = Date(0)
            countDownTimer.start()
            clickNumber += 1
        } else {
            when (countTimeStatus) {
                CountTimeStatus.READY -> {
                    countTimeStatus = CountTimeStatus.RUNNING
                    countDownTimer.start()
                    clickNumber += 1
                }
                CountTimeStatus.RUNNING -> {
                    countDownTimer.cancel()
                    initConfig()
                    countDownTimer.start()
                    clickNumber += 1
                }
                else -> {}
            }
        }
    }

    private fun pauseCountTime() {
        Log.d(TAG, "pauseCountTime: ")
        if (countTimeStatus != CountTimeStatus.RUNNING) return
        pauseTime = Date()
        countDownTimer.cancel()
        tempSecond++
        layout.setBackgroundColor(Color.GRAY)
        soundMap["count_down_start"]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
        countTimeStatus = CountTimeStatus.PAUSE
    }

    private fun recoveryCountTime() {
        Log.d(TAG, "recoveryCountTime: ")
        if (countTimeStatus != CountTimeStatus.PAUSE) return
        startTime = Date(Date().time - (pauseTime.time - startTime.time))
        pauseTime = Date(0)
        countDownTimer.start()
        layout.setBackgroundColor(Color.WHITE)
        recoveryMode = true
        countTimeStatus = CountTimeStatus.RUNNING
    }

    private fun stopCountTime() {
        Log.d(TAG, "stopCountTime: ")
        if (countTimeStatus == CountTimeStatus.RUNNING) countDownTimer.cancel()
        countTimeStatus = CountTimeStatus.READY
    }

    private fun countDownPromptSound() {
        if (tempSecond == totalSecond || recoveryMode) {
            soundMap["count_down_start"]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
            if (recoveryMode) recoveryMode = false
        } else if (tempSecond > 11) {
            if (soundPerSecond) soundMap["count_down_tick_" + (random.nextInt(5) + 1)]?.let {
                soundPool.play(
                    it,
                    1f,
                    1f,
                    1,
                    0,
                    1f
                )
            }
        } else if (tempSecond > 0) {
            if (tempSecond == 11) {
                if (paintedEggshell) {
                    soundMap["count_down_$tempSecond"]?.let {
                        soundPool.play(
                            it,
                            1f,
                            1f,
                            1,
                            0,
                            1f
                        )
                    }
                } else if (soundPerSecond) {
                    soundMap["count_down_tick_" + (random.nextInt(5) + 1)]?.let {
                        soundPool.play(
                            it,
                            1f,
                            1f,
                            1,
                            0,
                            1f
                        )
                    }
                }
            } else {
                soundMap["count_down_$tempSecond"]?.let {
                    soundPool.play(
                        it,
                        1f,
                        1f,
                        1,
                        0,
                        1f
                    )
                }
            }
        }
    }

    private fun inQueue(){}

    private fun formatDate(date: Long): String {
        val second: Int = (date / 1000).toInt()
        val minute: Int = second / 60
        return minute.toString() + "分" + (second - (minute * 60)).toString() + "秒"
    }
}