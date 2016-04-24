/*
VERSION:	dHome 2.0 
AUTHOR:		Vera. zhangshixiang@cnic.cn / shivera2004@163.com
DATE:		March, 2015
*/


/* CommonBanner*/
$(document).ready(function(){
	var getRightHeight = setInterval(function(){
		var rightHeight = $(".ins_backend_rightContent").outerHeight();
		$(".ins_backend_leftMenu").css("height",rightHeight);
		/*if($(".ins_backend_leftMenu").outerHeight() == $(".ins_backend_rightContent").outerHeight()){
			clearInterval(getRightHeight);
		}*/
	},20);
});

