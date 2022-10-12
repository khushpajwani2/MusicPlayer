package com.example.musicplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicplayer.AppConstants.Companion.CHANNEL_ID
import com.example.musicplayer.AppConstants.Companion.NOTIFICATION_ID
import com.example.musicplayer.AppConstants.Companion.STARTFOREGROUND_ACTION
import com.example.musicplayer.ui.theme.MusicPlayerTheme


var mMediaPlayer: MediaPlayer? = null
lateinit var mContext: Context
lateinit var serviceIntent: Intent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Declaring and Initializing
                    // the MediaPlayer to play "audio.mp3"
                    mMediaPlayer = MediaPlayer.create(this, R.raw.audio)
                    MusicPlayer(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.audio)
        }
    }

    override fun onDestroy() {
        if (serviceIntent != null) {
            stopPlaying()
            mContext.stopService(serviceIntent)
        }
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(intent: Intent) {
    Scaffold(
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                MyContent(intent)
            }
        }
    )
}

@Composable
fun MyContent(intent: Intent) {

    // Fetching the local context
    mContext = LocalContext.current

    LaunchedEffect(Unit) {
        createNotificationChannel()
    }
    val uri: Uri? = intent.data
    var deepLinkMsg = "khush"
    if (uri != null) {
        // if the uri is not null then we are getting the
        // path segments and storing it in list.
        val parameters: List<String> = uri.pathSegments

        // after that we are extracting string from that parameters.
        val param = parameters[parameters.size - 1]

        // on below line we are setting
        // that string to our text view
        // which we got as params.
        deepLinkMsg = param
    }
    Log.e("TAG", "MyContent: $deepLinkMsg")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {

        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // IconButton for Start Action
            Column(Modifier.weight(1f)) {
                IconButton(
                    onClick = { startPlaying() },
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "",
                        Modifier.size(100.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // IconButton for Pause Action
            Column(Modifier.weight(1f)) {
                IconButton(
                    onClick = { pausePlaying() },
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_pause),
                        contentDescription = "",
                        Modifier.size(100.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // IconButton for Stop Action
            Column(Modifier.weight(1f)) {
                IconButton(
                    onClick = { stopPlaying() },
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_stop),
                        contentDescription = "",
                        Modifier.size(100.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // IconButton for Stop Action
            Column(Modifier.weight(1f)) {
                IconButton(
                    onClick = { showClickableNotification() },
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_notificaiton),
                        contentDescription = "",
                        Modifier.size(100.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

fun showClickableNotification() {
    serviceIntent = Intent(mContext, NotificationService::class.java)
    serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
    serviceIntent.action = STARTFOREGROUND_ACTION
    mContext.startService(serviceIntent)

    /*val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(mContext, 0, intent, 0)
    }

    val snoozeIntent = Intent(mContext, Receiver::class.java)
    snoozeIntent.action = STARTFOREGROUND_ACTION
    snoozeIntent.putExtra(NOTIFICATION_ID.toString(), 0)

    val snoozePendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.getBroadcast(mContext, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getBroadcast(mContext, 0, snoozeIntent, 0)
    }

    val builder = NotificationCompat.Builder(mContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notificaiton)
        .setContentTitle("textTitle")
        .setContentText("textContent")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_play, mContext.getString(R.string.play),
            snoozePendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(mContext)) {
        notify(NOTIFICATION_ID, builder.build())
    }*/
}

fun showSimpleNotification() {
    // Building the notification
    var builder = NotificationCompat.Builder(mContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notificaiton)
        .setContentTitle("My notification")
        .setContentText("Much longer text that cannot fit one line...")
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line...")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(mContext)) {
        notify(NOTIFICATION_ID, builder.build())
    }
}

fun startPlaying() {
    mMediaPlayer?.start()
}

fun createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = mContext.getString(R.string.channel_name)
        val descriptionText = mContext.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun pausePlaying() {
    mMediaPlayer?.pause()
}

fun stopPlaying() {
    mMediaPlayer?.seekTo(0)
    mMediaPlayer?.pause()
}


/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicPlayerTheme {
        MusicPlayer(intent)
    }
}*/
