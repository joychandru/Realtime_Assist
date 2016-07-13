var NEWINCIDENT={};
NEWINCIDENT.activeBox = 1;
NEWINCIDENT.isSwipeRequied = false;

//**********VEHICLE******************/
//Vehicle Assist - DD - Incident Type
NEWINCIDENT.ddlIncidentType_vehicle = [{"Key":"Accident","Value":"Accident"},
                                   {"Key":"Theft","Value":"Theft"},
                                   {"Key":"STORM","Value":"STORM"}, //New field 6th-Apr-2015
                                   {"Key":"Other","Value":"Other"}];
//Vehicle Assist - DD - Category Type
NEWINCIDENT.ddlCatType_vehicle = [{"Key":"Vehicle","Value":"Vehicle"},
                                  {"Key":"Property","Value":"Property"},
                                  {"Key":"Other","Value":"Other"}];

/*NEWINCIDENT.ddlLocation_vehicle = [{"Key":"OnRoad","Value":"On Road"},
                                  {"Key":"ParkShoppingCentre","Value":"Parking Area or Shopping Centre road"},
                                  {"Key":"Driveway","Value":"Driveway or adjacent to roadway"},
                                  {"Key":"OffRoad","Value":"Off-road"}];*/

NEWINCIDENT.ddlLocation_vehicle = [{"Key":"OnRoad","Value":"On Road"},
                                   {"Key":"ParkShoppingCentre","Value":"Parking Area or Shopping Centre road"},
                                   {"Key":"DrivewayOnProp","Value":"Driveway On Property"},
                                   {"Key":"Garage","Value":"Garage"},
                                   {"Key":"StPARK","Value":"Street Parking"},
                                   {"Key":"underPark","Value":"Under Cover Parking"},
                                   {"Key":"parkbay","Value":"Parking Bay"}];


NEWINCIDENT.ddlInjuryType_vehicle = [{"Key":"Body","Value":"Body"},
                                     {"Key":"Car","Value":"Car"},
                                     {"Key":"Property","Value":"Property"},
                                     {"Key":"Other","Value":"Other"}];
									/*[{"Key":"Neck","Value":"Neck"},
                                   {"Key":"Leg","Value":"Leg"},
                                   {"Key":"Shoulder","Value":"Shoulder"},
                                   {"Key":"Other","Value":"Other"}];*/

NEWINCIDENT.ddlOtherPhoto_vehicle = [{"Key":"IDCard","Value":"ID Card"},
                                     {"Key":"DrivingLicence","Value":"Driving Licence"},
                                     {"Key":"OtherParty","Value":"Other Parties"},
                                     {"Key":"Vehicle","Value":"Vehicle"},
                                     {"Key":"NumberPlate","Value":"NumberPlate"}];

NEWINCIDENT.ddlCrashType_vehicle = [{"Key":"HOC","Value":"Head on Collision"},
                                     {"Key":"CPV","Value":"Collision with Parked Vehicle"},
                                     {"Key":"REC","Value":"Rear End Collision"},
                                     {"Key":"CWR","Value":"Collision while reversing"},
                                     {"Key":"SDS","Value":"Side Impact - Drivers Side"},
                                     {"Key":"SPS","Value":"Side Impact - Passengers Side"},
                                     {"Key":"OVR","Value":"Overturned"},
                                     {"Key":"SP","Value":"Struck Pedestrian"},
                                     {"Key":"SA","Value":"Struck Animal"},
                                     {"Key":"SO","Value":"Struck Object"},
                                     {"Key":"Theft","Value":"Theft"}];

//***********PROPERTY*******************/
//Property Assist - DD - Incident Type
NEWINCIDENT.ddlIncidentType_property = [{"Key":"Burglary","Value":"Burglary"},
                                   {"Key":"Flooding","Value":"Flooding"},
                                   {"Key":"Fire","Value":"Fire"},
                                   {"Key":"Earthquake","Value":"Earthquake"},
                                   {"Key":"Hail","Value":"Hail"},
                                   {"Key":"HighWind","Value":"High Wind"},
                                   {"Key":"Other","Value":"Other"}];
//Property Assist - DD - Category Type
NEWINCIDENT.ddlCatType_property = [{"Key":"Vehicle","Value":"Vehicle"},
                                  {"Key":"Property","Value":"Property"},
                                  {"Key":"Other","Value":"Other"}];

NEWINCIDENT.ddlLocation_property = [{"Key":"Property","Value":"Property"},
                                   {"Key":"Other","Value":"Other"}];

NEWINCIDENT.ddlInjuryType_property = [{"Key":"Neck","Value":"Body Injury"},
                                 {"Key":"CarCurFurnHHold","Value":"Carpets,Curtains,Furniture,Household items"},
                                 {"Key":"ClothingPersonalEffect","Value":"Clothing and Personal Effects"},
                                 {"Key":"SwimmingPool","Value":"Swimming pools"},
                                 {"Key":"GardenEquipment","Value":"Garden and Equipment"},
                                 {"Key":"Watercrafts","Value":"Watercrafts"},
                                 {"Key":"Bicycle","Value":"Bicycle"},
                                 {"Key":"ElectricalEquipment","Value":"Electrical Equipment"},
                                 {"Key":"MobileTele","Value":"Mobile telephones"},
                                 {"Key":"Computers","Value":"Computers"},
                                 {"Key":"CarParking","Value":"Car Parking"},
                                 {"Key":"ToolsInstruments","Value":"Tools and Instruments"},
                                 {"Key":"Documents","Value":"Documents"},
                                 {"Key":"Jewellery","Value":"Jewellery"},
                                 {"Key":"Others","Value":"Others"}];

