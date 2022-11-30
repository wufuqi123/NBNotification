package cn.wufuqi.nbnotification

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.DrawableRes
import cn.wufuqi.nbnotification.utils.AppUtils


object NBNotification {


    fun notification(builder: Builder) {
        TextNotificationExecute.execute(builder)
    }

    class Builder {

        /**
         * app 图标
         */
        @DrawableRes
        var resId = -1

        /**
         * app 名
         */
        var appName = ""

        /**
         * 标题
         */
        var title = ""

        /**
         * 内容
         */
        var content = ""

        /**
         * 组id
         */
        var groupId = 0

        /**
         * 点击调换意图
         */
        var intent: Intent? = null

        /**
         * 点击后是否自动消失
         */
        var autoCancel = true

        /**
         * 通知类型
         */
        var notificationType = NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR


        fun build(): Builder {
            if (resId == -1) {
                resId = AppUtils.getAppIcon()
            }

            if (appName.isEmpty()) {
                appName = AppUtils.getAppName()
            }
            return this
        }

    }
}