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