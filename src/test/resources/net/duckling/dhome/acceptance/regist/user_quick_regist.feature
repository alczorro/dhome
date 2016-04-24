Feature: 首页快速注册
  我是一个新用户，在首页浏览了一些主页之后，发现很有意思，也想创建自己的个人科研主页。
  #zhaojuan modifed
  @finished 
  Scenario: 首页快速注册成功
      Given 小明的输入真实姓名是"小明"
      And 小明的输入邮箱地址是"zhaojuan@cnic.cn"
      When 小明点击"开始创建主页"按钮
      Then 小明跳到了"个人研究主页创建"页面
      
  Scenario: 注册邮箱地址已存在
      Given 小红的输入邮箱地址是"liming@163.com"
      But  小红的输入邮箱地址已经被注册
      When 小红点击"开始创建主页"按钮
      Then 小红看到提示信息"邮件地址已存在，请用其它邮件地址注册；如果您要登录，请进入登陆页面"
      
 # Scenario: 注册邮箱非法
 #     Given 小黑的输入邮箱地址是"liming"
 #     When 小黑离开输入框
 #     Then 小黑看到提示信息"邮箱地址非法"
   

   