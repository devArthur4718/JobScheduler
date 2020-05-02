package com.devarthur4718.jobscheduler

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService

class NotificationJobService() : JobService() {

    companion object{
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"

    }

    lateinit var mNotifyManager : NotificationManager

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    @SuppressLint("ResourceType")
    override fun onStartJob(params: JobParameters?): Boolean {

        applicationContext.showNotificationTap(
            R.drawable.ic_android_black_24dp,
            "Job service",
            "Your Job ran to completion",
            PRIMARY_CHANNEL_ID,
            MainActivity::class.java,
            true
        )

        return false
    }


}