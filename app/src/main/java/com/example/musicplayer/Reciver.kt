package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.musicplayer.AppConstants.Companion.PAUSE_ACTION
import com.example.musicplayer.AppConstants.Companion.PLAY_ACTION


/**
 * Created by Khush Pajwani.
 * Created on 10/11/2022
 */
class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val whichAction = intent.action
        when (whichAction) {

            PLAY_ACTION -> {
                Log.e("TAG", "onReceive: play")
                startPlaying()
                return
            }

            PAUSE_ACTION -> {
                Log.e("TAG", "onReceive: pause")
                pausePlaying()
                return
            }

        }
    }

    companion object {
        var service: NotificationService? = null
    }
}