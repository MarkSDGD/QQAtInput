# QQAtInput

[![](https://jitpack.io/v/MarkSDGD/QQAtInput.svg)](https://jitpack.io/#MarkSDGD/QQAtInput)

## 仿QQ at 功能

最近在做即时通讯功能，在开发之余，封装了一个仿QQ at 功能组件。可以用在聊天，评论等模块中。该组件主要解决两个**难点问题**:
1. `@选取的成员作为一个整体，不可编辑，支持整体删除；`
2. `支持spannable类型存储，@内容可以保存草稿以及恢复草稿功能；`

如果觉得对你有帮助的话，请帮忙 **star** 一下！

## 现有功能

1.@选取的成员作为一个整体，不可编辑，支持整体删除；

2.支持spannable类型存储，@内容可以保存草稿以及恢复草稿功能；

3.支持设置@响应模式（文本中最后位置输入@ 触发和 文本中任意位置插入@触发），默认是第二种,光标位置自适应；

4.支持草稿恢复的数据增加标记，最后一位为@时不触发响应；

5.支持设置span整体块的背景和文字颜色；

6.支持获取span块中的内容以及对应的成员id信息，不需要代码中单独维护一个成员id列表，易拓展，易维护。

7.解决android中文字内嵌图片会导致输入框高度变化的问题。

8.demo 可以限制重复@某个人，默认关闭;

9.demo 可以控制键盘显示与隐藏


## demo apk下载

[[点击下载体验](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/QQAtInput.apk)]

扫码下载体验：

![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/download_qrcode.png)


## 演示截图

### 输入文本以及获取对应成员id
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/inputAndMemberId.png)

### 成员选择
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/memberSelect.png)


## 录屏gif

### 操作视频录屏
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/QQAtInputVideo.gif)


## 导入方式
### 项目根目录下build.gradle文件添加JitPack
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

###  app目录下build.gradle文件添加依赖项
```
dependencies {
    implementation 'com.github.MarkSDGD:QQAtInput:1.0.5'
}
```

###  使用方式
```
<com.mark.atlibrary.AtEditText
        android:layout_marginTop="30dp"
        android:id="@+id/chat_edit"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/shape_corner_edittext_bg"
        android:maxLines="6"
        android:focusable="true"
        android:focusableInTouchMode="true"
        android:text=""
        android:hint="请输入消息"
        android:textSize="16dp" />
```

###  api说明
####  @模式设置获取
```

     public void setOnlySupportLastAt(boolean onlySupportLastAt)

     public boolean isOnlySupportLastAt()

```
####  添加一个@块
```

     public void addSpan(String showText, int spanBgResId, int textColor, String userId,int maxEms)

```

####  获取输入文本中所有成员的id
```

     public String getUserIdString()

```

####  存草稿恢复草稿相关方法
```

     public String spannableString2JsonString(SpannableString ss)

     public SpannableString jsonString2SpannableString(String strjson)

```



## 实现原理

1. 首先将 **@李白** 字符串生成图片，在文本中插入图片span，每个图片span存储用户文本，id等信息；根据@模式，计算插入图片span后的光标位置；

2. 由于复杂文本无法直接存储，首先将复杂文本转换成整个普通文本+所有span块关键信息（普通文本中的起始位置，span块文字，id, 背景资源，文字颜色），然后将这些信息转换成json对象，再把json对象转成字符串即可。

3. 恢复草稿的时候，进行相反的操作，根据存储的整个文本，加上所有span块的关键信息，重新构建图片span块插入到文本中即可

## 声明

此组件属于个人开发作品，目前已满足大部分使用场景，如有个别场景，请下载源码自行更改。

## 支持与鼓励
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/donate.png)
