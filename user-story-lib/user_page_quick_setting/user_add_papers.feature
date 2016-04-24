Feature: 个人主页快速设置导航-论文设置
@java_enable
    在这步中，用户要设置自己发表过的论文。系统可以为他进行推荐，他也可以自己进行搜索。对推荐的论文要进一步确认是否是自己发表过的。
	Scenario: 根据人名加载的论文候选集
  		Given 全部论文集合
  		    |paper|
      		| Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012    |
      		| Graph Model of Real-World Networks with M. Kim. Internet Mathematics, 2012    |
      		| Human Wayfinding in Information Networks, Ming Li, R. West. WWW 2012          |
		When 张晓跳到设置向导"Step-2"
  		Then 张晓看到论文列表
			| paper|                                                                                                
      		| Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012    |
      		| Human Wayfinding in Information Networks, Ming Li, R. West. WWW 2012          |

	Scenario: 确认是李晓的论文
    	Given 程序推荐的论文列表
    	    |paper|
    	    |Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012 |
    	    |Graph Model of Real-World Networks with M. Kim. Internet Mathematics, 2012 |
      	    |Human Wayfinding in Information Networks, Ming Li, R. West. WWW 2012|
    	  
      	And 李晓确认的论文列表"我的论文列表"
    	When 李晓在程序推荐的论文列表中确认论文"Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012"是发表过的论文
    	Then 李晓看到"Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012"出现在"我的论文列表"
    
	Scenario:从"我的论文列表"里删除刚确认的论文
    	Given "我的论文列表"
    	    |paper|
    	    |Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012 |
    	    |Graph Model of Real-World Networks with M. Kim. Internet Mathematics, 2012 |
    	And 程序推荐的论文列表
    	    |paper|
      	    |Human Wayfinding in Information Networks, Ming Li, R. West. WWW 2012|
  
    	When 王晓在"我的论文列表"中删除论文"Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012"
    	Then "我的论文列表"变成
    	    |paper|
    	    |Graph Model of Real-World Networks with M. Kim. Internet Mathematics, 2012 |
    	And 程序推荐的论文列表变成
    	    |paper|
    	    |Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012 |
      	    |Human Wayfinding in Information Networks, Ming Li, R. West. WWW 2012|
    	
    Scenario:论文设置成功
        Given "我的论文列表"
    	    |paper|
    	    |Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012 |
    	    |Graph Model of Real-World Networks with M. Kim. Internet Mathematics, 2012 |
    	When 赵晓点击了"保存&下一步"按钮
    	Then 页面跳转到了设置向导"Step-3"
    	
    Scenario: 检索论文
  		Given 钱晓输入的检索词是"Li ming"	   
		When 钱晓点击"检索"按钮
  		Then 钱晓看到论文列表
			|paper|
      		| Predicting Group Growth and Longevity, Li Ming. Kairam, D. Wang. WSDM 2012    |
      		| Graph Model of Real-World Networks with M. Kim. Internet Mathematics, 2012    |
      		| Human Wayfinding in Information Networks, Ming Li, R. West. WWW 2012          |
     