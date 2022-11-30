package cn.wufuqi.nbnotification

enum class NBNotificationType {

    /**
     * 只显示前台通知
     */
    FRONT,

    /**
     * 只显示通知栏通知
     */
    NOTIFICATION_BAR,

    /**
     * 前台通知和通知栏通知一起显示
     */
    FRONT_AND_NOTIFICATION_BAR,

    /**
     * 前台通知和通知栏通知交错显示
     * 如果app在前台，则显示前台通知
     * 如果app在后台，则显示通知栏通知
     */
    FRONT_INTERLACE_NOTIFICATION_BAR,
}