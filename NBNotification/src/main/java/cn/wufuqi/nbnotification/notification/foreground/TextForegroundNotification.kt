package cn.wufuqi.nbnotification.notification.foreground

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.wufuqi.nbnotification.R
import cn.wufuqi.nbnotification.base.foreground.ForegroundNotification
import cn.wufuqi.nbnotification.utils.AppUtils

class TextForegroundNotification : ForegroundNotification() {

    private var mIvIcon: AppCompatImageView? = null
    private var mTvAppName: AppCompatTextView? = null
    private var mTvTitle: AppCompatTextView? = null
    private var mTvContent: AppCompatTextView? = null

    private var mResId = -1
    private var mAppName: String? = null
    private var mTitle: String? = null
    private var mContent: String? = null
    private var mGroupId = ""
    private var mAutoCancel = true
    private var mIntent: Intent? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.nbnotification_foreground_notification_text, container)
        initView(view)
        return view
    }


    private fun initView(view: View) {
        mIvIcon = view.findViewById(R.id.iv_icon)
        mTvAppName = view.findViewById(R.id.tv_app_name)
        mTvTitle = view.findViewById(R.id.tv_title)
        mTvContent = view.findViewById(R.id.tv_content)

        if (mAppName == null) {
            mAppName = AppUtils.getAppName()
        }

        if (mResId == -1) {
            mResId = AppUtils.getAppIcon()
        }
        if (mResId != -1) {
            mIvIcon?.setImageResource(mResId)
        }
        if (!mAppName.isNullOrEmpty()) {
            mTvAppName!!.text = mAppName
        }

        if (!mTitle.isNullOrEmpty()) {
            mTvTitle!!.text = mTitle
        }

        if (!mContent.isNullOrEmpty()) {
            mTvContent!!.text = mContent
        }
    }


    /**
     * 设置 app 小图标
     */
    fun setIcon(@DrawableRes resId: Int): TextForegroundNotification {
        if (isEnable()) {
            mIvIcon?.setImageResource(resId)
        }
        mResId = resId
        return this
    }

    /**
     * 设置app名
     */
    fun setAppName(appName: String): TextForegroundNotification {
        if (isEnable()) {
            mTvAppName?.text = appName
        }
        mAppName = appName
        return this
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String): TextForegroundNotification {
        if (isEnable()) {
            mTvTitle?.text = title
        }
        mTitle = title
        return this
    }

    /**
     * 设置内容
     */
    fun setContent(content: String): TextForegroundNotification {
        if (isEnable()) {
            mTvContent?.text = content
        }
        mContent = content
        return this
    }

    /**
     * 设置组id
     */
    fun setGroupId(groupId: String): TextForegroundNotification {
        mGroupId = groupId
        return this
    }

    /**
     * 设置 点击是否自动取消
     */
    fun setAutoCancel(autoCancel: Boolean): TextForegroundNotification {
        mAutoCancel = autoCancel
        return this
    }

    /**
     * 设置 点击是否自动取消
     */
    fun setIntent(intent: Intent?): TextForegroundNotification {
        mIntent = intent
        return this
    }

    override fun onClick() {
        if (mAutoCancel) {
            super.onClick()
        }
        if (mIntent != null) {
            getActivity()!!.startActivity(mIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ForegroundNotificationText.eventEmitter.emit(
            ForegroundNotificationText.EVENT_DESTROY_NAME,
            mGroupId
        )
    }

}