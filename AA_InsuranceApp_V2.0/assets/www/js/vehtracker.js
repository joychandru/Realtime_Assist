var VEHICLE_TRACKER={};
VEHICLE_TRACKER.mymapBranchView =null;
VEHICLE_TRACKER.onLoad=function()
{
}
VEHICLE_TRACKER.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	window.location = COMMON.parentPage;
};
VEHICLE_TRACKER.showLocation = function(latitude, longitude)
{
	try
	{
		
		 mymapBranchView = new google.maps.Map(document.getElementById('mapView'), {
	           zoom: 15,
	           center: new google.maps.LatLng(latitude, longitude),
	           mapTypeId: google.maps.MapTypeId.ROADMAP
	         });
	         
	         google.maps.event.trigger(mymapBranchView, 'resize');
	         
	        var viewport = {
				width: jQuery(window).width(),
				height: jQuery(window).height()
			};
	          
			 var iconpath = 'images/email.png';

			 var infowindow = new google.maps.InfoWindow();
	         var marker;
	             marker = new google.maps.Marker({
	             position: new google.maps.LatLng(latitude, longitude),
	             map: mymapBranchView,
	             /*icon: iconpath */
	           });
	         google.maps.event.addListener(marker, 'click', 
	            (function(marker) 
	            {
	                return function() 
	                {
	                	 /*infowindow.setContent("<body><h1>My Location </h1></body>");
		                 infowindow.open(mymapBranchView, marker);*/
	                }
	            })(marker));
	}
	catch(e)
	{
		alert(e);
	}
};
VEHICLE_TRACKER.getDirection = function(fromlatitude, fromlongitude, tolatitude, tolongitude)
{
	try
	{
		var directionsDisplay = new google.maps.DirectionsRenderer();	
		var directionsService = new google.maps.DirectionsService();
	
	    var centerpoint = new google.maps.LatLng(fromlatitude, fromlongitude);
	
	    var start =fromlatitude + "," + fromlongitude;
	    var end = tolatitude + "," + tolongitude;		    
	    
    	var myOptions = {
	     	zoom: 6,
	      center: centerpoint,
	      mapTypeId: google.maps.MapTypeId.ROADMAP,
	      zoomControl: true
	    }
	    mymapBranchView = new google.maps.Map(document.getElementById("mapView"), myOptions);		   
	    directionsDisplay.setMap(mymapBranchView);			    
	    
	    //set Direction
	    var request = {
	      origin:start, 
	      destination:end,
	      travelMode: google.maps.DirectionsTravelMode.DRIVING
	    };
	    directionsService.route(request, function(response, status) 
	    {
	      if (status == google.maps.DirectionsStatus.OK) 
	      {
	    	  directionsDisplay.setDirections(response);	
	      }
	    });
	}
	catch(e)
	{
		
	}
};
VEHICLE_TRACKER.btnTrackLocation_click = function()
{
	var retVal = Wrapper.TrackLocaiton($("#vehGPSNumber").val(),
			"VEHICLE_TRACKER.locSuccess", 
			"VEHICLE_TRACKER.locFailed");
	$("#lblStatus").text("Waiting for target location..");
};
VEHICLE_TRACKER.locSuccess = function(response)
{
	console.log(response);
	if(response.toUpperCase()=="NO")
	{
		$("#lblStatus").text("Unable to retrieve target Location.");
		return;
	}
	if(response.toUpperCase()=="WAITING..")
	{
		$("#lblStatus").text("Waiting for target location..");
		return;
	}
	$("#lblStatus").text("");
	
	//Step1: Read Current Location
	var locationDataStr = Wrapper.getLocationData();
	console.log("Current Location:"+ locationDataStr);
	var locationJSON;
	var target = response.split(",");
	
	var fromLat= "-33.7968037";
	var fromLong= "151.1781136";
	if(locationDataStr!=null && locationDataStr != "OFF")
	{
		locationJSON = JSON.parse(locationDataStr);
		fromLat = locationJSON["GPSLatitude"];
		fromLong = locationJSON["GPSLongitude"];
	}

	//Step2: Read GPS Device Location
	VEHICLE_TRACKER.getDirection(fromLat,fromLong, target[0],target[1])
};
VEHICLE_TRACKER.locFailed = function(response)
{
	console.log("Failed" + response);
};
$(document).ready(function()
{
	COMMON.CurrentPage = COMMON.SCREENS.Tracker;
	
	//VEHICLE_TRACKER.showLocation("-33.7968037","151.1781136");
	
	$("#btnTrackLocation").on("click", VEHICLE_TRACKER.btnTrackLocation_click);
	$(".header_left").on("click", VEHICLE_TRACKER.btnHome_click);
	
	try
	{
		var profileData = Wrapper.fetchProfileData("PROFILE.fetch_profile_success_callaback", "PROFILE.fetch_profile_failure_callaback");
		var GPSNumber = JSON.parse(profileData).GPSNumber;
		$("#vehGPSNumber").val(GPSNumber);
	}
	catch(e)
	{
	}	
});