NEWINCIDENT.ddlOtherPhoto_property = [{"Key":"Property","Value":"Property"},
                                     {"Key":"Other","Value":"Other"}];

NEWINCIDENT.ddlCrashType_property = [{"Key":"Roof","Value":"Roof"},
                                     {"Key":"Window","Value":"Window"},
                                     {"Key":"Door","Value":"Door"},
                                    {"Key":"Other","Value":"Other"}];

NEWINCIDENT.onLoad=function()
{
}
NEWINCIDENT.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	COMMON.NavigatePage(COMMON.SCREENS.Home);
	return false;
};
NEWINCIDENT.btnNavLeft_click = function(e)
{
	if(e) e.preventDefault();
	if(NEWINCIDENT.activeBox==1)
	{
		//Go to back
		COMMON.backPressed();
		return;
	}
	
	if(NEWINCIDENT.activeBox <1)
	{
		NEWINCIDENT.activeBox==1;
		//$(".nav_left").hide();
		$(".nav_right").show();
	}
	else
	{
		NEWINCIDENT.activeBox--;
		$(".nav_right").show();
		
		if(NEWINCIDENT.activeBox==7 && sessionStorage.DefaultCategoryType.toUpperCase() == "PROPERTY")
		{
			NEWINCIDENT.activeBox--;
			NEWINCIDENT.activeBox--;
		}
	}
	if(NEWINCIDENT.activeBox==1)
	{
		$(".nav_right").show();
		//$(".nav_left").hide();
	}
	var divId = "#box" + NEWINCIDENT.activeBox;
	
	sessionStorage.activeIndex = NEWINCIDENT.activeBox;
	
	$('#scroll_container').scrollTo($(divId), 100);

	NEWINCIDENT.saveExistingIncident();
	
	return true;
}
NEWINCIDENT.btnNavRight_click = function(e)
{
	if(e) e.preventDefault();

	if(NEWINCIDENT.activeBox==9)
	{
		NEWINCIDENT.activeBox==1;
		$(".nav_left").hide();
		$(".nav_right").show();
	}
	else
	{
		NEWINCIDENT.activeBox++;
		$(".nav_left").show();
		if(NEWINCIDENT.activeBox==6 && sessionStorage.DefaultCategoryType.toUpperCase() == "PROPERTY")
		{
			NEWINCIDENT.activeBox++;
			NEWINCIDENT.activeBox++;
		}
	}
	//alert(NEWINCIDENT.activeBox);
	if(NEWINCIDENT.activeBox==9)
	{
		$(".nav_left").show();
		$(".nav_right").hide();
	}
	var divId = "#box" + NEWINCIDENT.activeBox;

	sessionStorage.activeIndex = NEWINCIDENT.activeBox;
	
	$('#scroll_container').scrollTo($(divId), 100);

	NEWINCIDENT.saveExistingIncident();
	
	return true;
}
NEWINCIDENT.setSessionValues = function(Category, Prefix)
{
	if(sessionStorage!=null)
	{
		sessionStorage.parentPhotoPage = COMMON.SCREENS.NewIncident; //Parent Page
		sessionStorage.Category = Category;
		sessionStorage.prefix = Prefix;
	}
};

