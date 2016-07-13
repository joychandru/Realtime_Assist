var EMERGENCY={};
EMERGENCY.onLoad=function()
{
}
EMERGENCY.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	window.location = "home.html";
};
EMERGENCY.btnNewContact_click = function(e)
{
	if(e) e.preventDefault();
	EMERGENCY.ShowInputBox("Select", "","");
};
EMERGENCY.btnCloseNewCont_click = function(e)
{
	if(e) e.preventDefault();
	EMERGENCY.HieInputBox();
};
EMERGENCY.btnSaveNewCont_click = function(e)
{
	if(e) e.preventDefault();
	
	var type= $("#per_selectContactType").val();
	var name = $("#txtContactName").val();
	var contNo = $("#txtContactNumber").val();
	//var contEmail = $("#txtContactEmail").val();
	var text = $("#per_selectContactType option:selected").text();
	
	$(".inputType").removeClass("inputType_highlight");
	$(".inputType").removeClass("inputType_highlight");
	//validation
	var count = 0;
	if(type == "Select")
	{
		$("#per_selectContactType").addClass("inputType_highlight");
		count++;
	}
	if(name.length<1)
	{
		$("#txtContactName").addClass("inputType_highlight");
		count++;
	}
	if(contNo.length<1)
	{
		$("#txtContactNumber").addClass("inputType_highlight");
		count++;
	}
	/*if(contEmail.length<1)
	{
		$("#txtContactEmail").addClass("inputType_highlight");
		count++;
	}*/
	if(count>0) return;
	
	//var response = Wrapper.updateEmergencyContact(type, name, contNo,"false",false, text,contEmail);
	
	var response = Wrapper.updateEmergencyContact(type, name, contNo,"false",false, text,"");

	EMERGENCY.loadList();
	
	EMERGENCY.HieInputBox();
};
EMERGENCY.ShowInputBox = function(type, name, contNo)
{
	$("#per_selectContactType").prop('disabled', '');
	$("#per_selectContactType").val(type);
	$("#txtContactName").val(name);
	$("#txtContactNumber").val(contNo);
	$(".addNewBoxOverlay").show();
};
EMERGENCY.HieInputBox = function()
{
	$("#per_selectContactType").prop('disabled', '');
	$("#per_selectContactType").val("Select");
	$("#txtContactName").val("");
	$("#txtContactNumber").val("");
	$(".addNewBoxOverlay").hide();
};
EMERGENCY.loadList = function()
{
	var emergencyList = Wrapper.fetchEmergencyList();
	
	var data = JSON.parse(emergencyList);
	
	var keys = Object.keys(data);
	keys.sort();
	var index=0;
	var length = keys.length;
	
	if(length>0) $(".scroll_wrapper").empty();
	
	for(index=0;index<length;index++)
	{
		var key = keys[index];
		var listItem = data[key];
		console.log(key);
		
		var fav_str = "right_content_fav";
		if(listItem.IsFavourite=="true") fav_str = "right_content_fav_active";

		//Load dummy items
		var template= '<div class="list_item">' +
						'<div class="left_content">' +
							'<a href="#" onclick="EMERGENCY.emergencyListItem_click(\''+ key +'\');"><div class="list_item_title_main">' +
								'Type:&nbsp;' + listItem.ID+
							'</div>' +
							'<div class="list_item_title_sub">' +
								'Contact:&nbsp;' + listItem.Number +
							'</div></a>' +
							/*'<div class="list_item_title_sub colorEmail">' +
								'Email:&nbsp;' + listItem.Email +
							'</div>'*/
						'</div>' +
						'<a href="tel:' + listItem.Number + '"><div class="right_content right_content_call"></div></a>' +
						'<div class="fav_icon right_content ' + fav_str + '" itemKey="'+ 
						key + '" isActive="'+ listItem.IsFavourite + '" Number="'+ listItem.Number + 
						'" ContName="'+ listItem.Name + '" ID="'+ listItem.ID + '" Email="'+ listItem.Email + '"></div>' +
					'</div>';		
		$(".scroll_wrapper").append(template);
	}
	
	return;
	//Load dummy items
	/*var template= '<div class="list_item">'+
					'<div class="left_content">'+
						'<div class="list_item_title_main">'+
							'Record ID: 1-9898123'+
						'</div>'+
						'<div class="list_item_title_sub">'+
							'Created on 09 Mar,2015 10:00 AM'+
						'</div>'+
					'</div>'+
					'<div class="right_content">'+
						'<span class="sync_tick"></span>'+
					'</div>'+
				'</div>';
	$(".scroll_wrapper").empty();
	for(var i=0;i<=20;i++)
	{
		$(".scroll_wrapper").append(template);
	}*/
};
EMERGENCY.favourite_click = function(e)
{	
	if(e) e.preventDefault();
	
	var IsFavourite = $(this).attr("isActive");
	var Name = $(this).attr("ContName");
	var Number = $(this).attr("Number");
	var type = $(this).attr("itemKey")
	var ID = $(this).attr("ID")
	
	console.log(IsFavourite);
	
	if(IsFavourite=="true")
	{
		$(this).attr("isActive","false");
		$(this).removeClass("right_content_fav_active");
		$(this).addClass("right_content_fav");
	}
	else
	{
		$(this).attr("isActive","true");
		$(this).addClass("right_content_fav_active");
		$(this).removeClass("right_content_fav");
	}
	IsFavourite = $(this).attr("isActive");
	var response = Wrapper.updateEmergencyContact(type,Name, Number,IsFavourite, false, ID);
};
EMERGENCY.emergencyListItem_click = function(key)
{
	console.log("selected Key:" + key);
	var emergencyItem = Wrapper.fetchEmergencyListItem(key);
	emergencyItem = JSON.parse(emergencyItem);
	$("#per_selectContactType").prop('disabled', 'disabled');
	EMERGENCY.ShowInputBox(key, emergencyItem.Name, emergencyItem.Number);
};

EMERGENCY.btnPickContact_click = function(key)
{
	if(Wrapper!='undefined')
		{
			Wrapper.pickContacts("","EMERGENCY.btnPickContact_success","EMERGENCY.btnPickContact_failure");
		}
};
EMERGENCY.btnPickContact_success = function(args)
{
	alert(args);
};
EMERGENCY.btnPickContact_failure = function(args)
{
	alert(args);
};

$(document).ready(function()
{
	COMMON.CurrentPage = COMMON.SCREENS.Emergency;
	
	$(".header_left").on("touchstart", EMERGENCY.btnHome_click);
	$(".subheader_right").on("touchstart", EMERGENCY.btnNewContact_click);
	//$("#idPickContact").on("click", EMERGENCY.btnPickContact_click);
	
	$(".addNewBoxOverlay").hide();
	
	$(".scroll_wrapper").delegate(".fav_icon", "click", EMERGENCY.favourite_click);
	
	//TEST
	//Add new item String type, String name, String contactNumber, String isFavourite
	//var response = Wrapper.updateEmergencyContact("Office","Office Name", "+91987654362900","true",false);
	
	EMERGENCY.loadList();
});




