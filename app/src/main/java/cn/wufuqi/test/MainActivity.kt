package cn.wufuqi.test

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.wufuqi.nbnotification.foreground.TestForegroundNotification


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val n = TestForegroundNotification()
        findViewById<View>(R.id.btn).setOnClickListener {
            n.show(this)
        }

    }

}