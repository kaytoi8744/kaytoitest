package com.example.testapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.app.NotificationCompat

class GPSForegroundService : Service() {

    private lateinit var gpsLocationManager: GPSLocationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // GPSLocationManagerの初期化
        gpsLocationManager = GPSLocationManager(this)
        gpsLocationManager.startLocationUpdates(object : GPSLocationManager.MyLocationCallback {
            override fun onLocationResult(location: Location?) {
                location?.let {
                    // 必要に応じて、位置情報を他のコンポーネントやサーバーに送信
                    Log.d("GPS","Location: ${it.latitude}, ${it.longitude}")
                }
            }

            override fun onLocationError(error: String) {
                println("Location Error: $error")
            }
        })

        // フォアグラウンドサービスとして通知を表示
        startForegroundServiceWithNotification()
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "GPSServiceChannel"
        val channelName = "GPS Service Channel"

        // 通知チャンネルを作成
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)

        // メインアクティビティを起動するPendingIntent
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        // 通知の作成
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("GPS 位置情報サービス")
            .setContentText("位置情報を取得しています")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        // フォアグラウンドサービスとして通知を表示
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        // GPSの位置情報取得を停止
        gpsLocationManager.stopLocationUpdates()
        stopForeground(true) // 通知の削除
        stopSelf() // サービスの停止
    }

    override fun onBind(intent: Intent?) = null
}
