Feature:首页登录
  黄盖是老用户，从首页登录，去个人主页管理页面
  
  #杨小澎 正在实现
  Scenario: 首页登录成功
     Given 黄盖在dhome的注册邮箱是"xiaoming@163.com"
     Given 黄盖在dhome的登录密码是"123456"
     When 黄盖点击"登录"按钮
     Then 页面转向"个人主页管理"页面
     
  Scenario: 首页登录账户不存在
     Given 绿盖输入的邮箱是"xiaoming1@163.com"
     But 邮箱"xiaoming1@163.com"在系统中不存在
     When  绿盖点击"登录"按钮
     Then 绿盖看到提示信息"登录名不存在，请注册"

  Scenario: 首页登录账户密码错误
     Given 红盖在dhome的注册邮箱是"xiaoming@163.com"
     Given 红盖在dhome的登录密码是"123456"
     But 红盖的输入的密码是"12345"
     When 红盖点击"登录"按钮
     Then 红盖看到提示信息"密码错误"
          
 Scenario: 忘记密码
     When 盖中盖点击"忘记密码"链接
     Then 页面转向"密码重置"页面