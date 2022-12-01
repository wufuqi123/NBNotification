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
            // 不写默认 NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR
            notificationType = NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR
        }.build()
        NBNotification.notification(build)

    ```