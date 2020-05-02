package com.devarthur4718.jobscheduler

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build

class NotificationJobService() : JobService() {

    companion object{
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val JOB_NOTIFICATION = "job_notification"
        const val REQUEST_CODE = 2
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
            "Your Job is Running",
            PRIMARY_CHANNEL_ID,
            MainActivity::class.java,
            true
        )

        return false
    }

    fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = JOB_NOTIFICATION
            val descriptionText = getString(R.string.notification)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PRIMARY_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}