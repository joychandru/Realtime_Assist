var SHOW_MYLOCATION={};
SHOW_MYLOCATION.isStatcMap=true;
SHOW_MYLOCATION.mymapBranchView =null;
SHOW_MYLOCATION.isLocationChanged=false;
sessionStorage.changedLat ="";
sessionStorage.changedLng="";
SHOW_MYLOCATION.onLoad=function()
{
}
SHOW_MYLOCATION.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	window.location = COMMON.parentPage;
};
var markersArray = [];
SHOW_MYLOCATION.clearOverlays = function() {
	  if (markersArray) {
	    for (i in markersArray) {
	      markersArray[i].setMap(null);
	    }
	  }
	}
var marker1=null;
SHOW_MYLOCATION.showLocation = function(latitude, longitude)
{
	try
	{
		 mymapBranchView = new google.maps.Map(document.getElementById('mapView2'), {
	           zoom: 16,
	           center: new google.maps.LatLng(latitude, longitude),
	           mapTypeId: google.maps.MapTypeId.ROADMAP
	         });
	         
	         //google.maps.event.trigger(mymapBranchView, 'resize');
	         
	         google.maps.event.addListenerOnce(mymapBranchView, 'idle', function(){
	        	alert("Location is successfully loaded");
	        	});
	         
	        var viewport = {
				width: jQuery(window).width(),
				height: jQuery(window).height()
			};
	          
	        
			 var iconpath = 'images/email.png';

			 var infowindow = new google.maps.InfoWindow();
			 
	             google.maps.event.addListener(mymapBranchView, "click", function(event,latlng) {
	            	 var jsonStr = JSON.stringify(event);
	            	 var jsonObj = JSON.parse(jsonStr);
	            	
	            	 var latStr = jsonObj.latLng.lat;
	            	 var lngStr = jsonObj.latLng.lng;
	            	 
	            	 if(jsonObj.latLng.G!=undefined && jsonObj.latLng.G!=null)
	            		 {
	            		 latStr = jsonObj.latLng.G;
	            		 }
	            	 if(jsonObj.latLng.K!=undefined && jsonObj.latLng.K!=null)
            		 {
	            		 lngStr = jsonObj.latLng.K;
            		 }
	            	             	 
	            	 var latitude =sessionStorage.changedLat;
	            	 var longitude=sessionStorage.changedLng;
	            	 
	            	 if(latStr!=null && latStr!= undefined && latStr!="")
            		 {
	            		 SHOW_MYLOCATION.isLocationChanged=true;
	            		 latitude = latStr;	 
            		 }
	            	 if(lngStr!=null && lngStr!= undefined && lngStr!="")
            		 {
	            		 SHOW_MYLOCATION.isLocationChanged=true;
	            		 longitude = lngStr;	 
            		 }
	            	 
	            	 //alert(latitude + "," + longitude);
		        	 
		        	 sessionStorage.changedLat =latitude;
		        	 sessionStorage.changedLng=longitude;
		        	 $("#txtSetLat").val(latitude);
		     		 $("#txtSetLong").val(longitude);
	            	 
		     		 SHOW_MYLOCATION.showLocation(latitude, longitude);
	            	 SHOW_MYLOCATION.showStaticLocation(latitude, longitude);
	            	 
	            	 Wrapper.pushLocation(latitude,longitude);
	            	 
		        	 //Read lat and long values and call map view function
		        	 
		            /* if (true) 
		             {
		            	 SHOW_MYLOCATION.clearOverlays();
		            	 
		            	 if(marker1 !=null && marker1 != 'undefined')
		            	 {
		            		 setTimeout(function(){
		            		 marker1.setMap(null);
		            		 marker.setMap(null);
		            		 },100);
		            		 //mymapBranchView.removeOverlay(marker1);
		            	 }
		            	 marker1 = new google.maps.Marker({position: event.latLng, map: mymapBranchView});
		            	 markersArray.push(marker1);
		            	 
		                google.maps.event.addListener(marker1, "click", function() {
		                	//alert("test")
		                   var html = "<table>" +
		                              "<tr><td>Lat:</td> <td></td></tr>" +
		                              "<tr><td>Long:</td> <td></td> </tr>" +
		                              "<tr><td></td><td><input type='button' value='Save & Close' onclick='saveData()'/></td></tr>";
		                   //marker1.openInfoWindow(html);
		                   infowindow.setContent(html);
			               infowindow.open(mymapBranchView, marker1);
		                 })(marker1);

		              }*/
		             });
	             
	             var marker;
	             marker = new google.maps.Marker({
	             position: new google.maps.LatLng(latitude, longitude),
	             map: mymapBranchView,
	             /*icon: iconpath */
	           });
	             
	             markersArray.push(marker);
	             
	        google.maps.event.addListener(marker, 'click', 
	            (function(marker) 
	            {
	                return function() 
	                {
	                	 infowindow.setContent("<body><h4>My Location </h4></body>");
		                 infowindow.open(mymapBranchView, marker);
	                }
	            })(marker));
	}
	catch(e)
	{
		alert(e);
	}
};
SHOW_MYLOCATION.getDirection = function(fromlatitude, fromlongitude, tolatitude, tolongitude)
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
SHOW_MYLOCATION.showStaticLocation = function(latitude, longitude)
{
	try
	{
		var img = document.getElementById("mapView");
		//get height and width
		var width = img.clientWidth;
		var height = img.clientHeight;
		$("#txtSetLat").val(latitude);
		$("#txtSetLong").val(longitude);
		img.src ="";
		img.src = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&"+ //40.702147,-74.015794&zoom=13&"+
		//"maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794";
		"size="+width +"x" + height +"&maptype=roadmap&markers=color:red%7Clabel:S%7C"+latitude+","+longitude;
		
		img.onload = function()
		{
			//alert("Location is successfully loaded");
			var canvas = document.getElementById('mycanvas') // reference to canvas element
			
			var ctx = canvas.getContext('2d'); // get the canvas context;
			ctx.canvas.height = height;
			ctx.canvas.width = width;
			ctx.drawImage(img, 0, 0, width, height); //draw image into canvas;
			var strDataURI = canvas.toDataURL(); 
			Wrapper.saveImage(strDataURI);
		}
	}
	catch(e)
	{
		alert(e);
	}
}
SHOW_MYLOCATION.pushLocation = function(latitude, longitude)
{
	SHOW_MYLOCATION.isLocationChanged=false;
	sessionStorage.changedLat =latitude;
	sessionStorage.changedLng=longitude;
	
	console.log(latitude + "," + longitude);
	
	$("#txtSetLat").val(latitude);
	$("#txtSetLong").val(longitude);
	
	if(SHOW_MYLOCATION.isStatcMap == true)
	{
		SHOW_MYLOCATION.showStaticLocation(latitude, longitude);
	}
	else
	{
		SHOW_MYLOCATION.showLocation(latitude, longitude);
		
	}
};
SHOW_MYLOCATION.locFailed = function(response)
{
	console.log("Failed" + response);
};
SHOW_MYLOCATION.btnStatic_click =function()
{
	$("#mapView").addClass("displayShow");
	$("#mapView2").removeClass("displayShow");
	
	$(".buttonStatic").addClass("selectedButton");
	$(".buttonDynamic").removeClass("selectedButton");
	
	SHOW_MYLOCATION.isStatcMap = true;

	if(SHOW_MYLOCATION.isLocationChanged==false)
	{
		setTimeout(function(){
			Wrapper.getShowLocation();
		}, 1000);
	}
	else 
	{
		SHOW_MYLOCATION.showStaticLocation(
		sessionStorage.changedLat, 
		sessionStorage.changedLng);
	}
};
SHOW_MYLOCATION.btnDynamic_click =function()
{
	$("#mapView2").addClass("displayShow");
	$("#mapView1").removeClass("displayShow");
	
	$(".buttonDynamic").addClass("selectedButton");
	$(".buttonStatic").removeClass("selectedButton");
	
	SHOW_MYLOCATION.isStatcMap = false;
	
	if(SHOW_MYLOCATION.isLocationChanged==false)
	{
		setTimeout(function(){
			Wrapper.getShowLocation();
		}, 1000);
	}
	else 
	{
		SHOW_MYLOCATION.showStaticLocation(
		sessionStorage.changedLat, 
		sessionStorage.changedLng);
	}
};
SHOW_MYLOCATION.btnSetLocation_click =function()
{
	var lat= $("#txtSetLat").val();
	var long= $("#txtSetLong").val();
	SHOW_MYLOCATION.isLocationChanged=true;
	sessionStorage.changedLat =lat;
	sessionStorage.changedLng=long;
	Wrapper.pushLocation(lat,long);
	SHOW_MYLOCATION.showStaticLocation(lat,long);
};

$(document).ready(function()
{
	SHOW_MYLOCATION.isStatcMap = true;
	SHOW_MYLOCATION.isLocationChanged==false;
	
	setTimeout(function(){
		Wrapper.getShowLocation();
	}, 1000);
	
	//SHOW_MYLOCATION.pushLocation("10","79");
	COMMON.CurrentPage = COMMON.SCREENS.ShowMap;
	$(".header_left").on("click", SHOW_MYLOCATION.btnHome_click);	
	$(".buttonStatic").on("click", SHOW_MYLOCATION.btnStatic_click);	
	$(".buttonDynamic").on("click", SHOW_MYLOCATION.btnDynamic_click);	
	$("#btnSetLocation").on("click", SHOW_MYLOCATION.btnSetLocation_click);
});
