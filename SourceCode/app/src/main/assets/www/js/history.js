var HISTORY={};
HISTORY.onLoad=function()
{
}
HISTORY.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	window.location = "home.html";
};

$(document).ready(function()
{
	sessionStorage.isFromHistory = "FALSE";
	COMMON.CurrentPage = COMMON.SCREENS.History;
	
	$(".header_left").on("touchstart", HISTORY.btnHome_click);
	
	/*$(".scroll_wrapper").delegate(".list_item", "swipeleft", 
			function()
			{
				alert($(this).attr("name"));
			});
	*/
	
	HISTORY.fetchHistory(); //Read history date from mobile storage
});

HISTORY.fetchHistory = function()
{
	//call wrapper to fetch profile data		
	try
	{
		var historyData = Wrapper.fetchHistory("HISTORY.fetch_history_success_callaback", "HISTORY.fetch_history_failure_callaback");
		console.log(profileData);
	}
	catch(e)
	{
		//write error log
	}
};
HISTORY.loadEmpty = function()
{
	$(".scroll_wrapper").empty();
	var template= '<div class="list_item">'+
	'<div class="left_content">'+
		'<div class="list_item_title_main">'+
			'No user history data available anymore.' +
		'</div>'+
		'<div class="list_item_title_sub">'+
		'</div>'+
	'</div>'+
	'<div class="right_content" style="display:none">'+
	'</div>'+
'</div>';
$(".scroll_wrapper").append(template);
};

HISTORY.fetch_history_success_callaback = function(response)
{
	try
	{
		//validate response data
		if(response==null || response=='undefined' || response == undefined)
		{
			HISTORY.loadEmpty();
			return;
		}
		console.log("REsponse:" + response);
		//After validation success go further
		var historyArray = JSON.parse(response);
		if(historyArray.length < 1)
		{
			HISTORY.loadEmpty();
			return;
		}
	
		//Load dummy items
		$("#lblRecordCount").text(historyArray.length);
		$(".scroll_wrapper").empty();
		var historyItemIndex ={};
		for(historyItemIndex in historyArray)
		{
			var historyObject = historyArray[historyItemIndex];
			
			var emailTick = '<span class="sync_tick"></span>';
			if(historyObject.IsEmailed !="true")
			{
				emailTick ="";
			}		
			console.log("emailTick:"+ emailTick);
			
			var template= '<div class="list_item" name="'+ historyObject.IncidentID +'">'+
					'<div class="left_content">'+
						'<a href="#" onclick="HISTORY.historyItem_click(\''+ historyObject.IncidentID +'\',\'' + 
						historyObject.IncidentCategory + '\');">' +
						'<div class="list_item_title_main">'+
							historyObject.IncidentID +
						'</div>'+
						'<div class="list_item_title_sub">'+
							'Created on '+ historyObject.CreatedDate +
						'</div>'+
					'</div></a>'+
					'<div class="right_content" id="'+ historyObject.IncidentID +'">'+
					emailTick+
					'</div>'+
					'<div class="right_content_report" id="vr'+ historyObject.IncidentID +'">'+
					'</div>'+
				'</div>';
			
			$(".scroll_wrapper").append(template);
			
			var idStr = "#"+historyObject.IncidentID;
			
			var idStr2 = "#vr" + historyObject.IncidentID;
			
			$(idStr).on("click",function()
			{
				var id = $(this).attr('id');
				HISTORY.emailReport(id);
			});
			$(idStr2).on("click",function(){
				var id = $(this).attr('id').replace("vr","");
				HISTORY.viewReport(id);
			});
			
			$(idStr).swipe( {
			      swipe:function(event, direction, distance, duration, fingerCount, fingerData) {
			        //console.log("You swiped " + direction + " with " + fingerCount + " fingers");
			        //alert($(this).attr('id'));
			    	  if(direction=="right")
			    	  {
			    		 //call delete function and pass incident ID
			    		 var incidentID = $(this).attr('id');
			    		 console.log("Selected ID:" + incidentID);
			    		 var retVal = confirm("Are you sure, you want to delete Incident (" +incidentID +")?");
			    		 console.log("RetVal" + retVal);
			    		 if(retVal==true)
			    		 {
			    			 var deleteRes = Wrapper.deleteIncident(incidentID);
			    			 console.log(deleteRes);
			    			 if(deleteRes=="DONE")
			    			 {
			    				 HISTORY.fetchHistory();
			    			 }
			    		 }
			    	  }
			      },
			      threshold:0,
			      fingers:1
			    });
		}
	}
	catch(e)
	{
		console.log(e);
		HISTORY.loadEmpty();
	}
};
HISTORY.fetch_history_failure_callaback = function(response)
{
	
};
HISTORY.historyItem_click = function(incidentID, incidentCat)
{
	sessionStorage.activeIndex =null;
	sessionStorage.IncidentType = "EDIT";
	sessionStorage.IncidentID = incidentID;
	sessionStorage.isFromHistory = "TRUE";
	if(incidentCat.toUpperCase()=="VEHICLE")
	{
		COMMON.NavigatePage(COMMON.SCREENS.NewIncident_Car);
	}
	else
	{
		COMMON.NavigatePage(COMMON.SCREENS.NewIncident_Property);
	}
	//COMMON.NavigatePage(COMMON.SCREENS.NewIncident);
};
HISTORY.viewReport = function(incidentID)
{
    //**************TEST *****************
    var response = Wrapper.ShowReport(incidentID);
   
    if(response.toUpperCase() != "DONE")
    {
    	alert("No Report found.");
    }
};
HISTORY.emailReport = function(incidentID)
{
    //**************TEST *****************
    var response = Wrapper.EmailReport(incidentID, 
    		"HISTORY.email_success", "HISTORY.email_failure");
   
    if(response.toUpperCase() != "DONE")
    {
    	alert("No Report found.");
    }
};
HISTORY.email_success = function(response)
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
HISTORY.email_failure = function(response)
{
	alert("Send failed.");
	$("#isSynched").hide();
};