NEWINCIDENT.viewInjuryPhoto_click = function(e)
{	
	if(e) e.preventDefault();
	sessionStorage.IncidentType = "EDIT";
	var category = "Injury"; //Read category
	var prefix = ""; //Read prefix
	NEWINCIDENT.setSessionValues(category, prefix);
	COMMON.NavigatePage(COMMON.SCREENS.Gallery);
	return false;
};
NEWINCIDENT.viewOtherPhoto_click = function(e)
{
	if(e) e.preventDefault();
	sessionStorage.IncidentType = "EDIT";
	var category = "OtherPhoto"; //Read category
	var prefix = ""; //Read prefix
	NEWINCIDENT.setSessionValues(category, prefix);
	COMMON.NavigatePage(COMMON.SCREENS.Gallery);
	return false;
};
NEWINCIDENT.takeInjuryPhoto_click = function(e)
{
	if(e) e.preventDefault();
	
	if($("#ddInjuryType").val().toUpperCase()=="SELECT")
	{
		COMMON.showMessage("Please Select Injury Type");
		return;
	}

	var category = "Injury"; //Read category
	var prefix = $("#ddInjuryType").val(); //Read prefix
	NEWINCIDENT.setSessionValues(category, prefix);
	
	//Call Wrapper for photo 
	var responseWrapper = "";
	try
	{
		responseWrapper = Wrapper.takePhotos(sessionStorage.IncidentID, sessionStorage.Category, sessionStorage.prefix, 
				"NEWINCIDENT.success_callaback", "NEWINCIDENT.failure_callaback");
	}
	catch(e)
	{	
		alert("Error Invoking Native code!");
	}
	console.log("INSASSIST", "Response From Wrapper:" + responseWrapper);
	return false;
};
NEWINCIDENT.takeOtherPhoto_click = function(e)
{
	if($("#ddOtherPhotoCat").val().toUpperCase()=="SELECT")
	{
		COMMON.showMessage("Please Select Other Photo category");
		return;
	}
	
	if(e) e.preventDefault();
	var category = "OtherPhoto"; //Read category
	var prefix = $("#ddOtherPhotoCat").val(); //Read prefix
	NEWINCIDENT.setSessionValues(category, prefix);
	
	//Call wrapper for photo 
	var responseWrapper = "";
	try
	{
		responseWrapper = Wrapper.takePhotos(sessionStorage.IncidentID, sessionStorage.Category, sessionStorage.prefix, 
				"NEWINCIDENT.success_callaback", "NEWINCIDENT.failure_callaback");
	}
	catch(e)
	{	
		alert("Error Invoking Native code!");
	}
	console.log("INSASSIST", "Response From Wrapper:" + responseWrapper);
	return false;
};
NEWINCIDENT.success_callaback = function(response)
{
	//alert("Response From Camera: " + response);
	if(sessionStorage.Category =="Injury")
	{
		$("#lblNoOfInjuryPhotos").text(response);
	}
	else if(sessionStorage.Category =="OtherPhoto")
	{
		$("#lblNoOfOtherPhotos").text(response);
	}
};
NEWINCIDENT.failure_callaback = function(e)
{
	alert("failure");
};
NEWINCIDENT.createNewIncident = function(defaultCategory)
{
	//Create Unique ID for incident
	var uniqueID = Wrapper.generateUniqueID(); //"2015032002"; //Shall be dynamic
	sessionStorage.IncidentID = "ID_" + uniqueID; 
	sessionStorage.CreatedDate = Wrapper.currentDate(); //"16 Mar,2015 10:12PM";  //Shall be dynamic
	sessionStorage.isEmailed = "false";
	try
	{
		var responseWrapper = Wrapper.createNewIncident(sessionStorage.IncidentID, 
				sessionStorage.CreatedDate,
				sessionStorage.DefaultIncidentType,
				sessionStorage.DefaultCategoryType,
				sessionStorage.IncidentType);
				
				console.log("Response From wrapper:" + responseWrapper + "," + sessionStorage.IncidentType);
				
		if(responseWrapper=="NO"){
		
		var latitude="No Location";
		var longitude="No Location";	
					
		} else {	
		var location=responseWrapper.split(',');
		var latitude=location[0];
		var longitude=location[1];	
		}
		
		console.log(responseWrapper);
		console.log(latitude);
		console.log(latitude);
				
		//alert(responseWrapper);
	}
	catch(e)
	{		
	}

	//Call wrapper for new incident
	
	//Load Default Data
	$("#lblIncidentID").text(sessionStorage.IncidentID);
	$("#lblCreatedDate").text(sessionStorage.CreatedDate);
	NEWINCIDENT.setDropDownValue("ddIncidentType", sessionStorage.DefaultIncidentType);  //$("#ddIncidentType").val(sessionStorage.DefaultIncidentType);
	NEWINCIDENT.setDropDownValue("ddIncidentCategory", sessionStorage.DefaultCategoryType); //$("#ddIncidentCategory").val(sessionStorage.DefaultCategoryType);
	//$("#txtPoliceReferenceNo").val("");
	$("#lblNoOfInjuryPhotos").text("0");
	$("#lblNoOfOtherPhotos").text("0");
	$("#lblGPSLatitude").text(latitude);
	$("#lblGPSLongitude").text(longitude);
	$("#isSynched").hide();
	
	//Crash Details
	/*NEWINCIDENT.setDropDownValue("ddCrashLocation", "Select");
	NEWINCIDENT.setDropDownValue("ddlCrashType", "Select");
	NEWINCIDENT.setDropDownValue("ddlIsPoliceInspected", "Select");*/
	$("#txtNoOfInjured").val(0);
	
	//Environment Details
	/*NEWINCIDENT.setDropDownValue("ddlWeather", "Select");
	NEWINCIDENT.setDropDownValue("ddlEnvRoadType", "Select");
	NEWINCIDENT.setDropDownValue("ddlEnvRoadSurface", "Select");
	NEWINCIDENT.setDropDownValue("ddlEnvSpeed", "Select");*/
	
	//Witness Details
	/*NEWINCIDENT.setDropDownValue("ddlWitnessIsAny", "Select");*/
	$("#txtWitnessName").val("");
	$("#txtWitnessPhoneNo").val("");
	
	$("#viewReport").hide();
	$("#isSynched").hide();
	$("#lblConfirmationMsg").text("No Reports generated.");
	
	//New Fields 29th Mar, 2015
	//$("#txtDamageDet").val("");
		
	
	
	//sessionStorage.DefaultCategoryType = "Select";
	sessionStorage.DefaultIncidentType = "Select";
};
NEWINCIDENT.setDropDownValue=function(id, value)
{
	//console.log(id + "," + value);
	//$("#" + id +" option:contains('" + value + "')").attr("Selected", "Selected");
	//$("#ddIncidentType option:contains('Accident')").prop("selected", "selected");
	
	$('#' + id + ' option').map(function() {
	    if ($(this).text() == value) return this;
	}).prop('selected', 'selected');
	console.log($("#"+id +" option:selected").text());
	return;
};
NEWINCIDENT.readExistingIncident = function()
{
	//sessionStorage.IncidentID = "ID_20150315";
	try
	{
		//Fetch incident details from device memory
		var incidentData = Wrapper.fetchIncident(sessionStorage.IncidentID);
		console.log("Existing Incident Data:" + incidentData);
		
		incidentData = JSON.parse(incidentData);
		
		
		sessionStorage.DefaultCategoryType = incidentData.IncidentCategory;
		NEWINCIDENT.prepareUI();
		
		//Populate data to Screen elements
		$("#lblIncidentID").text(incidentData.IncidentID);
		$("#lblCreatedDate").text(incidentData.CreatedDate);
		NEWINCIDENT.setDropDownValue("ddIncidentType", incidentData.IncidentType); //$("#ddIncidentType").val(incidentData.IncidentType);
		NEWINCIDENT.setDropDownValue("ddIncidentCategory",incidentData.IncidentCategory); //$("#ddIncidentCategory").val(incidentData.IncidentCategory);
		//$("#txtPoliceReferenceNo").val(incidentData.PoliceReferenceNo);
		$("#lblNoOfInjuryPhotos").text(incidentData.NoOfInjuryPhotos);
		$("#lblNoOfOtherPhotos").text(incidentData.NoOfOtherPhotos);
		$("#lblGPSLatitude").text(incidentData.GPSLatitude);
		$("#lblGPSLongitude").text(incidentData.GPSLongitude);
		
		$("#txtOtherDetails").text(incidentData.OtherDetails); //New 
		
		
		//Crash Details
		NEWINCIDENT.setDropDownValue("ddCrashLocation", incidentData.CrashLocation);
		//NEWINCIDENT.setDropDownValue("ddlCrashType", incidentData.CrashType);
		//NEWINCIDENT.setDropDownValue("ddlCrashType", incidentData.CrashType.split(","));  //Multiselect items
		
		$("#ddlCrashType").val(incidentData.CrashType.split(","));
		
		NEWINCIDENT.setDropDownValue("ddlIsPoliceInspected", incidentData.IsPoliceInspected);
		$("#txtNoOfInjured").val(incidentData.NoOfInjured);
		
		//Environment Details
		NEWINCIDENT.setDropDownValue("ddlWeather",  incidentData.EnvWeather);
		NEWINCIDENT.setDropDownValue("ddlEnvRoadType", incidentData.EnvRoadType);
		NEWINCIDENT.setDropDownValue("ddlEnvRoadSurface", incidentData.EnvRoadSurface);
		NEWINCIDENT.setDropDownValue("ddlEnvSpeed", incidentData.EnvSpeed);
		
		//Witness Details
		NEWINCIDENT.setDropDownValue("ddlWitnessIsAny", incidentData.WitnessIsAny);
		$("#txtWitnessName").val(incidentData.WitnessName);
		$("#txtWitnessPhoneNo").val(incidentData.WitnessPhoneNo);
		
		if(incidentData.IsReportGenerated =="true")
		{
			$("#lblConfirmationMsg").text("Report already created!");
			$("#viewReport").show();
		}
		else
		{
			$("#lblConfirmationMsg").text("No report exists!.");
			$("#viewReport").hide();
		}

		if(incidentData.IsEmailed == "true")
		{
			$("#isSynched").show();
			sessionStorage.isEmailed = "true";
		}
		else
		{
			$("#isSynched").hide();
			sessionStorage.isEmailed = "false";
		}
		
		//New Fields 29th Mar, 2015
		NEWINCIDENT.setDropDownValue("ddlThirdPartyDamage", incidentData.IsThirdPartyDamage);
		NEWINCIDENT.setDropDownValue("ddlPoliceContacted", incidentData.IsPoliceContacted);
		$("#txtOtherAccIncDet").val(incidentData.OtherAccidentDet);
		//$("#txtDamageDet").val(incidentData.OtherDamage);

		NEWINCIDENT.setDropDownValue("ddlIsHouseAlarmFitted", incidentData.IsHouseAlarmFitted); //New
		$("#txtOtherDamageDetails").val(incidentData.OtherDamageDetails); //New
		//$("#txtPoliceContactNo").val(incidentData.PoliceContactNo); //New
		NEWINCIDENT.setDropDownValue("ddlVisibility", incidentData.Visibility); //New
		NEWINCIDENT.setDropDownValue("ddlEmergencyServiceType", incidentData.ServiceType); //New
		
		//$("#ddlEmergencyServiceType").val(incidentData.ServiceType.split(","));
		
		$("#per_txtWeatherTime").val(incidentData.WeatherTime); //New
		$("#per_txtWeatherDate").val(incidentData.WeatherDate); //New
	}
	catch(e)
	{
		console.log(e);
	}
};
NEWINCIDENT.saveExistingIncident = function()
{
	//sessionStorage.IncidentID = "ID_20150315";
	try
	{
		//Fetch incident details from device memory
		var incidentData = {}; //Wrapper.fetchIncident(sessionStorage.IncidentID);
	
		//Populate data to Screen elements
		incidentData.IncidentID = $("#lblIncidentID").text();
		incidentData.CreatedDate = $("#lblCreatedDate").text();
		incidentData.IncidentType=$("#ddIncidentType option:selected").text();
		incidentData.IncidentCategory =$("#ddIncidentCategory option:selected").text();
		//incidentData.PoliceReferenceNo = $("#txtPoliceReferenceNo").val();
		incidentData.NoOfInjuryPhotos = $("#lblNoOfInjuryPhotos").text();
		incidentData.NoOfOtherPhotos = $("#lblNoOfOtherPhotos").text();
		incidentData.GPSLatitude = $("#lblGPSLatitude").text();
		incidentData.GPSLongitude = $("#lblGPSLongitude").text();
		
		incidentData.OtherDetails = $("#txtOtherDetails").text(); //New
		
		//Crash Details
		incidentData.CrashLocation = $("#ddCrashLocation option:selected").text();
		
		//Read multiselected item
		incidentData.CrashType = $("#ddlCrashType").val().join(); //$("#ddlCrashType option:selected").text();
		incidentData.IsPoliceInspected = $("#ddlIsPoliceInspected option:selected").text();
		incidentData.NoOfInjured = $("#txtNoOfInjured").val();
		
		//Environment Details
		incidentData.EnvWeather = $("#ddlWeather option:selected").text();
		incidentData.EnvRoadType = $("#ddlEnvRoadType option:selected").text();
		incidentData.EnvRoadSurface = $("#ddlEnvRoadSurface option:selected").text();
		incidentData.EnvSpeed = $("#ddlEnvSpeed option:selected").text();
		
		//Witness Details
		incidentData.WitnessIsAny = $("#ddlWitnessIsAny option:selected").text();
		incidentData.WitnessName = $("#txtWitnessName").val();
		incidentData.WitnessPhoneNo = $("#txtWitnessPhoneNo").val();
		
		if(sessionStorage.isEmailed!=null || sessionStorage.isEmailed != "")
			incidentData.IsEmailed = sessionStorage.isEmailed;
		else
			sessionStorage.isEmailed ="false";		
		
		console.log("DATA:" + JSON.stringify(incidentData));
		
		//New Fields 29th Mar, 2015
		incidentData.IsThirdPartyDamage = $("#ddlThirdPartyDamage option:selected").text();
		incidentData.IsPoliceContacted = $("#ddlPoliceContacted option:selected").text();
		incidentData.OtherAccidentDet = $("#txtOtherAccIncDet").val().replace(/(\r\n|\n|\r)/gm,"");
		//incidentData.OtherDamage = $("#txtDamageDet").val();
		
		//Read values from multiselect dropdown
	
		incidentData.ServiceType = $("#ddlEmergencyServiceType option:selected").text(); 
		incidentData.IsHouseAlarmFitted = $("#ddlIsHouseAlarmFitted option:selected").text(); //New
		incidentData.OtherDamageDetails = $("#txtOtherDamageDetails").val(); //New
		//incidentData.PoliceContactNo = $("#txtPoliceContactNo").val(); //New
		incidentData.Visibility = $("#ddlVisibility option:selected").text(); //New
		incidentData.WeatherTime = $("#per_txtWeatherTime").val(); //New
		incidentData.WeatherDate = $("#per_txtWeatherDate").val(); //New
		
		
		var retVal = Wrapper.saveIncidentData(incidentData.IncidentID, JSON.stringify(incidentData), "", "");
		
		console.log("IsSaved:" + retVal);
	}
	catch(e)
	{
		console.log(e);
	}
};
NEWINCIDENT.generateReport = function(IncidentID)
{
    //**************TEST *****************
    var response = Wrapper.GenerateReport(sessionStorage.IncidentID, 
    		"NEWINCIDENT.generateReport_success", 
    		"NEWINCIDENT.generateReport_failure");
   
    return;

};
NEWINCIDENT.generateReport_success = function(response)
{
	 console.log(response);
	 alert(response);
	 $("#lblConfirmationMsg").text("Insurance Report prepared")
	 $("#viewReport").show();
};
NEWINCIDENT.generateReport_failure = function(response)
{
	alert("Error:"+ response);
};
NEWINCIDENT.viewReport = function()
{
    //**************TEST *****************
    var response = Wrapper.ShowReport(sessionStorage.IncidentID);
   
    if(response.toUpperCase() != "DONE")
    {
    	alert("No Report found.");
    }
};
NEWINCIDENT.emailReport = function()
{
    //**************TEST *****************
    var response = Wrapper.EmailReport(sessionStorage.IncidentID, 
    		"NEWINCIDENT.email_success", "NEWINCIDENT.email_failure");
   
    if(response.toUpperCase() != "DONE")
    {
    	alert("No Report found.");
    }
};
NEWINCIDENT.email_success = function(response)
{
	if(response=="Done")
	{
		$("#isSynched").show();
		sessionStorage.isEmailed = "true";
		NEWINCIDENT.saveExistingIncident();
	}
	else
	{
		$("#isSynched").hide();
	}	
};
NEWINCIDENT.email_failure = function(response)
{
	alert("Send failed.");
	$("#isSynched").hide();
};
NEWINCIDENT.recordAudio = function(IncidentID)
{
    //**************TEST *****************
    if(sessionStorage.IncidentType!="EDIT") {
    var response = Wrapper.recordAudio(sessionStorage.IncidentID,
    		"NEWINCIDENT.recordAudio_success", 
    		"NEWINCIDENT.recordAudio_failure");
    
    	
    	console.log("Response FROM WRAPPER:" + response);
    	if(response.toUpperCase()=="RECORDING STARTED")
    	{
    		console.log("Response FROM WRAPPER YES :" + response);
    		$("#lblActiveRecording").toggleClass("activeRecording");
    	}
    	else if(response.toUpperCase()=="RECORDING STOPPED")
    	{
    		console.log("Response FROM WRAPPER NO :" + response);
    		$("#lblActiveRecording").toggleClass("activeRecording");
    	}
	}
};
NEWINCIDENT.recordAudio_success = function(response)
{
	$("#lblConfirmationMsg").text("Audio Record Completed!")
};
NEWINCIDENT.recordAudio_failure = function(response)
{
	alert("Error:"+ response);
};
NEWINCIDENT.getLocation = function(IncidentID)
{
    //**************TEST *****************
    if(sessionStorage.IncidentType!="EDIT") 
    {
    	var responseWrapper = Wrapper.getLocation(sessionStorage.IncidentID, 
    		"NEWINCIDENT.getLocation_success", 
    		"NEWINCIDENT.getLocation_failure");
    		
    	if(responseWrapper=="NO"){
		
		var latitude="No Location";
		var longitude="No Location";	
					
		} else {	
		var location=responseWrapper.split(',');
		var latitude=location[0];
		var longitude=location[1];	
		}
		
		console.log(responseWrapper);
		console.log(latitude);
		console.log(latitude);
		
		$("#lblGPSLatitude").text(latitude);
		$("#lblGPSLongitude").text(longitude);
		
		return;
	}
    
    if($("#lblGPSLatitude").text().length>0 && $("#lblGPSLongitude").text().length>0 
    		&& $("#lblGPSLatitude").text().toUpperCase()!="NO LOCATION" && $("#lblGPSLongitude").text().toUpperCase()!="NO LOCATION"
    			&& $("#lblGPSLongitude").text().toUpperCase()!="DONE")
	{
		Wrapper.ShowMap($("#lblGPSLatitude").text() + "," + $("#lblGPSLongitude").text());
	}
};

