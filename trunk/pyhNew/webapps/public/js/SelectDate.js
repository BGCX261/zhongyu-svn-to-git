function BindDateTime(Yearid,Monthid,Dayid)
{
    var minYear = new Date().getFullYear() - 80; //最小年
    var maxYear = new Date().getFullYear() - 1; //最大年
    for(minYear;minYear<=maxYear;minYear++)
    {
        $("#"+Yearid).addOption(minYear,minYear);
    }
    for(var m=1;m<=12;m++)
    {
        $("#"+Monthid).addOption(m,m);
    };
    $("#"+Yearid).bind("change",function(){
        GetDays(Yearid,Dayid,$("#"+Monthid).val());
    });
    $("#"+Monthid).bind("change",function(){
        GetDays(Yearid,Dayid,$(this).val());
    });
    GetDays(Yearid,Dayid,1);//默认值
}   

function GetDays(Yearid,Dayid,Month)
{
    var obj = $("#"+Dayid);
    obj.clearAll();
    var MonthDay = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    for(var d=1;d<=MonthDay[Month-1];d++)
    {
        obj.addOption(d,d);
    }
    if(!IsPinYear($("#"+Yearid).val()) && Month == 2) obj.addOption(29,29);
}

function IsPinYear(year)
{ 
    return (0 == year%4 && (year%100 !=0 || year%400 == 0));
}

function BindDateValue(Ids,Values)
{
    for(var i=0;i<Ids.length;i++)
    {
        $("#"+Ids[i]).val(Values[i]);
    }
}