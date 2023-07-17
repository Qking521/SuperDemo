package com.king.superdemo.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import com.king.superdemo.R


class NotificationActivity : BaseActivity() {

    val CHANNEL_ID_PUSH = "push"
    val NOTIFICATION_ID_PUSH = 1

    val CHANNEL_ID_FLOAT = "float"
    val NOTIFICATION_ID_FLOAT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
//        requestPermission(PermissionBean(PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE, PermissionCallback {  }))
    }

    fun showNormalNotification(v: View?) {
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
        builder.setColor(Color.BLUE) //通知栏应用名前面图片颜色
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setLargeIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground))

        //step 3 send notification
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID_PUSH, notification)
    }

    fun showHeadsUpNotification(v: View?) {
        Log.v("wq", "showHeadsUpNotification: ")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID_FLOAT, "float", NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true) //是否在桌面icon右上角展示小圆点
        channel.lightColor = Color.RED //小圆点颜色
        channel.setShowBadge(true) //长按桌面图标时是否显示此渠道的通知信息
        notificationManager.createNotificationChannel(channel)

        val fullScreenPendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com")),
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder: Notification.Builder = Notification.Builder(this, CHANNEL_ID_FLOAT)
            .setSmallIcon(android.R.drawable.arrow_down_float)
            .setPriority(Notification.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setContentTitle("Notification title")
            .setContentText("Notification text")
            .setFullScreenIntent(fullScreenPendingIntent, true)
        notificationManager.notify(NOTIFICATION_ID_FLOAT, notificationBuilder.build());
    }

    fun isNotificationEnable() : Boolean{
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    fun goToNotificationSetting(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(intent)
        } else {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.applicationContext.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            context.startActivity(intent)
        }
    }
}