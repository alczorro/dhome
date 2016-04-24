Feature:手动添加新论文
     王雷要手动添加新论文
    
 Scenario: 手动添加新论文
      Given 王雷_1输入论文的标题是"This is a new paper"
      And 王雷_1输入论文的标题是"This is a new paper"
      And 王雷_1输入论文的作者是"Lei Wang,Ming Li"
      And 王雷_1输入的来源是"Journal of Science" 
      And 王雷_1输入的期和卷是"3(2)"
      And 王雷_1选择的出版时间是"2012.12" 
      And 王雷_1输入的原文链接是"www.acm.portal?id=1010" 
      And 王雷_1上传原文CLB的ID是"123"
      When  王雷_1点击"添加"按钮
      Then  显示"添加成功"信息
      