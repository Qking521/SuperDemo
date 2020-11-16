package com.king.superdemo.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import com.king.superdemo.R

class NotificationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
    }

    fun notification(v: View?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //step 1 create notification channel
        val channel = NotificationChannel(CHANNEL_ID_PUSH, "push", NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true) //是否在桌面icon右上角展示小圆点
        channel.lightColor = Color.RED //小圆点颜色
        channel.setShowBadge(true) //长按桌面图标时是否显示此渠道的通知信息
        notificationManager.createNotificationChannel(channel)

        //step 2 create notification
        val builder = Notification.Builder(this, CHANNEL_ID_PUSH)
        builder.setTicker("ticker") //状态栏 快速显示
        builder.setSubText("subText") //通知栏 应用名后面显示
        builder.setContentTitle("title") //通知栏 标题
        builder.setContentText("summary") //通知栏 内容
        builder.setColor(Color.BLUE) //通知栏应用名前面图片颜色色
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setLargeIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground))

        //step 3 send notification
        val notification = builder.build()
        notificationManager.notify(1, notification)
    }

    companion object {
        const val CHANNEL_ID_PUSH = "push"
    }
}