Feature:首页
 
 
 #小明已经登录，此时在任何一个页面，点击了首页链接，系统应该给他转到个人页面
 Scenario: 首页登录状态判断
      Given  小明的状态是"登录"
      When  小明点击"首页"链接
      Then  小明进入"个人主页管理"页面
      
 Scenario: 首页登录状态判断
      Given  小明的状态是"登录"
      When  小明点击"首页"链接
      Then  小明进入"个人主页管理"页面     
      
 Scenario: 最新创建个人主页展示
      Given 个人主页列表是:
      | page_id  | name    | created_time   |
      | 103      | xiaoming| 2012-2-11 1:00 |
      | 10       |   lilei | 2012-5-12 18:00|
      | 1        | wanghai | 2012-3-12 12:00|
      | 5        |    Lily | 2012-7-12 12:00|
      | 7        |    kevin| 2012-8-12 12:00|
     Given 要显示的列表个数项目个数是4
     Given 排序方式是"时间"
      When  小明进入"首页"
      Then  小明看到按照样式c1.css排列的最新创建个人主页展示列表:
      | page_id  | name    | created_time   |
      | 7        |    kevin| 2012-8-12 12:00|
      | 5        |    Lily | 2012-7-12 12:00|
      | 10       |   lilei | 2012-5-12 18:00|
      | 1        | wanghai | 2012-3-12 12:00|

    
 