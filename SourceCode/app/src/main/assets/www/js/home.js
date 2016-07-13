var HOME={};
HOME.onLoad=function()
{
}
HOME.btnHistory_click = function(e)
{
	if(e) e.preventDefault();
	COMMON.NavigatePage(COMMON.SCREENS.History);
	return false;
};
HOME.btnEmergency_click = function(e)
{
	if(e) e.preventDefault();
	COMMON.NavigatePage(COMMON.SCREENS.Emergency);
	return false;
};
var flag = false;
HOME.btnNewIncident_click = function(e)
{
	 e.stopPropagation();
     e.preventDefault();  
     $(this).off('click');
         
     //Create Incident ID
     sessionStorage.activeIndex =null;
     sessionStorage.isBackPressed = "false";
     sessionStorage.IncidentType = "NEW";
     
     COMMON.NavigatePage(COMMON.SCREENS.NewIncident);
     
     return false;
};
//House crash Report
HOME.btnCrashHouse_click = function(e)
{
	 e.stopPropagation();
     e.preventDefault();  
     $(this).off('click');
     
     //Create Incident ID
     sessionStorage.activeIndex =null;
     sessionStorage.isBackPressed = "false";
     sessionStorage.IncidentType = "NEW";
     
     sessionStorage.DefaultCategoryType = "Property";
     sessionStorage.DefaultIncidentType = "Accident";
     
     COMMON.NavigatePage(COMMON.SCREENS.NewIncident_Property);
     
     return false;
};
HOME.btnCrashCar_click = function(e)
{
	 e.stopPropagation();
     e.preventDefault();  
     $(this).off('click');
     
     //Create Incident ID
     sessionStorage.activeIndex =null;
     sessionStorage.isBackPressed = "false";
     sessionStorage.IncidentType = "NEW";
     
     sessionStorage.DefaultCategoryType = "Vehicle";
     sessionStorage.DefaultIncidentType = "Accident";
     
     COMMON.NavigatePage(COMMON.SCREENS.NewIncident_Car);
     
     return false;
};
HOME.btnProfile_click = function(e)
{
	if(e) e.preventDefault();
	
	//Read security pin from Local Storage
	sessionStorage.PIN = Wrapper.getSecurityPin();
	//alert(sessionStorage.PIN);
	
	//Validate if application is opened first time
	if((typeof sessionStorage.PIN) == 'undefined' || sessionStorage.PIN =="")
	{
		//If first time User has to create a new pin
		//Show pinbox in create Mode
		$("#pinBoxTitle").text("Create Secure PIN ID");
		$(".pinOverlay").show();
		COMMON.isPinOpened = "true";
	}
	else
	{
		//Else it shows Pin box to enter existing pin number
		//Show pin box in entry mode
		$("#pinBoxTitle").text("Secure PIN ID");
		$(".pinOverlay").show();
		COMMON.isPinOpened = "true";
	}
	return false;
};
HOME.btnTrackLocation_click = function(e)
{
	if(e) e.preventDefault();
	COMMON.NavigatePage(COMMON.SCREENS.Tracker);
	return false;
};
HOME.closePin = function()
{
	$("#txtPin").val("");
	$(".pinOverlay").hide();
	COMMON.isPinOpened = "false";
};
$(document).ready(function()
{
	sessionStorage.isFromHistory = "FALSE";
	//Hide Pin overlay
	$(".pinOverlay").hide();
	COMMON.isPinOpened = "false";
	
	sessionStorage.isEmailed = "false"
	sessionStorage.DefaultCategoryType ="Select";
	sessionStorage.DefaultIncidentType = "Select";
	
	COMMON.CurrentPage = COMMON.SCREENS.Home;
	
	$(".btn_tile_history").bind("click", HOME.btnHistory_click);  //Goto History Page
	$(".btn_tile_emergency").bind("click", HOME.btnEmergency_click);  //Goto Emergency Page
	$(".btn_tile_newincident").bind("click", HOME.btnNewIncident_click);  //Goto New incident Page
	$(".btn_tile_profile").bind("click", HOME.btnProfile_click);  //Goto My Profile Page
	
	$(".btn_tile_crashhouse").bind("click", HOME.btnCrashHouse_click);  //Goto My Profile Page
	$(".btn_tile_crashcar").bind("click", HOME.btnCrashCar_click);  //Goto My Profile Page
	$(".btn_tile_tracklocation").bind("click", HOME.btnTrackLocation_click);  //Goto My Profile Page

	//PIN box related events
	$("#btnClear").bind("click", HOME.btnClear_click);  //clear button
	//$("#btnClear").bind("touchend", HOME.btnClear2_click);  //clear button
	$(".digits").bind("touchstart", HOME.btnNumber_click); 
	$(".digits").bind("touchend", HOME.btnNumber2_click); 
	$("#btnValidatePin").bind("click", HOME.btnValidatePin_click); 
	//$("#btnValidatePin").bind("touchend", HOME.btnValidatePin2_click); 
	
	Wrapper.doPreRequites();
});

//******* PIN box related events ****************//
HOME.btnClear_click = function(e)
{
	if(e) e.preventDefault();
	$("#txtPin").val("");
	return false;
};
HOME.btnClear2_click = function(e)
{
	if(e) e.preventDefault();
	if(e) e.stopPropagation();
	return false;
};
HOME.btnNumber2_click = function(e)
{
	if(e) e.preventDefault();
	if(e) e.stopPropagation();
	return false;
};
HOME.btnNumber_click = function(e)
{
	if(e) e.preventDefault();
	var existVal = "" + $("#txtPin").val();
	if(existVal.length>=5)
	{
		return;
	}
	existVal += ("" + $(this).val());
	$("#txtPin").val(existVal);
	return false;
}
HOME.btnValidatePin2_click = function(e)
{
	if(e) e.preventDefault();
	if(e) e.stopPropagation();
	return true;
};
HOME.btnValidatePin_click = function(e)
{
	if(e) e.preventDefault();
	if($("#txtPin").val().length<1)
	{
		alert("Please enter PIN.");
		return;
	}
	if($("#txtPin").val().length<5)
	{
		alert("Please enter 5 digits.");
		return;
	}
	if(typeof sessionStorage.PIN == 'undefined' || sessionStorage.PIN =="")
	{
		
		if($("#txtPin").val().length<3)
		{
			alert("Please enter 5 digits.");
		}
		else
		{
			sessionStorage.PIN = $("#txtPin").val();
			var retVal = Wrapper.updateSecurityPin(sessionStorage.PIN);
			$(".pinOverlay").hide();
			COMMON.NavigatePage(COMMON.SCREENS.MyProfile);
		}
	}
	else if(sessionStorage.PIN != ""+$("#txtPin").val())
	{
		alert("Invalid PIN, Please try again.");
		$(".pinOverlay").hide();
		$("#txtPin").val("");
	}
	else if(sessionStorage.PIN == ""+$("#txtPin").val())
	{
		sessionStorage.PIN = $("#txtPin").val();
		$(".pinOverlay").hide();
		COMMON.NavigatePage(COMMON.SCREENS.MyProfile);
	}
	return false;
}


