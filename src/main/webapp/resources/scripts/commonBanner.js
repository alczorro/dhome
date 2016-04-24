$(document).ready(function() {
	var urlStr = document.getElementById("bannerJs").getAttribute("data");
	var url = urlStr.substring(urlStr.indexOf('=')+1, urlStr.length);
	$("input[name=bannerSearch]").keyup(function(event) {
		if (event.which == 13) {
			var keyword = $(this).val();
			window.location.href = url+"?keyword=" + keyword;
		}
	});
})
