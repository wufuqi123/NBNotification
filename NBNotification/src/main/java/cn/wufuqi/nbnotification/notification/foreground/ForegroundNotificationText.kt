package cn.wufuqi.nbnotification.notification.foreground

import cn.wufuqi.convertmainthread.ConvertMainThread
import cn.wufuqi.eventemitter.EventEmitter
import cn.wufuqi.nbnotification.NBNotification
import cn.wufuqi.nbnotification.utils.ActivityUtils
import java.util.*

object ForegroundNotificationText {
    val eventEmitter by lazy { EventEmitter() }
    const val EVENT_DESTROY_NAME = "EVENT_DESTROY_NAME"

    private val mBuilderGroupQueueMap = mutableMapOf<Int, Queue<NBNotification.Builder>>()
    private val mForegroundNotificationGroupMap = mutableMapOf<String, TextForegroundNotification>()

    init {
        eventEmitter.on(EVENT_DESTROY_NAME) {
            val groupId = it[0] as String
            val a = mForegroundNotificationGroupMap.remove(groupId)
            val gInt = groupId.substring(groupId.length - 1, groupId.length)
                .toInt()
            val nbBuilder =
                mBuilderGroupQueueMap[gInt]!!.poll()
            if (nbBuilder != null) {
                val cnG = ActivityUtils.getTopActivityClassName() + gInt
                val foregroundNotification = createTextForegroundNotification(nbBuilder, cnG)
                mForegroundNotificationGroupMap[groupId] = foregroundNotification
                foregroundNotification.show()
            }
        }
    }


    fun notify(nbBuilder: NBNotification.Builder) {
        ConvertMainThread.runOnUiThread {


            val groupId = nbBuilder.groupId
            val cnG = ActivityUtils.getTopActivityClassName() + groupId
            var foregroundNotification = mForegroundNotificationGroupMap[cnG]

            var queue = mBuilderGroupQueueMap[groupId]
            if (queue == null) {
                queue = LinkedList()
                mBuilderGroupQueueMap[groupId] = queue
            }

            if (foregroundNotification == null) {
                foregroundNotification = createTextForegroundNotification(nbBuilder, cnG)
                mForegroundNotificationGroupMap[cnG] = foregroundNotification
                foregroundNotification.show()
                return@runOnUiThread
            }
            queue.add(nbBuilder)
            if (foregroundNotification.isEnable()) {
                val b = queue.poll()
                foregroundNotification.stopWait()
                setBuild(foregroundNotification, b, cnG)
                foregroundNotification.startWait()
            }
        }
    }

    private fun createTextForegroundNotification(
        nbBuilder: NBNotification.Builder,
        cnG: String
    ): TextForegroundNotification {
        return TextForegroundNotification().apply {
            setBuild(this, nbBuilder, cnG)
        }

    }

    private fun setBuild(
        n: TextForegroundNotification,
        nbBuilder: NBNotification.Builder,
        cnG: String
    ) {
        n.setIcon(nbBuilder.resId)
            .setAppName(nbBuilder.appName)
            .setGroupId(cnG)
            .setTitle(nbBuilder.title)
            .setContent(nbBuilder.content)
            .setIntent(nbBuilder.intent)
            .setAutoCancel(nbBuilder.autoCancel)
    }


}