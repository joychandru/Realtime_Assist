var PROFILE={};
PROFILE.onLoad=function()
{
}
PROFILE.btnHome_click = function(e)
{
	if(e) e.preventDefault();
	window.location = COMMON.SCREENS.Home;
};
PROFILE.setSessionValues = function()
{
	if(sessionStorage!=null)
	{
		sessionStorage.parentPhotoPage = COMMON.SCREENS.MyProfile; //Parent Page
		sessionStorage.IncidentID = "Profile";
		sessionStorage.Category = "MyPhotos";
		sessionStorage.prefix = $("#per_selectPhotoType").val();
	}
};
PROFILE.viewProfilePhoto_click = function(e)
{
	if(e) e.preventDefault();
	PROFILE.setSessionValues();
	COMMON.NavigatePage(COMMON.SCREENS.Gallery);
	return false;
};
PROFILE.takeProfilePhoto_click = function(e)
{
	if(e) e.preventDefault();
	
	if($("#per_selectPhotoType").val().toUpperCase() =="SELECT")
	{
		alert("Please select photo type.");
		return;
	}
	
	PROFILE.setSessionValues();

	//Call Wrapper for photo 
	var responseWrapper = "";
	try
	{
		responseWrapper = Wrapper.takePhotos(sessionStorage.IncidentID, sessionStorage.Category, sessionStorage.prefix, 
				"PROFILE.success_callaback", "PROFILE.failure_callaback");
	}
	catch(e)
	{	
		alert("Error Invoking Native code!");
	}
	console.log("INSASSIST", "Response From Wrapper:" + responseWrapper);
	return false;
};
PROFILE.success_callaback = function(response)
{
	$("#lblNoOfPhotos").text(response);
};
PROFILE.failure_callaback = function(e)
{
	alert("failure");
};

$(document).ready(function()
{
	PROFILE.activeDP = "";
	
	//Read Screen width and height and set to Main container
	 var viewport = {
				width: jQuery(window).width(),
				height: jQuery(window).height()
			};
	$(".mainContainer").height((viewport.height-10) + "px");
	//$(".mainContainer").width((viewport.width) + "px");
	
	COMMON.CurrentPage = COMMON.SCREENS.MyProfile;
	
	$(".header_left").on("click", PROFILE.btnHome_click);
	
	 function close_accordion_section() {
	        $('.accordion .right').removeClass('active').removeClass("expand");
	        $('.accordion .accordion-section-content').slideUp(1).removeClass('open');
	        
	        //Save data to Device storage
	        PROFILE.saveProfileData();
	    }
	 
	 $('.right').on("touchstart", function(e) {
		 	if(e) e.preventDefault();
		 	
	        // Grab current anchor value
	        var currentAttrValue = $(this).attr('id');
	 
	        if($(e.target).is('.active')) {
	            close_accordion_section();
	        }else {
	            close_accordion_section();
	 
	            // Add active class to section title
	            $(this).addClass('active').addClass("expand");
	            // Open up the hidden content panel
	            $('.accordion ' + currentAttrValue).slideDown(1).addClass('open'); 
	        }
	        
	    });
	
	 $("#viewProfilePhoto").on("click", PROFILE.viewProfilePhoto_click);
	 $("#takeProfilePhoto").on("click", PROFILE.takeProfilePhoto_click);
	 $("#per_txtExpiryDate").on("click",PROFILE.selectDate_click);
	 
	 $("#per_txtExpiryDate1").on("click",PROFILE.selectDate_click);
	 
	 $("#per_txtDatePurchased").on("click",PROFILE.selectDate_click);
	 $("#per_txtDOB").on("click",PROFILE.selectDate_click);
	 //$("#per_txtLastServiceDate").on("click",PROFILE.selectDate_click);
	 $("#per_txtYearOfPrevClaim").on("click",PROFILE.selectDate_click);
	 
	 $("#divIsPrevClaim").hide();
	 $("#per_selectIsPrevClaims").change(function(){
		 if($("#per_selectIsPrevClaims option:selected").text().toUpperCase()=="YES")
		 {
			 $("#divIsPrevClaim").show();
		 }
		 else
		 {
			 $('#per_selectPrevClaims option').map(function() {   //New
				    if ($(this).text() == "Select") return this;
				}).prop('selected', 'selected'); 
				$('#per_selectPrevClaimType option').map(function() {   //New
				    if ($(this).text() == "Select") return this;
				}).prop('selected', 'selected');
				$("#per_txtYearOfPrevClaim").val('');
			 $("#divIsPrevClaim").hide();
		 }
	 });
	
	 PROFILE.fetchProfileData();	 
});
PROFILE.activeDP = "";
PROFILE.selectDate_click = function()
{
	PROFILE.activeDP = "";
	var title = $(this).attr("name");
	//$(this).val("2015/03/10");
	PROFILE.activeDP = $(this).attr("id");
	var currDate= new Date();
	var day =  currDate.getDate();
	var month =  currDate.getMonth()+1;
	var year =  currDate.getFullYear()+1;
	
	var existingVal = $(this).val();
	var defaultDate = [year,month,day];	
	
	if(existingVal.length>0)
	{
		var defaultDate = existingVal.split("/");
	}
	//alert(defaultDate);
	
	var requestdata='{"Request":{"Title":"' + title + '", "DefaultDay":"' + defaultDate[2] + 
	'","DefaultMonth":"' + defaultDate[1] + '", "DefaultYear":"' + defaultDate[0] + '"}}'; 
	
	var retVal = Wrapper.pickDate(requestdata, "PROFILE.success_dp", "PROFILE.failure_dp");
};