NEWINCIDENT.getLocation_success = function(response)
{
	if(response=="OFF")
	{
		 alert("Switch ON GPS to update Location");
		 $("#lblGPSLatitude").text("No Location");
		 $("#lblGPSLongitude").text("No Location");
	} 
	else 
	{
		alert(response);	
		//Show Location in Map
		if(response.split(",").length == 2)
		{
			Wrapper.ShowMap(response);
		}
	}
	 console.log(response);
	
	$("#lblConfirmationMsg").text("GPS Location Updated!")
};
NEWINCIDENT.getLocation_failure = function(response)
{
	alert("Error:"+ response);
};

NEWINCIDENT.loadSpeed = function()
{
	$("#ddlEnvSpeed").empty();
	$('#ddlEnvSpeed').append('<option value="Select">Select</option>');
	for(var i=0;i<=160;i++)
	{
		var str= i + "Km"
		$('#ddlEnvSpeed').append('<option value="' + i  +'">' + str  + '</option>');
	}
};
NEWINCIDENT.loadIncidentType = function(ddlData)
{
	$("#ddIncidentType").empty();
	$('#ddIncidentType').append('<option value="Select">Select</option>');
	var length = ddlData.length;
	for(var i=0;i<length;i++)
	{
		var listItem = ddlData[i];
		$('#ddIncidentType').append('<option value="' + listItem.Key +'">' + listItem.Value  + '</option>');
	}
};
NEWINCIDENT.loadCategoryType = function(ddlData)
{
	$("#ddIncidentCategory").empty();
	$('#ddIncidentCategory').append('<option value="Select">Select</option>');
	var length = ddlData.length;
	for(var i=0;i<length;i++)
	{
		var listItem = ddlData[i];
		$('#ddIncidentCategory').append('<option value="' + listItem.Key +'">' + listItem.Value  + '</option>');
	}
};
NEWINCIDENT.loadLocation = function(ddlData)
{
	$("#ddCrashLocation").empty();
	$('#ddCrashLocation').append('<option value="Select">Select</option>');
	var length = ddlData.length;
	for(var i=0;i<length;i++)
	{
		var listItem = ddlData[i];
		$('#ddCrashLocation').append('<option value="' + listItem.Key +'">' + listItem.Value  + '</option>');
	}
};
NEWINCIDENT.loadInjuryType = function(ddlData)
{
	$("#ddInjuryType").empty();
	$('#ddInjuryType').append('<option value="Select">Select</option>');
	var length = ddlData.length;
	for(var i=0;i<length;i++)
	{
		var listItem = ddlData[i];
		$('#ddInjuryType').append('<option value="' + listItem.Key +'">' + listItem.Value  + '</option>');
	}
};
NEWINCIDENT.loadOtherPhotoList = function(ddlData)
{
	$("#ddOtherPhotoCat").empty();
	$('#ddOtherPhotoCat').append('<option value="Select">Select</option>');
	var length = ddlData.length;
	for(var i=0;i<length;i++)
	{
		var listItem = ddlData[i];
		$('#ddOtherPhotoCat').append('<option value="' + listItem.Key +'">' + listItem.Value  + '</option>');
	}
};
NEWINCIDENT.loadPointOfImpact = function(ddlData)
{
	$("#ddlCrashType").empty();
	$('#ddlCrashType').append('<option value="Select">Select</option>');
	var length = ddlData.length;
	for(var i=0;i<length;i++)
	{
		var listItem = ddlData[i];
		$('#ddlCrashType').append('<option value="' + listItem.Key +'">' + listItem.Value  + '</option>');
	}
};
NEWINCIDENT.prepareUI = function()
{
	//Load Speed Dropdown
	NEWINCIDENT.loadSpeed();
	
	//Load Incident Type
	//sessionStorage.DefaultCategoryType = "House"; 
	//DefaultIncidentType ="Accident"
    if(sessionStorage.DefaultCategoryType.toUpperCase() == "VEHICLE")
    {
    	NEWINCIDENT.loadIncidentType(NEWINCIDENT.ddlIncidentType_vehicle);
    	NEWINCIDENT.loadCategoryType(NEWINCIDENT.ddlCatType_vehicle);
    	NEWINCIDENT.loadLocation(NEWINCIDENT.ddlLocation_vehicle);
    	NEWINCIDENT.loadInjuryType(NEWINCIDENT.ddlInjuryType_vehicle);
    	NEWINCIDENT.loadOtherPhotoList(NEWINCIDENT.ddlOtherPhoto_vehicle);
    	NEWINCIDENT.loadPointOfImpact(NEWINCIDENT.ddlCrashType_vehicle);
    	//show Incident Type in panel2
    	//$("#lblPanel2IncType").show(); //Based on change request on 29th Mar, 2015
    	//$("#ddlPanel2IncTypeDD").show(); //Based on change request on 29th Mar, 2015
    	$("#box6").show();
    	$("#box7").show();
    	$("#liLblThirdParty").show();
    	$("#liDDThirdParty").show();
    	$("#lblOtherDamageDetails").show();
    	$("#txtOtherDamageDetails").show();
    	
    	$("#lblddlIsHouseAlarmFitted").hide();
    	$("#ddlIsHouseAlarmFitted").hide();
    }
    else if(sessionStorage.DefaultCategoryType.toUpperCase() == "PROPERTY")
    {
    	NEWINCIDENT.loadIncidentType(NEWINCIDENT.ddlIncidentType_property);
    	NEWINCIDENT.loadCategoryType(NEWINCIDENT.ddlCatType_property);
    	NEWINCIDENT.loadLocation(NEWINCIDENT.ddlLocation_property);
    	NEWINCIDENT.loadInjuryType(NEWINCIDENT.ddlInjuryType_property);
    	NEWINCIDENT.loadOtherPhotoList(NEWINCIDENT.ddlOtherPhoto_property);
    	NEWINCIDENT.loadPointOfImpact(NEWINCIDENT.ddlCrashType_property);
    	//hide Incident Type in panel2
    	//$("#lblPanel2IncType").hide(); //Based on change request on 29th Mar, 2015
    	//$("#ddlPanel2IncTypeDD").hide(); //Based on change request on 29th Mar, 2015
    	$("#box6").hide();
    	$("#box7").hide();
    	$("#liLblThirdParty").hide();
    	$("#liDDThirdParty").hide();
    	$("#lblOtherDamageDetails").show();
    	$("#txtOtherDamageDetails").show();
    	$("#lblOtherDamageDetails").text("Damage");
    	
    	$("#lblddlIsHouseAlarmFitted").show();
    	$("#ddlIsHouseAlarmFitted").show();
    }    
};

