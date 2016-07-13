var GALLERY={};
GALLERY.onLoad=function()
{
}
GALLERY.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	window.location = COMMON.parentPage;
};
GALLERY.loadEmpty = function()
{
	$(".scroll_wrapper").empty();
	var template= '<div class="photobox" style="width:100%;text-align:center;color:yellow;font-weight:bold;">'+
			'<div class="photoinnner">'+
		'No Records found.'+
		'<div class="photodate"></div>'+
		'</div>'+		
		'</div>';
	$(".scroll_wrapper").append(template);
};
var data = "";
GALLERY.success_callback = function(response)
{
	//Validation Goes here
	if(response==null || response=='undefined' || response == undefined)
	{
		GALLERY.loadEmpty();
		return;
	}
	
	var jsonImageArray = JSON.parse(response);
	console.log(typeof jsonImageArray);
	if(jsonImageArray.length < 1)
	{
		GALLERY.loadEmpty();
		return;
	}
	
	$("#lblRecordCount").text(jsonImageArray.length);
	$(".scroll_wrapper").empty();
	var index;
	for(index in jsonImageArray)
	{
		var photoObject = jsonImageArray[index];
	
		var template= '<div class="photobox">'+
			'<div class="photoinnner">'+
		'<img src="' +  photoObject.ImageData  + '" class="phototile"/>'+
		'<div class="photodate">'+ photoObject.CreatedDate +'</div>'+
		'</div>'+
		'<a class="deleteButton" href="#delPhoto" onclick="GALLERY.deleteImage_click(\''+ photoObject.FileName +'\');"></a>'+			
		'</div>';
		$(".scroll_wrapper").append(template);
	}
};
GALLERY.failure_callback = function(response)
{
	alert("Failed, Message:" + response);
};
GALLERY.deleteImage_click = function(fileName)
{
	//if(e) e.preventDefault();
	//alert(fileName);
	
	//Show Confirmation for delete
	
	
	try
	{
		var deleteResponse = Wrapper.deletePhoto(sessionStorage.IncidentID, sessionStorage.Category, fileName);
		
		if(deleteResponse == "Done")
		{
			GALLERY.loadImage();
		}
	}
	catch(e)
	{
		
	}
};
GALLERY.loadImage = function()
{
	//Retrieve session Values and trigger Wrapper
	var JSONStrPhotots = "";
	try
	{
		JSONStrPhotots = Wrapper.fetchPhotos(sessionStorage.IncidentID, sessionStorage.Category, sessionStorage.prefix,
				"GALLERY.success_callback", "GALLERY.failure_callback");
	}
	catch(e)
	{	
		alert("Error Invoking Native code!");
	}
};
$(document).ready(function()
{
	COMMON.CurrentPage = COMMON.SCREENS.Gallery;
	COMMON.parentPage = sessionStorage.parentPhotoPage;

	$(".header_left").on("click", GALLERY.btnHome_click);
	
	//Load images
	GALLERY.loadImage();
});




