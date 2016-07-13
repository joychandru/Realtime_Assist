var COMMON={};
COMMON.isPinOpened = "false";
COMMON.SCREENS = {
				"Home":"home.html", 
				"NewIncident":"newincident.html",
				"NewIncident_Car":"newincident_car.html",
				"NewIncident_Property":"newincident_property.html",
				"History":"history.html",
				"MyProfile":"myprofile.html",
				"Emergency":"emergency.html",
				"Gallery":"gallery.html",
				"ShowMap":"showmap.html",
				"Tracker":"vehtracker.html"};

COMMON.CurrentPage = COMMON.SCREENS.Home;
COMMON.parentPage = COMMON.SCREENS.Home;

COMMON.NavigatePage=function(screenRef)
{
	sessionStorage.isBackPressed = false;
	window.location = screenRef;
}
COMMON.success_callback = function(response)
{
	alert(response);
};
COMMON.backPressed = function()
{	
	sessionStorage.isBackPressed = true;
	sessionStorage.isEmailed = "false";
	if(COMMON.isPinOpened=="true")
	{
		HOME.closePin();
		COMMON.isPinOpened = "false";
		return;
	}
	switch(COMMON.CurrentPage)
	{
		case COMMON.SCREENS.Home:
			//Ask confirmation and quit the application
			Wrapper.ExitApplication();
			break;
		case COMMON.SCREENS.Tracker:
			window.location = COMMON.SCREENS.Home;
			break;
		case COMMON.SCREENS.History:
			window.location = COMMON.SCREENS.Home;
			break;
		case COMMON.SCREENS.Emergency:
			window.location = COMMON.SCREENS.Home;
			break;
		case COMMON.SCREENS.MyProfile:
			PROFILE.saveProfileData();
			window.location = COMMON.SCREENS.Home;
			break;
		case COMMON.SCREENS.NewIncident:
			if(sessionStorage.IncidentType=="EDIT")
			{
				window.location = COMMON.SCREENS.History;
			}
			else
			{
				window.location = COMMON.SCREENS.Home;
			}			
			break;
		case COMMON.SCREENS.NewIncident_Property :
			if(sessionStorage.isFromHistory=="TRUE") //sessionStorage.IncidentType=="EDIT")
			{
				window.location = COMMON.SCREENS.History;
			}
			else
			{
				window.location = COMMON.SCREENS.Home;
			}			
			break;
		case COMMON.SCREENS.NewIncident_Car:
			if(sessionStorage.isFromHistory=="TRUE") //sessionStorage.IncidentType=="EDIT")
			{
				window.location = COMMON.SCREENS.History;
			}
			else
			{
				window.location = COMMON.SCREENS.Home;
			}			
			break;
		case COMMON.SCREENS.ShowMap:
			window.location = COMMON.parentPage;
			break;
		case COMMON.SCREENS.Gallery:
			window.location = COMMON.parentPage;
			break;
		default:
			window.location = COMMON.SCREENS.Home;
			break;
	}	
	//alert("back pressed");
};
COMMON.showMessage = function(msg)
{
	alert(msg);
};
