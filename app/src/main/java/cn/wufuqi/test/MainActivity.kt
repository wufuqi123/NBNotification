package cn.wufuqi.test

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.wufuqi.easytimer.EasyTimer
import cn.wufuqi.nbnotification.NBNotification
import cn.wufuqi.nbnotification.NBNotificationType
import cn.wufuqi.nbnotification.notification.foreground.TextForegroundNotification
import com.gyf.immersionbar.ImmersionBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            //状态栏颜色，不写默认透明色
            .statusBarColor("#ffffff")
            .autoStatusBarDarkModeEnable(true, 0.2f)
//            .transparentStatusBar()  //透明状态栏，不写默认透明色
//            .transparentNavigationBar()
            .init()

        val n = TextForegroundNotification()
        findViewById<View>(R.id.btn).setOnClickListener {
//            n.show(this)

            notification()
        }

        EasyTimer().scheduleUI(5200) {
//            val build = NBNotification.Builder().apply {
//                title = "换电提醒"
//                content = "[上汽大通]到你换电啦！请点击大屏上的开始换电，并将车辆停在换电房中的换电车位。未在*分钟内到达换电站将视作过号哦~"
//
//            }.build()
//            NBNotification.notification(build)
            notification()
        }

    }

    var index = 0

    @SuppressLint("UnspecifiedImmutableFlag")
    fun notification() {

        index += 1
        val build = NBNotification.Builder().apply {
            title = "换电提醒"
            notificationType = NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR
            content = "[上汽大通]到你换电啦！请点击大屏上的开始换电，并将车辆停在换电房中的换电车位。未在${index}分钟内到达换电站将视作过号哦~"
            intent = Intent(this@MainActivity, TestActivity::class.java)
        }.build()
        NBNotification.notification(build)
    }


}