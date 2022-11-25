package cn.wufuqi.nbnotification.foreground

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.wufuqi.nbnotification.R

class TestForegroundNotification:ForegroundNotification() {

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.nbnotification_foreground_notification_test,container)
    }
}