package com.devarthur4718.jobscheduler

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun Context.showNotification(
    @IdRes iconDrawable: Int,
    title: String,
    comment: String,
    CHANNEL_ID: String = "11"
) {

    var builder = createNoficationBuilder(title, comment, CHANNEL_ID, iconDrawable, this)
    createNotificationChannel(this,this.getString(R.string.app_name),CHANNEL_ID)
    with(NotificationManagerCompat.from(this)) {
        notify(1, builder.build())
    }
}

fun Context.showNotificationActionButton(
    @IdRes iconDrawable: Int,
    title: String,
    comment: String,
    action : String,
    CHANNEL_ID: String = "11",
    activityClass: Class<*>
) {

    val intent = Intent(this, activityClass)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    var builder = createNoficationBuilder(title, comment, CHANNEL_ID, iconDrawable, this)

    builder
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_android_black_24dp, action, pendingIntent)

    createNotificationChannel(this,this.getString(R.string.app_name),CHANNEL_ID)

    with(NotificationManagerCompat.from(this)) {
        notify(1, builder.build())
    }

}

//Opens a destinity activity when user taps in it
fun Context.showNotificationTap(
    @IdRes iconDrawable: Int,
    title: String,
    comment: String,
    CHANNEL_ID: String = "11",
    activityClass: Class<*>,
    cleartTask: Boolean = false
) {

    val intent = Intent(this, activityClass).apply {
        if (cleartTask) flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    var builder = createNoficationBuilder(title, comment, CHANNEL_ID, iconDrawable, this)
    createNotificationChannel(this,this.getString(R.string.app_name),CHANNEL_ID)

    builder
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(this)) {
        notify(1, builder.build())
    }


}

fun Context.showNotificationReply(
    @IdRes iconDrawable: Int,
    @IdRes iconActionDrawable : Int,
    title: String,
    comment: String,
    action : String,
    CHANNEL_ID: String,
    activityClass: Class<*>
) {

    var REQUEST_CODE  = 2
    var NOTIFICATION_ID = 3
    val KEY_TEXT = "key_text_reaply"
    var replyLabel : String = this.getString(R.string.reply)

    createNotificationChannel(this, "reply", CHANNEL_ID)

    //Reply input builder
    var remoteInput :  androidx.core.app.RemoteInput =  androidx.core.app.RemoteInput.Builder(KEY_TEXT).run {
        setLabel(replyLabel)
        build()
    }

    val intent = Intent(this, activityClass)

    //Pending Intent
    var replyPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

    //Attach remote input
    // Create the reply action and add the remote input.
    var action: NotificationCompat.Action =
        NotificationCompat.Action.Builder(
            iconActionDrawable,
            action, replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

    var newMessageNotification = createNoficationBuilder(title,comment, CHANNEL_ID, iconDrawable, this).apply {
        addAction(action)
    }
    // Issue the notification.
    with(NotificationManagerCompat.from(this)) {
        notify(NOTIFICATION_ID, newMessageNotification.build())
    }



}

//Download notification sample
fun Context.showNotificationProgress(
    @IdRes iconDrawable: Int,
    title: String,
    comment: String,
    CHANNEL_ID: String = "11",
    notification_id : Int

) {


    createNotificationChannel(this,this.getString(R.string.app_name),CHANNEL_ID)
    var builder = createNoficationBuilder(title, comment, CHANNEL_ID, iconDrawable, this)

    val PROGRESS_MAX = 100
    val PROGRESS_CURRENT = 0

    //Use a background service/coroutine to update progress
    NotificationManagerCompat.from(this).apply {
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
        notify(notification_id, builder.build())

        builder
            .setContentText("Download Complete")
            .setProgress(0,0,false)

        notify(notification_id, builder.build())

    }

}

fun createNoficationBuilder(
    title: String,
    comment: String,
    CHANNEL_ID: String = "11",
    @IdRes iconDrawable: Int,
    context: Context
): NotificationCompat.Builder {

    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(iconDrawable)
        .setContentTitle(title)
        .setContentText(comment)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

}

fun createNotificationChannel(
    context: Context,
    channel_name: String = "app_channel",
    CHANNEL_ID: String
) {

    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = channel_name
        val descriptionText = context.getString(R.string.notification)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


@SuppressLint("ResourceType")
fun Context.showToast(@IdRes stringRes: Int) {
    Toast.makeText(this, resources.getString(stringRes), Toast.LENGTH_SHORT).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}