$(document).ready(function()
{
	$("#lblOtherDetails").hide();
	$("#txtOtherDetails").hide();
	
	//If new Incident create dynamic number for Incident
	if(sessionStorage.IncidentType == "NEW")
	{
		NEWINCIDENT.prepareUI();
		$("#lblScreenSubTitle").text("New Incident");
		NEWINCIDENT.createNewIncident();
	}
	else if(sessionStorage.IncidentType == "EDIT")
	{
		$("#lblScreenSubTitle").text("Update Incident");
		NEWINCIDENT.readExistingIncident();
	}
	
	if($("#ddlThirdPartyDamage option:selected").text().toUpperCase()=="YES")
	{
		$("#lblOtherDamageDetails").show();
		$("#txtOtherDamageDetails").show();
	}
	else
	{
		if($("#ddlThirdPartyDamage").is(':visible'))
		{
			$("#lblOtherDamageDetails").hide();
			$("#txtOtherDamageDetails").hide();
		}
		else
		{
			$("#lblOtherDamageDetails").show();
			$("#txtOtherDamageDetails").show();
		}
	}
	
	COMMON.CurrentPage = COMMON.SCREENS.NewIncident;
	
	//if(NEWINCIDENT.activeBox==1){$(".nav_left").hide();}	
	if(NEWINCIDENT.activeBox==9){$(".nav_right").hide();}
	
	if(NEWINCIDENT.isSwipeRequied)
	{
		$('#scroll_container').on("swiperight",function(e){
			if(e) e.preventDefault();
			NEWINCIDENT.btnNavLeft_click(e);
		});
		$('#scroll_container').on("swipeleft",function(e){
			if(e) e.preventDefault();
			NEWINCIDENT.btnNavRight_click(e);
		});
	}
	
	$("#btnHome").on("click", NEWINCIDENT.btnHome_click);
	$(".nav_left").on("click", NEWINCIDENT.btnNavLeft_click);
	$(".nav_right").on("click", NEWINCIDENT.btnNavRight_click);
	
	$("#viewInjuryPhoto").on("click", NEWINCIDENT.viewInjuryPhoto_click);
	$("#viewOtherPhoto").on("click", NEWINCIDENT.viewOtherPhoto_click);
	
	$("#takeInjuryPhoto").on("click", NEWINCIDENT.takeInjuryPhoto_click);
	$("#takeOtherPhoto").on("click", NEWINCIDENT.takeOtherPhoto_click);
	
	$("#generateReport").on("click", NEWINCIDENT.generateReport);	
	$("#viewReport").on("click", NEWINCIDENT.viewReport);
	$("#sendEmail").on("click", NEWINCIDENT.emailReport);
	$("#recordAudio").on("touchstart", NEWINCIDENT.recordAudio);
	$("#getLocation").on("click", NEWINCIDENT.getLocation);
	
	//New Event - 06-Apr-2015
	$("#per_txtWeatherDate").on("click",NEWINCIDENT.selectDate_click);
	$("#per_txtWeatherTime").on("click",NEWINCIDENT.selectTime_click);
	
	$("#ddIncidentType").change(function(){
		if($("#ddIncidentType option:selected").text().toUpperCase()=="OTHER")
		{
			$("#lblOtherDetails").show();
			$("#txtOtherDetails").show();
		}
		else
		{
			$("#lblOtherDetails").hide();
			$("#txtOtherDetails").hide();
		}
	});
	$("#ddlThirdPartyDamage").change(function(){
		if($("#ddlThirdPartyDamage option:selected").text().toUpperCase()=="YES")
		{
			$("#lblOtherDamageDetails").show();
			$("#txtOtherDamageDetails").show();
		}
		else
		{
			$("#lblOtherDamageDetails").hide();
			$("#txtOtherDamageDetails").hide();
		}
	});
	
	//alert(sessionStorage.activeIndex);
	if(sessionStorage.activeIndex!=null && sessionStorage.activeIndex.length>0)
	{
		var id= "#box" + sessionStorage.activeIndex;
		$('#scroll_container').scrollTo($(id), 100);
		if(sessionStorage.activeIndex=="6" || sessionStorage.activeIndex=="7")
		{
			$(".nav_left").hide();
			$(".nav_right").show();
		}
		else
		{
			$(".nav_left").show();
		}
		if(sessionStorage.activeIndex="6" || sessionStorage.activeIndex=="7")
		{
			$(".nav_left").show();
			$(".nav_right").hide();
		}
		NEWINCIDENT.activeBox = parseInt(sessionStorage.activeIndex);
		sessionStorage.activeIndex=null;
	}
});

