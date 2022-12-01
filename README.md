```
    一个很牛逼的通知，不止可以显示在通知栏，在app开启的状态下可以显示在app顶部
```

#### 基础功能
1. 添加依赖

    请在 build.gradle 下添加依赖。

    ``` 
        implementation 'cn.wufuqi:NBNotification:1.0.1'
    ```


2. 设置jdk8或更高版本

    因为本sdk使用了jdk8才能使用的 Lambda 表达式，所以要在 build.gradle 下面配置jdk8或以上版本。

    ``` 
    android {
        ....

        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
        
    }
    ```

3. 使用方式

    打开一个通知，如果app在前台显示则显示在app内部，如果app在后台，则显示在通知栏。

    ```
        val build = NBNotification.Builder().apply {
            title = "标题"
            content = "内容....."
        }.build()
        NBNotification.notification(build)

    ```


## 后台通知演示
#### ![后台通知](https://raw.githubusercontent.com/wufuqi123/NBNotification/master/img/bar.png)


## 前台通知演示
#### ![前台通知](https://raw.githubusercontent.com/wufuqi123/NBNotification/master/img/front.png)

## 通知使用

#### 一个简单的标题文本通知

   打开一个通知，如果app在前台显示则显示在app内部，如果app在后台，则显示在通知栏。

    ```
        val build = NBNotification.Builder().apply {
            title = "标题"
            content = "内容....."
        }.build()
        NBNotification.notification(build)

    ```

#### 一个带跳转的通知

   打开一个通知，如果app在前台显示则显示在app内部，如果app在后台，则显示在通知栏。

    ```
        val build = NBNotification.Builder().apply {
            title = "标题"
            content = "内容....."
            intent = Intent(context,class)
        }.build()
        NBNotification.notification(build)

    ```

#### 一个更改图标喝app名的通知

    打开一个通知，如果app在前台显示则显示在app内部，如果app在后台，则显示在通知栏。

    ```
        val build = NBNotification.Builder().apply {
            title = "标题"
            content = "内容....."
            //不写默认当前app图标
            resId = id
            // 不写默认当前app名
            appName = ""
        }.build()
        NBNotification.notification(build)

    ```

#### 一个更改通知显示类型的通知

   设置通知显示类型，不写默认   FRONT_INTERLACE_NOTIFICATION_BAR

   /**
    * 前台通知和通知栏通知交错显示
    * 如果app在前台，则显示前台通知
    * 如果app在后台，则显示通知栏通知
    */
   NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR

   /**
    * 只显示前台通知
    */
   NBNotificationType.FRONT

   /**
    * 只显示通知栏通知
    */
   NBNotificationType.NOTIFICATION_BAR


   /**
    * 前台通知和通知栏通知一起显示
    */
   NBNotificationType.FRONT_AND_NOTIFICATION_BAR

    ```
        val build = NBNotification.Builder().apply {
            title = "标题"
            content = "内容....."
            // 不写默认 
            NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR
            notificationType = NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR
        }.build()
        NBNotification.notification(build)

    ```

## 自定义前台通知
   
   目前自定义的前台通知无法使用  NBNotification.notification(build) 去显示。需要自己去创建自己的的通知的实例，调用show()方法
   

#### 新建前台通知


##### 创建 class
   ```
        // 继承 ForegroundNotification
        class TestForegroundNotification : ForegroundNotification() {
        
            //重写 onCreateView 方法
            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
                // 把 foreground_notification_test xml 添加到container种，并返回view
                return inflater.inflate(R.layout.foreground_notification_test, container)
            }
        }
   ```

##### 创建 xml

    foreground_notification_test xml

    ```
        <?xml version="1.0" encoding="utf-8"?>
        <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_marginStart="10dp"
        android:layout_marginEnd="10dp"
        android:layout_height="wrap_content">
        
            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:gravity="center"
                android:textColor="#ff0000"
                android:textSize="35dp"
                android:text="555555"
                android:background="#00ff00"/>
        </RelativeLayout>
    ```


##### 调用显示

    把 TestForegroundNotification 显示在前台：

    ```
        // 创建实例对象
        val tfn = TestForegroundNotification()
        // 把前台通知显示在最顶部的activity上。
        tfn.show()
    ```
   


### ForegroundNotification 生命周期


    onCreate(context: Context)          ：   前台通知被创建
    
    onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View                             ：   前台通知 view 被创建


    onDestroyView(v: View)              ：   前台通知  view 被销毁

    
    onDestroy()                         ：   前台通知被销毁


    onMove(offsetY: Float)              ：   移动时调用，建议调用super方法，否则通知不会移动

    onClick()                           ：   点击时调用，建议调用super方法，否则通知点击不会取消

    onTouch(event: MotionEvent)         ：   Touch  事件，建议调用super方法，否则通知Touch无反应。


### ForegroundNotification 建议重写的方法

    getBottomOffsetMoveY():Float        ：   获取 底部移动时偏移的 Y值

    buildAnimationShow(
        time: Long,
        bottomY: Float
    ): SequenceActionBuild              ：   创建前台通知显示动画

    buildAnimationHide(
        time: Long, 
        topY: Float
    ): SequenceActionBuild              ：   创建隐藏动画

    getMoveTime(): Long                 ：   移动时的时间

    getWaitTime(): Long                 ：   等待时间，负数为  不会自动隐藏

##### [SequenceActionBuild](https://github.com/wufuqi123/WuKongAnimation) 是 WuKongAnimation（悟空动画）的属性动画。
    
    使用方式：
    
    ```
        //创建一个顺序动画，按照顺序一个一个执行
        SequenceActionBuild()
            .moveTo(time,x,y)
            .fadeIn(time)
            .fadeOut(time)
            // 添加一个同步动画，里面创建的一起执行
            .spawn { 
                // 一起执行移动和旋转
                SpawnActionBuild()
                    .moveBy(time, x, y)
                    .rotateBy(time, rotation)
            }
            .callFunc{
                // 当前 callFunc 是添加到最后的，所以这里面的代码，最后执行。
                // 这相当于动画执行完成的回调了
                // .callFunc{} 可以在 SequenceActionBuild() 地方任意插入一个位置，执行时间为那个位置上一个动画执行完成后执行。
                // 在末尾插入则为：动画执行完成的回调。
            }
    ```

    
### ForegroundNotification 方法

    isEnable():Boolean                  ：   是否可使用

    getActivity(): Activity?            ：   获取 Activity 对象

    show(activity: Activity)            ：   指定activity 显示前台通知

    show()                              ：   在顶部activity  显示前台通知

    hide()                              ：   隐藏前台通知

    startWait()                         ：   开始等待

    startWait(waitTime:Long)            ：   开始等待，并设置等待时间

    startWait(
        isWait:Boolean,
        waitTime:Long
    )                                   ：   开始等待，并设置等待时间，并设置是否可以等待

    stopWait()                          ：   结束等待


### ForegroundNotification 属性

    mContentView: View?                 ：   当前通知内的显示View，onCreateView 返回的view

    isShow: Boolean                     ：   是否正在显示

    isAnimationRunning: Boolean         :   是否正在执行动画

    easing                              ：   如果不自定义动画，默认的缓动函数

    currStatusBarHeight: Int            ：   当前状态栏的高度