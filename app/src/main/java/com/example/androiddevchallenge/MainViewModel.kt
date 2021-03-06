package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private lateinit var timer: CountDownTimer
    private val mainSurfaceModel: MutableLiveData<MainSurfaceModel> by lazy {
        MutableLiveData<MainSurfaceModel>()
    }

    fun getMainSurfaceModel(): LiveData<MainSurfaceModel> {
        return mainSurfaceModel
    }

    fun startTimer(hour: Int?, minute: Int?, second: Int?) {
        timer = object : CountDownTimer(formatToMillis(hour, minute, second), COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                mainSurfaceModel.value = millisToMainSurfaceModel(millisUntilFinished)
            }

            override fun onFinish() {
                mainSurfaceModel.value = MainSurfaceModel("00", "00", "00")
            }
        }
        timer.start()
    }

    private fun formatToMillis(hour: Int?, minute: Int?, second: Int?): Long {
        return ((hour?.times(HOUR_TO_MILLIS) ?: 0)
                + (minute?.times(MINUTE_TO_MILLIS) ?: 0)
                + (second?.times(SECOND_TO_MILLIS) ?: 0)).toLong()
    }

    private fun millisToMainSurfaceModel(millisecond: Long): MainSurfaceModel {
        val seconds = (millisecond / SECOND_TO_MILLIS).toInt() % 60
        val minutes = (millisecond / (MINUTE_TO_MILLIS) % 60)
        val hours = (millisecond / (HOUR_TO_MILLIS) % 24)
        return MainSurfaceModel(hours.toString(), minutes.toString(), seconds.toString())
    }

    companion object {
        const val HOUR_TO_MILLIS = 3600000
        const val MINUTE_TO_MILLIS = 60000
        const val SECOND_TO_MILLIS = 1000
        const val COUNTDOWN_INTERVAL = 1000L
    }
}