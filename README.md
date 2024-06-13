# AndroidCode中App是第一周的作业
日志打印Hellow world

# AndroidCode中week2是第二周的作业

写了一个简单的登陆页面，用户输入用户名和密码时会先判断是否为空，若为空则页面弹出警告，若都不为空则将数据传到下一个Activity并显示

# AndroidCode中sqlitetest是第三周的作业

写了一个页面，里面有两个按钮，点击第一个按钮访问远程url中的json数据，并用Gson解析json数据后存到sqlite数据库中；点击第二个按钮将数据库中的数据展示到页面中。

有一些注意事项：

- AndroidManifest.xml中需要添加
  - <uses-permission android:name="android.permission.INTERNET"/>
  - <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

- 访问远程url时要关闭vpn，关闭tizi