/**** New Functions *************/
NEWINCIDENT.activeDP = "";
NEWINCIDENT.selectDate_click = function()
{
	NEWINCIDENT.activeDP = "";
	var title = $(this).attr("name");
	//$(this).val("2015/03/10");
	NEWINCIDENT.activeDP = $(this).attr("id");
	var currDate= new Date();
	var day =  currDate.getDate();
	var month =  currDate.getMonth()+1;
	var year =  currDate.getFullYear()+1;
	
	var existingVal = $(this).val();
	var defaultDate = [year,month,day];	
	
	if(existingVal.length>0)
	{
		defaultDate = existingVal.split("/");
	}
	//alert(defaultDate);
	
	var requestdata='{"Request":{"Title":"' + title + '", "DefaultDay":"' + defaultDate[2] + 
	'","DefaultMonth":"' + defaultDate[1] + '", "DefaultYear":"' + defaultDate[0] + '"}}'; 
	
	var retVal = Wrapper.pickDate(requestdata, "NEWINCIDENT.success_dp", "NEWINCIDENT.failure_dp");
};

NEWINCIDENT.success_dp = function(selectedDate)
{
	//alert(selectedDate)
	$("#" + NEWINCIDENT.activeDP).val(selectedDate);
};
NEWINCIDENT.failure_dp = function(selectedDate)
{
	alert(selectedDate);
};

