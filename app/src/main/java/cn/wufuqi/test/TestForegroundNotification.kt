package cn.wufuqi.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.wufuqi.nbnotification.base.foreground.ForegroundNotification

// 继承 ForegroundNotification
class TestForegroundNotification : ForegroundNotification() {

    //重写 onCreateView 方法
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        // 把 foreground_notification_test xml 添加到container种，并返回view
        return inflater.inflate(R.layout.foreground_notification_test, container)
    }
}