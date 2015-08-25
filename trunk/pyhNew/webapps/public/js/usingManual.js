$(function(){
	$(".yiji:eq(0)").addClass("current");
	$(".subnav:eq(0)").addClass("subnavvis");
	$(".subnav:eq(0) li:eq a").removeClass("current");
	if(target=="0") {
		$(".subnav:eq(0) li:eq(0) a").addClass("current");
	} else if(target=="1") {
		location.hash="1";
		$(".subnav:eq(0) li:eq(1) a").addClass("current");
	} else if(target=="2") {
		location.hash="2";
		$(".subnav:eq(0) li:eq(2) a").addClass("current");
	} else if(target=="3") {
		location.hash="3";
		$(".subnav:eq(0) li:eq(3) a").addClass("current");
	}  else if(target=="4") {
		location.hash="4";
		$(".subnav:eq(0) li:eq(4) a").addClass("current");
	}
});