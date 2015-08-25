$(function(){
	$(".yiji:eq(5)").addClass("current");
	$(".subnav:eq(5)").addClass("subnavvis");
	$(".subnav:eq(5) li:eq a").removeClass("current");
	if(target=="0") {
		$(".subnav:eq(5) li:eq(0) a").addClass("current");
	} else if(target=="1") {
		location.hash="1";
		$(".subnav:eq(5) li:eq(1) a").addClass("current");
	} else if(target=="2") {
		location.hash="2";
		$(".subnav:eq(5) li:eq(2) a").addClass("current");
	} else if(target=="3") {
		location.hash="3";
		$(".subnav:eq(5) li:eq(3) a").addClass("current");
	}  else if(target=="4") {
		location.hash="4";
		$(".subnav:eq(5) li:eq(4) a").addClass("current");
	}  else if(target=="5") {
		location.hash="5";
		$(".subnav:eq(5) li:eq(5) a").addClass("current");
	}  else if(target=="6") {
		location.hash="6";
		$(".subnav:eq(5) li:eq(6) a").addClass("current");
	}
});