PROFILE.success_dp = function(selectedDate)
{
	//alert(selectedDate)
	$("#" + PROFILE.activeDP).val(selectedDate);
};
PROFILE.failure_dp = function(selectedDate)
{
	alert(selectedDate);
};

//Used to save profile data to device storage
PROFILE.saveProfileData = function()
{
	//Sample Data: {"FirstName":"Albert","LastName":"Joe","DOB":"15/03/2015","Address":
	//"2rd Street","LiceneNo":"F123AST","LicenceState":"NSW","LicenceExpiry":"01/01/2020",
	//"PropertyType":"Car","PropRegNo":"1-AFASE123","PropKms":"100000","InsPersonalNo":"IN12ASD",
	//"InsPropertyNo":"INS12332","InsName":"AGG"}
	
	//Default data
	//Read data from html controls
	var profileData = {};
	profileData.FirstName = $("#per_txtFirstName").val();
	profileData.LastName = $("#per_txtLastName").val();
	profileData.DOB = $("#per_txtDOB").val();
	profileData.Address = $("#per_txtAddress").val();
	profileData.Address2 = $("#per_txtAddress2").val();
	profileData.AddrSuburb = $("#per_txtAddrSuburb").val(); //New
	profileData.AddrCity = $("#per_txtAddrCity").val(); //New
	profileData.AddrCountry = $("#per_txtAddrCountry").val(); //New
	profileData.AddrPostalCode = $("#per_txtAddrPostCode").val(); //New
	profileData.Gender = $("#per_selectGender option:selected").text(); //New (29th Mar, 2015)
	profileData.ContactNo = $("#per_txtContactNo").val(); //New (29th Mar, 2015)
	profileData.ContactEmail = $("#per_txtContactEmail").val(); //New (29th Mar, 2015)

	profileData.LiceneNo = $("#per_txtLicenceNo").val();
	profileData.LiceneNo2 = $("#per_txtLicenceNo2").val(); //New
	//profileData.LicenceState = $("#per_txtState").val();
	profileData.LicenceCountry = $("#per_txtCountry").val(); //New
	profileData.LicenceExpiry = $("#per_txtExpiryDate").val();
	profileData.LicenceExpiry1 = $("#per_txtExpiryDate1").val();
	profileData.LicenceType = $("#per_selectLicenceType option:selected").text(); //New (29th Mar, 2015)
	profileData.LicenceType2 = $("#per_selectLicenceType2 option:selected").text(); //New (29th Mar, 2015)

	
	profileData.VehModel = $("#per_txtVehModel").val(); //New
	profileData.PropertyType = $("#per_selectPropertyType option:selected").text();
	profileData.PropRegNo = $("#per_txtRegNo").val();
	profileData.PropKms = $("#per_txtNoOfKms").val();
	profileData.DatePurchased = $("#per_txtDatePurchased").val();
	profileData.GPSNumber = $("#per_txtGPSNumber").val();
	//profileData.InsPropertyNo = $("#per_txtPropRegNo").val();
	//profileData.VehicleMake = $("#per_txtVehMake").val(); //New (29th Mar, 2015)
	
	
	profileData.InsPersonalNo = $("#per_txtPersonalRegNo").val();
	//profileData.PropRegNo = $("#per_txtPropInsNo").val(); //New 
	profileData.InsName = $("#per_PropInsName").val();
	
	profileData.IsPreviewClaims = $("#per_selectIsPrevClaims option:selected").text(); //New
	if(profileData.IsPreviewClaims.toUpperCase()=="NO")
	{
		profileData.PreviewClaims = ''; //New
		profileData.ClaimType = ''; //New
		profileData.YearOfPrevClaim = '';  //New
	}
	else
	{
		profileData.PreviewClaims = $("#per_selectPrevClaims option:selected").text(); //New
		profileData.ClaimType = $("#per_selectPrevClaimType option:selected").text(); //New
		profileData.YearOfPrevClaim = $("#per_txtYearOfPrevClaim").val();  //New
	}
	
	profileData.IsAlarmFitted = $("#per_selectVehAlarmFitted option:selected").text(); //New
	profileData.IsGPSFitted = $("#per_selectVehGPSFitted option:selected").text(); //New
	profileData.IsHouseAlarmFitted = $("#per_selectHouseAlarmFitted option:selected").text(); //New
	//profileData.LastServiceDate = $("#per_txtLastServiceDate").val();  //New
	
	
	profileData.InsuranceType = $("#per_selectInsuranceType option:selected").text(); //New
	
	var str_profiledata = JSON.stringify(profileData);
	
	console.log(str_profiledata);
	
	//call wrapper for update profile
	var responseFromWrapper = Wrapper.saveProfileData(str_profiledata, 
			"PROFILE.save_profile_success_callaback", "PROFILE.save_profile_failure_callaback");
	console.log("Save Status:" + responseFromWrapper);
};
PROFILE.fetchProfileData = function()
{
	//call wrapper to fetch profile data
	try
	{
		var profileData = Wrapper.fetchProfileData("PROFILE.fetch_profile_success_callaback", "PROFILE.fetch_profile_failure_callaback");
		console.log(profileData);
		
		var profileData = JSON.parse(profileData);
		$("#per_txtFirstName").val(profileData.FirstName);
		$("#per_txtLastName").val(profileData.LastName);
		$("#per_txtDOB").val(profileData.DOB);
		$("#per_txtAddress").val(profileData.Address);
		$("#per_txtAddress2").val(profileData.Address2);
		$("#per_txtAddrSuburb").val(profileData.AddrSuburb); //New
		$("#per_txtAddrCity").val(profileData.AddrCity); //New
		$("#per_txtAddrCountry").val(profileData.AddrCountry); //New
		$("#per_txtAddrPostCode").val(profileData.AddrPostalCode); //New
		//New (29th Mar, 2015)
		$('#per_selectGender option').map(function() { 
		    if ($(this).text() == profileData.Gender) return this;
		}).prop('selected', 'selected');
		$("#per_txtContactNo").val(profileData.ContactNo); //New (29th Mar, 2015)
		$("#per_txtContactEmail").val(profileData.ContactEmail); //New (29th Mar, 2015)
		
		$("#per_txtLicenceNo").val(profileData.LiceneNo);
		$("#per_txtLicenceNo2").val(profileData.LiceneNo2); //New
		//$("#per_txtState").val(profileData.LicenceState);
		$("#per_txtExpiryDate").val(profileData.LicenceExpiry);
		$("#per_txtExpiryDate1").val(profileData.LicenceExpiry1);
		$("#per_txtCountry").val(profileData.LicenceCountry); //New 
		$('#per_selectLicenceType option').map(function() { //New (29th Mar, 2015)
		    if ($(this).text() == profileData.LicenceType) return this;
		}).prop('selected', 'selected');
		$('#per_selectLicenceType2 option').map(function() { //New (29th Mar, 2015)
		    if ($(this).text() == profileData.LicenceType2) return this;
		}).prop('selected', 'selected');

		$("#per_txtVehModel").val(profileData.VehModel); //New
		$('#per_selectPropertyType option').map(function() {
		    if ($(this).text() == profileData.PropertyType) return this;
		}).prop('selected', 'selected');
		
		$('#per_selectIsPrevClaims option').map(function() {   //New
		    if ($(this).text() == profileData.IsPreviewClaims) return this;
		}).prop('selected', 'selected'); 
		
		if($("#per_selectIsPrevClaims option:selected").text().toUpperCase()=="YES")
		 {
			 $("#divIsPrevClaim").show();
		 }
		
		$('#per_selectPrevClaims option').map(function() {   //New
		    if ($(this).text() == profileData.PreviewClaims) return this;
		}).prop('selected', 'selected'); 
		$('#per_selectPrevClaimType option').map(function() {   //New
		    if ($(this).text() == profileData.ClaimType) return this;
		}).prop('selected', 'selected');
		$("#per_txtYearOfPrevClaim").val(profileData.YearOfPrevClaim);
		
		
		$('#per_selectVehAlarmFitted option').map(function() {   //New
		    if ($(this).text() == profileData.IsAlarmFitted) return this;
		}).prop('selected', 'selected');
		$('#per_selectVehGPSFitted option').map(function() {   //New
		    if ($(this).text() == profileData.IsGPSFitted) return this;
		}).prop('selected', 'selected');
		$('#per_selectHouseAlarmFitted option').map(function() {   //New
		    if ($(this).text() == profileData.IsHouseAlarmFitted) return this;
		}).prop('selected', 'selected');
		//$("#per_txtLastServiceDate").val(profileData.LastServiceDate);  //New
		
		$('#per_selectInsuranceType option').map(function() {   //New
		    if ($(this).text() == profileData.InsuranceType) return this;
		}).prop('selected', 'selected');
		
		
		$("#per_txtRegNo").val(profileData.PropRegNo);
		$("#per_txtNoOfKms").val(profileData.PropKms);
		$("#per_txtDatePurchased").val(profileData.DatePurchased);
		$("#per_txtGPSNumber").val(profileData.GPSNumber);
		//$("#per_txtPropRegNo").val(profileData.InsPropertyNo);
		//$("#per_txtVehMake").val(profileData.VehicleMake); //New (29th Mar, 2015)
		
		$("#per_txtPersonalRegNo").val(profileData.InsPersonalNo);
		//$("#per_selectPropertyType").val(profileData.PropertyType);
		//$("#per_txtPropInsNo").val(profileData.PropRegNo); //New
		$("#per_PropInsName").val(profileData.InsName);
		
	
		//$("div[name='first']").trigger("touchstart");		
		$("#lblNoOfPhotos").text(profileData.PhotoCount);
		
		//{"FirstName":"Albert","LastName":"Joe","DOB":"15/03/2015","Address":
			//"2rd Street","LiceneNo":"F123AST","LicenceState":"NSW","LicenceExpiry":"01/01/2020",
			//"PropertyType":"Car","PropRegNo":"1-AFASE123","PropKms":"100000","InsPersonalNo":"IN12ASD",
			//"InsPropertyNo":"INS12332","InsName":"AGG"}
	}
	catch(e)
	{
		COMMON.showMessage("Unable to fetch profile data.");
	}
};

PROFILE.fetch_profile_success_callaback = function(response)
{
	alert(response);
	//Validate profile response data
	//populate values in html controls
};
PROFILE.fetch_profile_failure_callaback = function(response)
{
	alert("Failed fetching user profile data.");
};
PROFILE.save_profile_success_callaback = function(response)
{
	alert(response);
};
PROFILE.save_profile_failure_callaback = function(response)
{
	alert("Profile data update failed.");
};

