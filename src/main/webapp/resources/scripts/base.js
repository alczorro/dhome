/*
VERSION:	dHome 1.0
AUTHOR:		Vera. zhangshixiang@cnic.cn / shivera2004@163.com
DATE:		August, 2012
*/


/* Left Menu*/
$(document).ready(function(){
	$("ul.nav.nav-list li").hover(function(){
		$(this).find(".left-pull-down").css({"border-top-color":"#000"});
	});
	$("ul.nav.nav-list li").mouseleave(function(){
		$(this).find(".left-pull-down").css({"border-top-color":"transparent"});
	});
	$("ul.nav.nav-list li span").click(function(event){
		var itemId = $(this).attr("itemId");
		$(".left-dropdown-menu").show();
		$(".left-dropdown-menu").attr("parentId",itemId); //子菜单操作的父元素的menu_item_id
		var top = $(this).offset().top;
		var left = $(this).offset().left;
		$(".left-dropdown-menu").css({"top":top,"left":left});
	});
	$(".left-dropdown-menu li").live("click",function(){
		$(".left-dropdown-menu").hide();
	});
	var backBox = new BackToTop('回顶部');
});
var showMesg = function(message){
	$('#information').html(message).show();
	setTimeout(function(){
		$('#information').hide();
	},1000);
};

/* BACK TO TOP */
function BackToTop(TEXT) {
	//append items
	this.fulH = $(document).height();
	this.winH = $(window).height();
	$('.dHome-body').append('<div id="backToTop" title="'+TEXT+'"></div>');
	this.trigger = $('#backToTop');
	this.trigger.css('left', $('.container').offset().left + parseInt(this.trigger.css('left')));
	var entity = this;
	this.trigger.click(function(){
		entity.toTop();
	});
	$(window).scroll(function(){
		entity.appearance($(this).scrollTop());
	});
}
	BackToTop.prototype.appearance = function(SCRTOP) {
		if (SCRTOP > 0.2*this.winH) {
			this.trigger.fadeIn();
		}
		else {
			this.trigger.fadeOut();
		}
	};
	
	BackToTop.prototype.toTop = function() {
		$(window).scrollTop(0);
	};