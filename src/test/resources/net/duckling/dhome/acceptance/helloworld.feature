Feature: 协同中心BDD开发样例

  Scenario: Say hello
    Given 我问候了下 "Howdy"
    When I ask it to say hi
    Then it should answer with "Howdy World"

  Scenario: Print my shopping list
    The list should be printed in alphabetical order of the item names

    Given a shopping list:
      | name  | count |
      | Milk  |     2 |
      | Cocoa |     1 |
      | Soap  |     5 |
    When I print that list
    Then it should look like:
      """
      1 Cocoa
      2 Milk
      5 Soap

      """

    
    
  Scenario: 快速注册
  	看看这个例子
    Given 我的Email地址是"name@163.com"
    And 我的密码是"123456"
    When 当我点击"创建页面"按钮
    Then 我应该看到"成功"
    And 我跳转到"新建页面"
    