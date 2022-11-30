package cn.wufuqi.nbnotification

import cn.wufuqi.nbnotification.notification.bar.NotificationBar
import cn.wufuqi.nbnotification.notification.foreground.ForegroundNotificationText
import cn.wufuqi.nbnotification.utils.AppUtils

object TextNotificationExecute {

    fun execute(builder: NBNotification.Builder) {
        when (builder.notificationType) {
            NBNotificationType.FRONT_AND_NOTIFICATION_BAR -> {
                NotificationBar.notify(builder)
                ForegroundNotificationText.notify(builder)
            }
            NBNotificationType.FRONT -> {
                ForegroundNotificationText.notify(builder)
            }
            NBNotificationType.NOTIFICATION_BAR -> {
                NotificationBar.notify(builder)
            }
            NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR -> {
                if (!AppUtils.isRunningForeground()) {
                    NotificationBar.notify(builder)
                } else {
                    ForegroundNotificationText.notify(builder)
                }
            }
        }

    }
}