NEWINCIDENT.activeTP = "";
NEWINCIDENT.selectTime_click = function()
{
	NEWINCIDENT.activeTP = "";
	var title = $(this).attr("name");
	//$(this).val("2015/03/10");
	NEWINCIDENT.activeTP = $(this).attr("id");
	var currDate= new Date();
	var hours =  currDate.getHours();
	var min =  currDate.getMinutes();
	var ampm = "AM";
	//Check AM or PM
	if((hours >12))
		{
		ampm = "PM"
			hours = hours-12;
		}
	else if(hours==12)
		{
		ampm ="PM";
		}
	else
		{
		ampm ="AM";
		}
	
	var existingVal = $(this).val();
	var defaultDate = [hours,(min + ' ' + ampm)];	
	
	if(existingVal.length>0)
	{
		defaultDate = existingVal.split(":");
	}
	//alert("defaultDate:" + defaultDate);
	var arr2 = defaultDate[1].split(" ");
	//alert("arr2:"+ arr2);
	
	var requestdata='{"Request":{"Title":"' + title + '", "DefaultHour":"' + defaultDate[0] + 
	'","DefaultMin":"' + arr2[0] + '","DefaultAMPM":"' + arr2[1] + '"}}'; 
	
	//alert(requestdata);
	
	var retVal = Wrapper.pickTime(requestdata, "NEWINCIDENT.success_tp", "NEWINCIDENT.failure_tp");
};

NEWINCIDENT.success_tp = function(selectedDate)
{
	//alert(selectedDate)
	$("#" + NEWINCIDENT.activeTP).val(selectedDate);
};
NEWINCIDENT.failure_tp = function(selectedDate)
{
	alert(selectedDate);
};