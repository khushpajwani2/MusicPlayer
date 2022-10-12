package com.example.musicplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat


/**
 * Created by Khush Pajwani.
 * Created on 10/11/2022
 */
class NotificationService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        stopSelf();
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals(AppConstants.STARTFOREGROUND_ACTION)) {
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
            val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getActivity(mContext, 0, intent, 0)
            }

            val playIntent = Intent(mContext, Receiver::class.java)
            playIntent.action = AppConstants.PLAY_ACTION
            playIntent.putExtra(AppConstants.NOTIFICATION_ID.toString(), 0)

            val playPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(mContext, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getBroadcast(mContext, 0, playIntent, 0)
            }
            val pauseIntent = Intent(mContext, Receiver::class.java)
            pauseIntent.action = AppConstants.PAUSE_ACTION
            pauseIntent.putExtra(AppConstants.NOTIFICATION_ID.toString(), 0)

            val pausePendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(mContext, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getBroadcast(mContext, 0, pauseIntent, 0)
            }

            val builder: Notification =
                NotificationCompat.Builder(mContext, AppConstants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notificaiton)
                    .setContentTitle("textTitle")
                    .setContentText("textContent")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(
                        R.drawable.ic_play, mContext.getString(R.string.play),
                        playPendingIntent
                    )
                    .addAction(
                        R.drawable.ic_pause, mContext.getString(R.string.pause),
                        pausePendingIntent
                    )
                    .setAutoCancel(true)
                    .build()

            startForeground(1, builder)


        } else if (intent?.action.equals(AppConstants.PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show()
            Log.i("LOG_TAG", "Clicked Previous")
        } else if (intent?.action.equals(AppConstants.PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show()
            Log.i("LOG_TAG", "Clicked Play")
        } else if (intent?.action.equals(AppConstants.NEXT_ACTION)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show()
            Log.i("LOG_TAG", "Clicked Next")
        } else if (intent?.action.equals(
                AppConstants.STOPFOREGROUND_ACTION
            )
        ) {
            Log.i("LOG_TAG", "Received Stop Foreground Intent")
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show()
            stopForeground(true)
            stopSelf();
        }
        return START_STICKY
    }
}