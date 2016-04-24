Feature:个人主页设置向导-头像设置

  Scenario: 头像上传
       Given 李晨的照片是"my.jpg"
       When 李晨点击"上传头像"按钮
       Then 李晨在头像显示区域看到我的头像变成了三种尺寸"my_size1.jpg","my_size2.jpg","my_size3.jpg"
  Scenario: 完成头像设置
       Given 王晨上传了头像"my.jpg"
       When 王晨点击"完成"按钮
       Then 王晨进入了"个人主页预览"页面