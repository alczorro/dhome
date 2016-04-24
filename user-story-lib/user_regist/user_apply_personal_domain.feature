Feature: 个人研究主页创建

为了能够创建个人主页，作为一名访客，李明希望注册网站并创建个人主页。
    
    @java_enable
	Scenario: 个人研究主页创建_1_用户输入姓名自动提取拼音
		Given 李明填写了真实姓名"李明"
		When 李明离开输入姓名输入框时
		Then 李明的姓名拼音"liming"被自动提取出来   
		
	@java_enable	
	Scenario: 用户填写正确数据注册和创建主页
	    Given 安小糖输入的真实姓名是"安小糖"
	    And 安小糖输入英文名是"xiaotang"
        And 安小糖的输入邮箱地址是"liming@163.com"
        And 安小糖的输入注册密码是"123456"
	    And 安小糖的输入确认密码是"125636"
	    And 安小糖选择的研究领域是"信息科学","计算机科学与技术"
		And 安小糖输入主页域名的最后部分"xiaotang"
		When 安小糖点击"创建"按钮时
		Then 页面跳转到"people/xiaotang"
		And 进入设置步骤"Step-1"
		
    Scenario Outline: 个人研究主页创建_2_用户填写无效数据注册和创建主页
		Given 王强输入的真实姓名是<name>
	    And  王强输入英文名是<ename>
        And  王强的输入邮箱地址是<email>
        And  王强的输入注册密码是<password>
	    And  王强的输入确认密码是<verify_password>
		And  王强输入主页域名的最后部分<domain>
		When 王强点击"创建"按钮时
		Then 王强看到提示<error>
	Examples: 
	|name|ename     |email        |password|verify_password |domain   |error |
	|王强    |wang qiang|qiang@cnic.cn|123456  |125636          |wangqiang|两次密码输入不一致 |
	|王强    |wang qiang|qiang        |123456  |123456          |wangqiang|邮箱无效                     |
	|王强    |wang qiang|qiang@cnic.cn|123456  |123456          |王强                   |域名无效                     |
		
    @java_enable	 
	Scenario: 个人研究主页创建_3_个性化域名已被注册
	  Given 系统已经存在域名"liming"
	  And   李雷输入主页域名的最后部分"liming"
	  When  李雷离开域名输入框时
	  Then  李雷看到提示"该域名已被注册，请更换其他域名"
	@java_enable	
    Scenario: 个人研究主页创建_4_注册邮箱地址已存在
      Given 小红的输入邮箱地址是"liming@163.com"
      But  小红的输入邮箱地址已经被注册
      When 小红点击"开始创建主页"按钮
      Then 小红看到提示信息"邮件地址已存在，请用其它邮件地址注册；如果您要登录，请进入登陆页面"
      
