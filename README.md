# 仿QQ at 功能

今年项目中增加了即时通讯功能，在开发之余，封装了一个仿QQ at 功能组件。可以用在聊天，评论等模块中。该组件主要解决两个难点问题:
1. @选取的成员作为一个整体，不可编辑，支持整体删除；
2. 支持spannable类型存储，@内容可以保存草稿以及恢复草稿功能；
如果觉得对你有帮助的话，请帮忙star一下！

# 现有功能

1.@选取的成员作为一个整体，不可编辑，支持整体删除；

2.支持spannable类型存储，@内容可以保存草稿以及恢复草稿功能；

3.支持设置@响应模式（文本中最后位置输入@ 触发和 文本中任意位置插入@触发），默认是第二种,光标位置自适应；

4.支持草稿恢复的数据增加标记，最后一位为@时不触发响应；

5.支持设置span整体块的背景和文字颜色；

6.支持获取span块中的内容以及对应的成员id信息，不需要代码中单独维护一个成员id列表，易拓展，易维护。

7.解决android中文字内嵌图片会导致输入框高度变化的问题。

8.demo 可以限制重复@某个人，默认关闭;

9.demo 可以控制键盘显示与隐藏

具体使用方式可以参考demo.


# Apk下载地址

[[点击下载体验](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/QQAtInput.apk)]

扫码下载体验：

![](https://raw.githubusercontent.com/chaychan/TouTiaoPics/master/screenshot/apk_qrcode.png)


# 演示截图

## 输入文本以及获取对应成员id
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/inputAndMemberId.png)

## 成员选择
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/memberSelect.png)


# 录屏

## 操作视频录屏
![](https://raw.githubusercontent.com/MarkSDGD/repositoryResources/main/QQAtInput/QQAtInputVideo.mp4)


# 声明
此组件属于个人开发作品，目前已满足大部分使用场景，如有个别场景，请下载源码自行更改。