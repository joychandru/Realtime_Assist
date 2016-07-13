package com.insurance.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Wrapper {
	public static Activity myActivity;
	public static WebView appWebView;
	public String CB_Success = "success_callback";
	public String CB_failure = "failure_callback";
	public static String APP_FOLDERNAME = "InsuranceAssist";
	private boolean isRecording = false;
	public static int REQUEST_CODE_TAKE_PHTO = 200;
	private static final String TAG = "GenerateReport";
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
			Font.BOLD);
	private static Font headFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
			Font.BOLD);
	private static Font paraHeadFont = new Font(Font.FontFamily.TIMES_ROMAN,
			16, Font.BOLD);
	private static Font paraTextFont = new Font(Font.FontFamily.TIMES_ROMAN,
			16, Font.NORMAL);
	private static Font imageNameFont = new Font(Font.FontFamily.TIMES_ROMAN,
			11, Font.ITALIC);

	/**
	 * Constructor
	 * 
	 * @param gap
	 * @param view
	 */
	public Wrapper(Activity gap, WebView view) {
		this.myActivity = gap;
		this.appWebView = view;
		
		APP_FOLDERNAME = Utility.getApplicationFolder(myActivity);
	}

	public Wrapper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Used to naviagate page to home page
	 * 
	 * @param requestData
	 * @param successCallbackMethodName
	 * @param failureCallbackMethodName
	 * @return
	 */
	public int gotoHome(String requestData, String successCallbackMethodName,
			String failureCallbackMethodName) {
		invokeURL("javascript:COMMON.success_callback('Test')");
		return 1;
	}

	public String fetchProfileData(String successCB, String failureCB) {
		Log.e("IAWRAPPER", "Reached fetchProfileData Wrapper method");
		String retVal = Constants.TEMPLATE_PROFILE;
		try 
		{
			String path = APP_FOLDERNAME + File.separator
					+ Constants.STR_PROFILE_FOLDER + File.separator
					+ Constants.STR_PROFILE_FILE;
			File profileDir = new File(path);
			Log.e("Path", "Path" + profileDir.getAbsolutePath() + ", isExists:"
					+ profileDir.exists());
			if (profileDir.isFile() && profileDir.exists()) {
				FileReader reader = new FileReader(profileDir.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line).append("\n");
				}
				bufferedReader.close();
				reader.close();
				retVal = stringBuffer.toString();
				JSONObject obj = new JSONObject(retVal);

				// Fetch Profile photos count
				path = APP_FOLDERNAME + File.separator
						+ Constants.STR_PROFILE_FOLDER + File.separator
						+ "MyPhotos" + File.separator;
				profileDir = new File(path);

				if (profileDir.exists() && profileDir.isDirectory()) {
					obj.put("PhotoCount", profileDir.list().length);
				}
				retVal = obj.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			retVal = Constants.TEMPLATE_PROFILE;
		}
		return retVal;
	}

	public String saveProfileData(String profileData, String successCB,
			String failureCB) {
		Log.e("IAWRAPPER", "Reached fetchProfileData Wrapper method");
		String retVal = "Done";
		try {
			// Create Parent folder if not exists
			String path = APP_FOLDERNAME + File.separator
					+ Constants.STR_PROFILE_FOLDER + File.separator;
			File profileDir = new File(path);
			if (!profileDir.exists())
				profileDir.mkdirs();

			MediaRefresh(profileDir);

			path = APP_FOLDERNAME + File.separator
					+ Constants.STR_PROFILE_FOLDER + File.separator
					+ Constants.STR_PROFILE_FILE;
			profileDir = new File(path);

			Log.e("Path", "Path" + profileDir.getAbsolutePath() + ", isExists:"
					+ profileDir.exists());

			if (!profileDir.exists())
				profileDir.createNewFile();

			if (profileDir.isFile() && profileDir.exists()) {
				profileDir.delete();
				FileWriter fileWritter = new FileWriter(profileDir, false);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(profileData);
				bufferWritter.close();
				fileWritter.close();
				retVal = "Done";
			}
			MediaRefresh(profileDir);
		} catch (Exception e) {
			retVal = "ERROR";
		}
		return retVal;
	}

	public void showProgress() {
		// Show Activity Indicator
		Handler handler = new Handler();
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Utility.showActivityIndicator(myActivity, "Loading Photos..",
						null);
			}
		});

	}

	public void hideProgress() {
		// Hide activity Indicator
		Utility.hideActivityIndicator();
	}

	JSONArray incidentArrayMain = new JSONArray();

	public String fetchPhotos(String pIncidentId, String pCategory,
			String pPrefix, String successCB, String failureCB) {
		String retVal = "";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;

		final String IncidentId = pIncidentId;
		final String Category = pCategory;
		final String prefix = pPrefix;

		// Retrieve all photos for this parameter and returns JSON string
		/*
		 * String retVal =
		 * "[{'filename':'001.jpg','data':'base64string here','createddate':'10 Mar,2015,11:00AM'},"
		 * +
		 * "{'filename':'001.jpg','data':'base64string here','createddate':'10 Mar,2015,11:00AM'},"
		 * +
		 * "{'filename':'001.jpg','data':'base64string here','createddate':'10 Mar,2015,11:00AM'},]"
		 * ;
		 */

		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Utility.showActivityIndicator(myActivity, "Loading Photos..",
						null);
			}

			@Override
			protected Integer doInBackground(Void... strings) {
				int success = 1;
				try {
					JSONArray incidentArray = new JSONArray();

					String path = APP_FOLDERNAME + File.separator + IncidentId
							+ File.separator + Category + File.separator;
					File mediaStorageDir = new File(path);

					List<File> files = Utility.getFiles(mediaStorageDir
							.getPath());
					JSONObject incidentObject = new JSONObject();

					if (files != null && files.size() > 0) {
						System.out.println(files.size());
						for (File file : files) {
							incidentObject = new JSONObject();
							// String
							// encodedImage=Utility.convertImageToBase64(file);
							// //Fix for gallery load issue

							String dateString = Utility.getImageDate(file);
							Log.e("dateString", file.getName());

							incidentObject.put("FileName", file.getName());
							incidentObject.put("CreatedDate", dateString);
							// incidentObject.put("ImageData",
							// encodedImage.replaceAll("(\\r|\\n|\\t)", ""));
							incidentObject.put("ImageData",
									file.getAbsolutePath());
							incidentArray.put(incidentObject);
						}
					}
					incidentArrayMain = incidentArray;
					return success;
				} catch (IOException e) {
					return 0;
				} catch (JSONException e) {
					return 0;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				invokeSuccessCallback(incidentArrayMain.toString());

				// After Async close progress dialog
				Utility.hideActivityIndicator();

			}
		}.execute();
		return "Done";
	}

	public String takePhotos(String IncidentId, String Category, String prefix,
			String successCB, String failureCB) {
		String retVal = "";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;

		Log.d("IncidentId", IncidentId);
		Log.d("Category", Category);
		Log.d("Prefix", prefix);

		Intent cameraIntent = new Intent(this.myActivity, CameraActivity.class);
		cameraIntent.putExtra("IncidentId", IncidentId);
		cameraIntent.putExtra("Category", Category);
		cameraIntent.putExtra("Prefix", prefix);
		myActivity.startActivityForResult(cameraIntent,
				Constants.REQUEST_CODE_TAKE_PHOTO);

		Log.e("Wrapper", "Category:" + Category + ", CB:" + successCB);
		return retVal;
	}

	public String deletePhoto(String IncidentId, String Category,
			String fileName) {
		Log.e("IAWRAPPER", "Reached fetchProfileData Wrapper method");
		try {
			String path = APP_FOLDERNAME + File.separator + IncidentId
					+ File.separator + Category + File.separator + fileName;
			File photoFileDir = new File(path);
			Log.e("Path", "Path" + photoFileDir.getAbsolutePath()
					+ ", isExists:" + photoFileDir.exists());
			if (photoFileDir.isFile() && photoFileDir.exists()) {
				photoFileDir.delete();
				return "Done";
			} else {
				return "No File exists!";
			}
		} catch (Exception e) {
			return "Unable to delete the file.!";
		}
	}

	public String fetchHistory(String successCB, String failureCB) {
		String retVal = "";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;

		// Retrieve all photos for this parameter and returns JSON string
		/*
		 * String retVal =
		 * "{"IncidentID":"","CreatedDate":"","IncidentType":"","
		 * IncidentCategory":"",
		 * "PoliceReferenceNo":"","IsEmailed":"false","IsUploadedToDropBox"
		 * :"false","IsUploadedToFTP":"false",
		 * "GPSLatitude":"","GPSLongitude":""
		 * ,"IsVoiceRecorded":"false","ModifiedDate":""}";
		 */

		try {

			JSONArray incidentArray = new JSONArray();

			String path = APP_FOLDERNAME + File.separator;
			File mediaStorageDir = new File(path);

			List<File> folders = Utility.getIncidentFolders(mediaStorageDir
					.getPath());
			JSONObject incidentObject = new JSONObject();
			Log.e("IAWRAPPER", "Size:" + folders.size());
			if (folders != null && folders.size() > 0) {
				for (File folder : folders) {
					File dataFile = new File(folder.getAbsoluteFile()
							+ File.separator + "data.json");
					if (dataFile.exists()) {
						FileReader reader = new FileReader(
								dataFile.getAbsoluteFile());
						BufferedReader bufferedReader = new BufferedReader(
								reader);
						StringBuffer stringBuffer = new StringBuffer();
						String line = null;
						while ((line = bufferedReader.readLine()) != null) {
							stringBuffer.append(line).append("\n");
						}
						bufferedReader.close();
						reader.close();
						retVal = stringBuffer.toString();
						incidentObject = new JSONObject(retVal);

						// Log.e("IAWRAPPER", "Data:"+
						// incidentObject.toString());
					}
					incidentArray.put(incidentObject);
				}
			}
			Log.e("IAWRAPPER", "Data:"+ incidentArray.toString());
			invokeSuccessCallback(incidentArray.toString());

			retVal = "Done";
		} catch (Exception e) {

		}
		return retVal;
	}

	// ********************INCIDENT Details ************************
	public String fetchIncident(String incidentID) {
		Log.e("IAWRAPPER", "Reached fetchProfileData Wrapper method");
		String retVal = "{\"IsUploadedToFTP\":\"false\",\"IsUploadedToDropBox\":\"false\","
				+ "\"GPSLongitude\":\"\",\"IncidentType\":\"Select\",\"IsVoiceRecorded\":\"false\",\"IsEmailed\":\"false\", "
				+ "\"NoOfInjuryPhotos\":0,\"ModifiedDate\":\"\",\"IncidentID\":\"\",\"IncidentCategory\":\"\","
				+

				"\"CrashLocation\":\"\",\"CrashType\":\"\",\"IsPoliceInspected\":\"\",\"NoOfInjured\":\"\","
				+ "\"EnvWeather\":\"\",\"EnvRoadType\":\"\",\"EnvRoadSurface\":\"\",\"EnvSpeed\":\"\","
				
				+ "\"OtherDetails\":\"\",\"IsHouseAlarmFitted\":\"\",\"OtherDamageDetails\":\"\","
				+ "\"PoliceContactNo\":\"\",\"Visibility\":\"\",\"WeatherTime\":\"\",\"WeatherDate\":\"\","
				
				+ "\"ServiceType\":\"\","
				
				
				+ "\"IsThirdPartyDamage\":\"\",\"IsPoliceContacted\":\"\",\"OtherDamage\":\"\",\"OtherAccidentDet\":\"\"," //New Field 29th Mar, 2015
				+ "\"WitnessIsAny\":\"\",\"WitnessName\":\"\",\"WitnessPhoneNo\":\"\","
				+

				"\"CreatedDate\":\"\",\"PoliceReferenceNo\":\"\",\"NoOfOtherPhotos\":0,\"GPSLatitude\":\"\"}";
		try {
			String path = APP_FOLDERNAME + File.separator + incidentID
					+ File.separator + "data.json";
			File incidentDir = new File(path);
			Log.e("Path", "Path" + incidentDir.getAbsolutePath()
					+ ", isExists:" + incidentDir.exists());
			if (incidentDir.isFile() && incidentDir.exists()) {
				FileReader reader = new FileReader(
						incidentDir.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line).append("\n");
				}
				bufferedReader.close();
				reader.close();
				retVal = stringBuffer.toString();
				JSONObject obj = new JSONObject(retVal);

				// Read Number of Injury Photos
				path = APP_FOLDERNAME + File.separator + incidentID
						+ File.separator + "Injury";
				incidentDir = new File(path);
				if (incidentDir.isDirectory()) {
					obj.put("NoOfInjuryPhotos", incidentDir.list().length);
				}

				// Read Number of other photos
				path = APP_FOLDERNAME + File.separator + incidentID
						+ File.separator + "OtherPhoto";
				incidentDir = new File(path);
				if (incidentDir.isDirectory()) {
					obj.put("NoOfOtherPhotos", incidentDir.list().length);
				} else {
					obj.put("NoOfOtherPhotos", 0);
				}

				// Read Report is generated
				path = APP_FOLDERNAME + File.separator + incidentID
						+ File.separator + "Report.pdf";
				incidentDir = new File(path);
				if (incidentDir.exists()) {
					obj.put("IsReportGenerated", "true");
				} else {
					obj.put("IsReportGenerated", "false");
				}
				retVal = obj.toString();
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	public String createNewIncident(String incidentID, String createdDate,
			String incidentType, String categoryType, String RecordType) {
		String retVal = "Done";
		Log.d("createNewIncident incidentType", "" + incidentType);
		Log.d("createNewIncident Record Type", "" + RecordType);
		Log.e("IAWRAPPER", "Reached createNewIncident Wrapper method");
		if (categoryType != null && categoryType.isEmpty()) {
			categoryType = "Select";
		}
		String defaultData = "{\"IsUploadedToFTP\":\"false\",\"IsUploadedToDropBox\":\"false\","
				+

				"\"CrashLocation\":\"\",\"CrashType\":\"\",\"IsPoliceInspected\":\"\",\"NoOfInjured\":\"\","
				+ "\"EnvWeather\":\"\",\"EnvRoadType\":\"\",\"EnvRoadSurface\":\"\",\"EnvSpeed\":\"\","
				+ "\"WitnessIsAny\":\"\",\"WitnessName\":\"\",\"WitnessPhoneNo\":\"\","
				+ "\"IsThirdPartyDamage\":\"\",\"IsPoliceContacted\":\"\",\"OtherDamage\":\"\",\"OtherAccidentDet\":\"\"," //New Field 29th Mar, 2015
				+ "\"GPSLongitude\":\"\",\"IncidentType\":\""
				+ incidentType
				+ "\",\"IsVoiceRecorded\":\"false\",\"IsEmailed\":\"false\", "
				+ "\"NoOfInjuryPhotos\":0,\"ModifiedDate\":\"\",\"IncidentID\":\""
				+ incidentID
				+ "\",\"IncidentCategory\":\""
				+ categoryType
				+ "\","
				+ "\"CreatedDate\":\""
				+ createdDate
				+ "\",\"PoliceReferenceNo\":\"\",\"NoOfOtherPhotos\":0,\"GPSLatitude\":\"\"}";
		try {
			// Create Incident Folder
			String path = APP_FOLDERNAME + File.separator + incidentID
					+ File.separator;
			File incidentDir = new File(path);
			if (incidentDir.exists())
				incidentDir.delete();
			incidentDir.mkdirs();
			MediaRefresh(incidentDir);
			JSONObject includeLocation = new JSONObject(defaultData);

			// If New Incident, find the GPS location..If Edit, dont find.
			if (!RecordType.equals("EDIT")) 
			{
				Log.e("GPS Location:", "insideFEtch Location:" + RecordType);
				GetLocation objGetLocation = new GetLocation(this.myActivity);
				if (objGetLocation.displayGpsStatus()) {
					Location location = objGetLocation.getLocationData();
					if (location != null) {
						includeLocation.put("GPSLatitude",
								location.getLatitude());
						includeLocation.put("GPSLongitude",
								location.getLongitude());
						defaultData = includeLocation.toString();
						retVal = "" + location.getLatitude() + ","
								+ location.getLongitude();
						Log.d("Latitude", "" + location.getLatitude());
						Log.d("Longitude", "" + location.getLongitude());
					}

				} else {
					retVal = "NO";
				}

			}

			// Create data file inside Incident folder
			path = APP_FOLDERNAME + File.separator + incidentID
					+ File.separator + "data.json";
			File incidentDataFile = new File(path);
			if (incidentDataFile.isAbsolute() && incidentDataFile.exists())
				incidentDataFile.delete();
			incidentDataFile.createNewFile();

			// Write default JSON to incident data file
			FileWriter fileWritter = new FileWriter(incidentDataFile, false);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(defaultData);
			bufferWritter.close();
			fileWritter.close();

			MediaRefresh(incidentDataFile);

		} catch (Exception e) {
			retVal = "ERROR";
		}
		return retVal;
	}

	public String saveIncidentData(String incidentID, String incidentData,
			String successCB, String failureCB) {
		Log.e("IAWRAPPER", "Reached fetchProfileData Wrapper method");
		String retVal = "Done";
		try {
			String path = APP_FOLDERNAME + File.separator + incidentID
					+ File.separator + "data.json";
			File profileDir = new File(path);

			Log.e("Path", "Path" + profileDir.getAbsolutePath() + ", isExists:"
					+ profileDir.exists());

			if (profileDir.isFile() && profileDir.exists()) {
				profileDir.delete();
				FileWriter fileWritter = new FileWriter(profileDir, false);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(incidentData);
				bufferWritter.close();
				fileWritter.close();
				retVal = "Done";
			}

			MediaRefresh(profileDir);
		} catch (Exception e) {
			retVal = "ERROR";
		}
		return retVal;
	}

	public String deleteIncident(String incidentID)
	{
		String retVal = "DONE";
		try
		{
			String path = APP_FOLDERNAME + File.separator + incidentID;
			File incidentDir = new File(path);
			if (incidentDir.isDirectory() && incidentDir.exists()) 
			{
				deleteDirectory(incidentDir);
				Log.e("WRAPPER", "IsDeleted:done," + incidentDir.getAbsolutePath());
				retVal = "DONE";
			}
			return retVal;
		}
		catch(Exception e)
		{		
			retVal ="ERROR";
		}
		return retVal;
	}
	
	static public boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return(path.delete());
	  } 
	
	/****
	 * This method helps to send success response to Siebel Parameters
	 ***/
	protected void invokeSuccessCallback(Object responseData) {
		try {
			invokeURL("javascript:" + this.CB_Success + "('" + responseData
					+ "')");
		} catch (Exception e) {
			// write error log to file.
		} finally {
		}
	}

	/****
	 * This method helps to send failure response to Siebel Parameters
	 ***/
	protected void invokeFailureCallback(String errorMessage) {
		try {
			invokeURL("javascript:" + this.CB_failure + "('" + errorMessage
					+ "')");
		} catch (Exception e) {

		} finally {
		}
	}

	/**
	 * Used to unvoke the instant WebURL to send detials from Wrapper to Siebel.
	 */
	public static void invokeURL(String webUrl) {
		final String webResponseURL = webUrl;
		Log.e("WRAPPER RES", "URL:"+webUrl);
		myActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Log.e("IAWRapper", "INV_URL:" + webResponseURL);
				appWebView.loadUrl(webResponseURL);
			}
		});
	}

	public void ExitApplication() {
		this.myActivity.finish();
	}

	public String generateUniqueID() {
		String uniqueID = "";
		// Convert DateString in Specific format Here
		SimpleDateFormat formatDate = new SimpleDateFormat(
				Constants.TEMPLATE_UNIQUE_ID);
		Date currentDate = new Date();
		uniqueID = formatDate.format(currentDate);
		return uniqueID;
	}

	public String currentDate() {
		String currentDateStr = "";
		// Convert DateString in Specific format Here
		SimpleDateFormat formatDate = new SimpleDateFormat(
				Constants.TEMPLATE_DATE_FORMAT_TO);
		Date currentDate = new Date();
		currentDateStr = formatDate.format(currentDate);
		return currentDateStr;
	}

	public void doPreRequites() {
		try {
			
			String appFolder = Utility.getApplicationFolder(myActivity);
			
			// Create Incident Application Folder
			File profileDir = new File(appFolder);
			if (!profileDir.exists())
				profileDir.mkdirs();
			MediaRefresh(profileDir);

			// Create profile folder if not exists
			String path = appFolder + File.separator
					+ Constants.STR_PROFILE_FOLDER + File.separator;
			profileDir = new File(path);
			if (!profileDir.exists())
				profileDir.mkdirs();
			MediaRefresh(profileDir);

			// validate and create default emergency list
			path = appFolder + File.separator
					+ Constants.STR_EMERGENCY_LISTFILE;
			profileDir = new File(path);
			if (!profileDir.exists()) {
				FileWriter fileWritter = new FileWriter(profileDir, false);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(Constants.TEMPLATE_EMERGENCY_LIST);
				bufferWritter.close();
				fileWritter.close();

				MediaRefresh(profileDir);
			}

			// Move template file from Assets to Main folder
			InputStream in = myActivity.getResources().openRawResource(
					R.raw.template);

			path = appFolder + File.separator + "template.pdf";
			profileDir = new File(path);
			if (profileDir.exists())
				profileDir.delete();

			OutputStream out = null;
			out = new FileOutputStream(profileDir.getAbsoluteFile());

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}

			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;

			MediaRefresh(profileDir);
		} catch (Exception e) {
		}
	}

	// *****************Emergency List screen ******************/
	public String fetchEmergencyList() {
		String retVal = "{}";
		try {
			String path = APP_FOLDERNAME + File.separator
					+ Constants.STR_EMERGENCY_LISTFILE;
			File phonelistFile = new File(path);
			Log.e("Path", "Path" + phonelistFile.getAbsolutePath()
					+ ", isExists:" + phonelistFile.exists());
			if (phonelistFile.isFile() && phonelistFile.exists()) {
				FileReader reader = new FileReader(
						phonelistFile.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
				}
				bufferedReader.close();
				reader.close();
				retVal = stringBuffer.toString();
				JSONObject obj = new JSONObject(retVal);

				retVal = obj.toString();
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	public String fetchEmergencyListItem(String key) {
		String retVal = "{}";
		try {
			String path = APP_FOLDERNAME + File.separator
					+ Constants.STR_EMERGENCY_LISTFILE;
			File phonelistFile = new File(path);
			Log.e("Path", "Path" + phonelistFile.getAbsolutePath()
					+ ", isExists:" + phonelistFile.exists());
			if (phonelistFile.isFile() && phonelistFile.exists()) {
				FileReader reader = new FileReader(
						phonelistFile.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
				}
				bufferedReader.close();
				reader.close();
				retVal = stringBuffer.toString();
				JSONObject obj = new JSONObject(retVal);

				if (obj.has(key)) {
					retVal = obj.getJSONObject(key).toString();
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	public String updateEmergencyContact(String type, String name,
			String contactNumber, String isFavourite, boolean isDelete, String disText, String contEmail) {
		String retVal = "Done";
		try {
			String path = APP_FOLDERNAME + File.separator
					+ Constants.STR_EMERGENCY_LISTFILE;
			File phonelistFile = new File(path);
			Log.e("Path", "Path" + phonelistFile.getAbsolutePath()
					+ ", isExists:" + phonelistFile.exists());
			if (phonelistFile.isFile() && phonelistFile.exists()) {
				FileReader reader = new FileReader(
						phonelistFile.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
				}
				bufferedReader.close();
				reader.close();
				retVal = stringBuffer.toString();
				JSONObject obj = new JSONObject(retVal);

				if (!isDelete) {
					JSONObject newContact = new JSONObject();
					newContact.put("Number", contactNumber);
					newContact.put("Name", name);
					newContact.put("IsFavourite", isFavourite);
					newContact.put("ID", disText);
					newContact.put("Email", contEmail);
					
					if (obj.has(type)) {
						obj.remove(type);
					}
					obj.put(type, newContact);
				} else {
					obj.remove(type);
				}
				String srcData = obj.toString();

				FileWriter fileWritter = new FileWriter(phonelistFile, false);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(srcData);
				bufferWritter.close();
				fileWritter.close();
			}
		} catch (Exception e) {
			retVal = "ERROR";
		}
		return retVal;
	}

	private void MediaRefresh(File file) {
		MediaScannerConnection.scanFile(myActivity,
				new String[] { file.getPath() }, null, null);
	}

	/******************** Generate Report ***********************/
	public String ShowReport(String incidentID) {
		String retVal = "Done";

		String pathDest = Constants.FILE_PROVIDER_PATH + File.separator + incidentID
				+ File.separator + "Report.pdf";
		
		String phyPath = APP_FOLDERNAME + File.separator + incidentID
				+ File.separator + "Report.pdf";
		
		Log.e("WRAPPER", "Status:" + Constants.IsDebug);
		Log.e("WRAPPER", "APP Path:" + APP_FOLDERNAME);
		
		File reportPDFFile = new File(phyPath);
		if (reportPDFFile.exists()) 
		{
			Intent viewIntent = new Intent(Intent.ACTION_VIEW);
			if(Constants.IsDebug)
			{
				Log.e("WRAPPER", "CONTENT PATH:" + pathDest);
				viewIntent.setDataAndType(Uri.fromFile(reportPDFFile),"application/pdf");
			}
			else
			{
				Log.e("WRAPPER", "APP PATH:" + phyPath);
				viewIntent.setDataAndType(Uri.parse(pathDest),"application/pdf");
			}
			viewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			//myActivity.startActivity(viewIntent);
			
			myActivity.startActivity((Intent.createChooser(viewIntent, "View Report..")));
		} else {
			retVal = "No Report generated.";
		}

		return retVal;
	}
	public void PreviewImage(String filePath) 
	{
		Log.e("WRAPPER", "Image Path:" + filePath);
		Intent previewImgIntent =new Intent(myActivity, ImagePreviewActivity.class);
		previewImgIntent.putExtra("imgFilePath", filePath);
		myActivity.startActivityForResult(previewImgIntent, Constants.REQUEST_CODE_PREVIEW_IMAGE);
	}
	public String EmailReport(String incidentID, String successCB,
			String failureCB) {
		String retVal = "Done";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;
		try {
			String pathDest = Constants.FILE_PROVIDER_PATH + File.separator + incidentID
					+ File.separator + "Report.pdf";
			String phyPathRpt = APP_FOLDERNAME+ File.separator + incidentID
					+ File.separator + "Report.pdf";
			File reportPDFFile = new File(phyPathRpt);
			
			
			String audioPath = Constants.FILE_PROVIDER_PATH + File.separator + incidentID
					+ File.separator + incidentID + ".3gp";
			String phyPathAudio = APP_FOLDERNAME + File.separator + incidentID
					+ File.separator + incidentID + ".3gp";
			File audioFile = new File(phyPathAudio);
			
			

			ArrayList<Uri> uris = new ArrayList<Uri>();
			// convert from paths to Android friendly Parcelable Uri's
			String[] filePaths = new String[] { reportPDFFile.getAbsolutePath(), audioFile.getAbsolutePath()};
			if(!Constants.IsDebug)
			{
				filePaths = new String[] { pathDest, audioPath};
			}
			for (String file : filePaths) {
				File fileIn = new File(file);
				Uri u = Uri.fromFile(fileIn);
				if(!Constants.IsDebug){
					u = Uri.parse(file);
				}
				uris.add(u);
				Log.e("WRAPPER", "FILE:" + u.toString());
			}
			
			//Read Profile data and take email address for sender
			String profileDataStr = fetchProfileData("", "");
			JSONObject profileJSON = new JSONObject();
			if (profileDataStr != null && profileDataStr.length() > 0) {
				profileJSON = new JSONObject(profileDataStr);
				Log.e("JOY", "ContEmail:"+ profileJSON);
			}
			String senderEmail =Constants.INSURANCE_COMP_EMAIL;
			if(profileJSON != null && profileJSON.has("ContactEmail"))
			{
				Log.e("JOY", "ContEmail:"+ profileJSON.getString("ContactEmail"));
				senderEmail = profileJSON.getString("ContactEmail");
			}

			if (reportPDFFile.exists() && audioFile.exists()) 
			{
				Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{senderEmail});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Accident Report for Claim");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,	Constants.TEMPLATE_EMAIL_CONTENT);
				emailIntent.setType("text/plain");
				emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

				myActivity.startActivityForResult(
						(Intent.createChooser(emailIntent, "Share report...")),
						Constants.REQUEST_CODE_SEND_MAIL);
			} 
			else if (reportPDFFile.exists()) 
			{
				Uri htmlUri = Uri.fromFile(reportPDFFile);
				if(!Constants.IsDebug)
				{
					htmlUri = Uri.parse(pathDest);
				}
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{senderEmail});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Accident Report for Claim");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,	Constants.TEMPLATE_EMAIL_CONTENT);
				emailIntent.setType("application/pdf");
				emailIntent.putExtra(Intent.EXTRA_STREAM, htmlUri);
				myActivity.startActivityForResult(
						(Intent.createChooser(emailIntent, "Share report...")),
						Constants.REQUEST_CODE_SEND_MAIL);
			} else {

				retVal = "No report exists to send email!";
			}
		} catch (Exception e) {
			retVal = "Emailing report failed or No report exists!";
		}
		return retVal;
	}
	
	public String buildAddress(JSONObject profileData)
	{
		String retVal = "";
		try
		{
			//",\"AddrSuburb\":\"\",\"AddrCity\":\"\",\"AddrCountry\":\"\",\"AddrPostalCode\":\"\"" +
			if(profileData.has("Address"))
			{
				if(retVal.length()>0) retVal = retVal+",";
				retVal = retVal+profileData.getString("Address");
			}
			if(profileData.has("AddrSuburb"))
			{
				if(retVal.length()>0) retVal = retVal+",";
				retVal = retVal+profileData.getString("AddrSuburb");
			}
			if(profileData.has("AddrCity"))
			{
				if(retVal.length()>0) retVal = retVal+",";
				retVal = retVal+profileData.getString("AddrCity");
			}
			if(profileData.has("AddrCountry"))
			{
				if(retVal.length()>0) retVal = retVal+",";
				retVal = retVal+profileData.getString("AddrCountry");
			}
			if(profileData.has("AddrPostalCode"))
			{
				if(retVal.length()>0) retVal = retVal+",";
				retVal = retVal+profileData.getString("AddrPostalCode");
			}
		}
		catch(Exception e)
		{
		}
		return retVal;
	}
	
	public String FilterData(JSONObject jsonObj, String Key)
	{
		String retVal = "";
		try
		{
			Log.e("FILTER DATA", "Key:" + Key + ", VAL:" + jsonObj.getString(Key).toUpperCase());
			Log.e("FILTER DATA", "is VALid:" + !jsonObj.getString(Key).equalsIgnoreCase("SELECT"));
			
			if(jsonObj.has(Key) && !jsonObj.getString(Key).equalsIgnoreCase("SELECT"))
			{
				retVal = jsonObj.getString(Key);
			}
		}
		catch(Exception e)
		{
			retVal = "";
		}
		return retVal;
	}

	public String GenerateReport(final String incidentID, String successCB,
			String failureCB) {
		Log.e("WRAPPER", "Request Reached Wrapper...");
		Log.e("WRAPPER", "Incident ID..." + incidentID);
		String retVal = "Done";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;
		final String incidentIDStr = incidentID;

		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Utility.showActivityIndicator(myActivity, "Creating Report..",
						null);
			}

			@Override
			protected Integer doInBackground(Void... strings) {
				int success = 1;
				try {
					PdfReader.unethicalreading = true;
					String path = APP_FOLDERNAME + File.separator
							+ "template.pdf";
					File templatePDFFile = new File(path);

					String pathDest = APP_FOLDERNAME + File.separator
							+ incidentIDStr + File.separator + "text.pdf";
					File reportPDFFile = new File(pathDest);
					if (reportPDFFile.exists())
						reportPDFFile.delete();

					PdfReader reader = new PdfReader(
							templatePDFFile.getAbsolutePath());
					FileOutputStream os = new FileOutputStream(
							reportPDFFile.getAbsolutePath(), true);
					PdfStamper stamper = new PdfStamper(reader, os);
					AcroFields form = stamper.getAcroFields();
					
					Set<String> fields = form.getFields().keySet();					
					for (String key : fields) 
					{
						Log.e("PDF Fields", "Name:" + key);
					}
					// Fetch Profile Data
					String profileDataStr = fetchProfileData("", "");
					JSONObject profileJSON = new JSONObject();
					if (profileDataStr != null && profileDataStr.length() > 0) {
						profileJSON = new JSONObject(profileDataStr);
					}
					Log.e("JSON Data", "Profile:" + profileJSON.toString());

					// Fetch Incident Data
					String incidentDataStr = fetchIncident(incidentIDStr);
					JSONObject incidentJSON = new JSONObject();
					if (incidentDataStr != null && incidentDataStr.length() > 0) {
						incidentJSON = new JSONObject(incidentDataStr);
					}
					Log.e("JSON Data", "Incident:" + incidentJSON.toString());

					//New JSON
					//******* Profile Data **********
					//{"AddrPostalCode":"","LicenceExpiry":"","DOB":"","PropRegNo":"","PropertyType":"","PropKms":"",
					//"LiceneNo":"","LastName":"","PropInsuranceNo":"","FirstName":"","LicenceCountry":"","AddrCity":"",
					//"DatePurchased":"","VehicleMake":"","InsPersonalNo":"","ContactEmail":"","GPSNumber":"","InsName":"",
					//"LicenceType":"","ContactNo":"","AddrCountry":"","AddrSuburb":"","InsPropertyNo":"","VehModel":"",
					//"Gender":"","Address":"","LicenceState":""}
					
					//***** Incident Data **********
					//{"EnvRoadSurface":"Select","IsPoliceContacted":"Select","GPSLongitude":"No Location",
					//"OtherDamage":"","IncidentID":"ID_20150330203052","IsPoliceInspected":"Select","IsReportGenerated":"false",
					//"PoliceReferenceNo":"","EnvRoadType":"Select","WitnessIsAny":"Select","EnvWeather":"Select","CrashType":"Select",
					//"IncidentType":"Accident","EnvSpeed":"Select","IsEmailed":"false","CrashLocation":"Select","NoOfInjuryPhotos":"0",
					//"WitnessPhoneNo":"","IsThirdPartyDamage":"Select","IncidentCategory":"Vehicle","CreatedDate":"30 Mar,2015, 08:30 PM",
					//"OtherAccidentDet":"","NoOfOtherPhotos":0,"GPSLatitude":"No Location","NoOfInjured":"0","WitnessName":""}
					
					
					//***** Fill Header Details ************
					//Incident ID
					form.setField("TxtRecordID", FilterData(incidentJSON,"IncidentID")); //incidentIDStr);
					//form.setField("TxtClaimNumber", FilterData(incidentJSON,"IncidentID"));//Commented on 5th July 2015
					//Incident Date
					form.setField("TxtRecordDate", FilterData(incidentJSON, "CreatedDate"));
					//Insurance Company Name
					form.setField("TxtCompanyName", FilterData(profileJSON,"InsName"));
					
					//************* Fill Client Details **************
					form.setField("TxtFirstName", FilterData(profileJSON,"FirstName"));
					form.setField("TxtLastName", FilterData(profileJSON,"LastName"));
					form.setField("TxtAddress", FilterData(profileJSON,"Address"));
					form.setField("TxtDOB", FilterData(profileJSON,"DOB"));
					form.setField("TxtGender", FilterData(profileJSON,"Gender"));
					form.setField("TxtAddrCity", FilterData(profileJSON,"AddrCity"));
					form.setField("TxtAddrContactNo", FilterData(profileJSON,"ContactNo"));
					form.setField("TxtLicenceState", FilterData(profileJSON,"LicenceState"));
					form.setField("TxtContactEmail", FilterData(profileJSON,"ContactEmail"));
					form.setField("TxtAddrCountry", FilterData(profileJSON,"AddrCountry"));
					form.setField("TxtAddrPostalCode", FilterData(profileJSON,"AddrPostalCode"));

					//Licence / Insurance Details
					form.setField("TxtInsName", FilterData(profileJSON,"InsName"));
					form.setField("TxtLiceneNo", FilterData(profileJSON,"LiceneNo"));
					//form.setField("TxtLiceneNo2", FilterData(profileJSON,"LiceneNo2")); //New
					form.setField("TxtInsPersonalNo", FilterData(profileJSON,"InsPersonalNo"));
					form.setField("TxtLicenceType", FilterData(profileJSON,"LicenceType"));
					form.setField("TxtPropertyType", FilterData(profileJSON,"PropertyType"));
					form.setField("TxtLicenceExpiry", FilterData(profileJSON,"LicenceExpiry"));
					form.setField("TxtVehModel", FilterData(profileJSON,"VehModel"));
					//form.setField("TxtPropRegNo", incidentIDStr); //To be updated
					//form.setField("TxtVehicleMake", FilterData(profileJSON,"VehicleMake"));
					form.setField("TxtPropRegNo", FilterData(profileJSON,"PropRegNo"));
					form.setField("TxtDatePurchased", FilterData(profileJSON,"DatePurchased"));
					
					form.setField("TxtLicenceCountry", FilterData(profileJSON,"LicenceCountry")); //New
					//form.setField("TxtIsPreviewClaims", FilterData(profileJSON,"IsPreviewClaims")); //New
					//form.setField("TxtPreviewClaims", FilterData(profileJSON,"PreviewClaims")); //New
					//form.setField("TxtClaimType", FilterData(profileJSON,"ClaimType")); //New
					//form.setField("TxtYearOfPrevClaim", FilterData(profileJSON,"YearOfPrevClaim")); //New
					form.setField("TxtIsAlarmFitted", FilterData(profileJSON,"IsAlarmFitted")); //New
					form.setField("TxtIsGPSFitted", FilterData(profileJSON,"IsGPSFitted")); //New
					form.setField("TxtIsHouseAlarmFitted", FilterData(profileJSON,"IsHouseAlarmFitted")); //New
					form.setField("TxtLastServiceDate", FilterData(profileJSON,"LastServiceDate")); //New
					form.setField("TxtGPSNumber", FilterData(profileJSON,"GPSNumber")); //New
					
					
					//Incident / Accident Details
					form.setField("TxtIncidentCategory", FilterData(incidentJSON,"IncidentCategory"));
					form.setField("TxtEnvWeather", FilterData(incidentJSON,"EnvWeather"));
					form.setField("TxtIncidentType", FilterData(incidentJSON,"IncidentType"));
					//form.setField("TxtEnvRoadType", FilterData(incidentJSON,"EnvRoadType"));
					form.setField("TxtCrashType", FilterData(incidentJSON,"CrashType"));
					form.setField("TxtEnvRoadSurface", FilterData(incidentJSON,"EnvRoadSurface"));
					//form.setField("TxtOtherDamageDetails", FilterData(incidentJSON,"OtherDamageDetails")); //"OtherDamage"));
					//form.setField("TxtEnvSpeed", FilterData(incidentJSON,"EnvSpeed"));
					form.setField("TxtIsThirdPartyDamage", FilterData(incidentJSON,"IsThirdPartyDamage"));
					form.setField("TxtIsPoliceContacted", FilterData(incidentJSON,"IsPoliceContacted"));
					form.setField("TxtWitnessIsAny", FilterData(incidentJSON,"WitnessIsAny"));
					form.setField("TxtIsPoliceInspected", FilterData(incidentJSON,"IsPoliceInspected"));
					form.setField("TxtWitnessName", FilterData(incidentJSON,"WitnessName"));
					form.setField("TxtPoliceReferenceNo", FilterData(incidentJSON,"PoliceReferenceNo"));
					//form.setField("TxtPoliceContactNo", FilterData(incidentJSON,"PoliceContactNo"));
					form.setField("TxtIncidentDate", FilterData(incidentJSON,"CreatedDate"));
					//form.setField("TxtGeoLocation", "(" + FilterData(incidentJSON,"GPSLatitude") + "," + FilterData(incidentJSON,"GPSLongitude") + ")");
					form.setField("TxtOtherAccidentDet", FilterData(incidentJSON,"OtherAccidentDet"));
					
					form.setField("TxtVisibility", FilterData(incidentJSON,"Visibility")); //new
					form.setField("TxtWeatherTime", FilterData(incidentJSON,"WeatherTime")); //new
					form.setField("TxtWeatherDate", FilterData(incidentJSON,"WeatherDate")); //new
					form.setField("TxtGPSLatitude", "Latitude:" + FilterData(incidentJSON,"GPSLatitude")); //new 
					form.setField("TxtGPSLongitude", "Longitude:" + FilterData(incidentJSON,"GPSLongitude")); //New
					form.setField("TxtCrashLocation", FilterData(incidentJSON,"CrashLocation")); //New

					stamper.close();
					reader.close();
					os.close();

					step2(incidentID);

					return success;
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					return 0;
				} catch (JSONException e) {
					e.printStackTrace();
					return 0;
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}

			public void step2(String incidentId) {
				try {

					String reportPdf = APP_FOLDERNAME + File.separator
							+ incidentId + File.separator + "text.pdf";
					File reportPDFFile = new File(reportPdf);

					String photoPdf = APP_FOLDERNAME + File.separator
							+ incidentId + File.separator + "photos.pdf";
					File photoPDFFile = new File(photoPdf);

					String finalPdf = APP_FOLDERNAME + File.separator
							+ incidentId + File.separator + "Report.pdf";
					File finalPDFFile = new File(finalPdf);

					preparePhotoDocument(incidentId);

					writePhotoIntoTextDocument(reportPDFFile, photoPDFFile,
							finalPDFFile);

				} catch (Exception e) {

				}
			}

			private void preparePhotoDocument(String incidentId) {
				try {
					String pathDest = APP_FOLDERNAME + File.separator
							+ incidentId + File.separator + "photos.pdf";
					File reportPDFFile = new File(pathDest);

					FileOutputStream fos = new FileOutputStream(reportPDFFile);

					// Create output PDF
					Document document = new Document(PageSize.A4);
					PdfWriter.getInstance(document, fos);
					document.open();

					// Add your new data / text here
					document.newPage();
					
					writePhotosToPdf("MyPhotos", document);
					writePhotosToPdf(incidentId, document);

					document.close();
				} catch (Exception e) {

				}

			}

			private void writePhotoIntoTextDocument(File reportPDFFile,
					File photoPDFFile, File finalPDFFile) {
				try {
					Log.e("WRAPPPER REPORT:", "RPT PATH:" + finalPDFFile.getAbsolutePath());
					PdfReader reportReader = new PdfReader(
							reportPDFFile.getAbsolutePath());
					PdfReader photoReader = new PdfReader(
							photoPDFFile.getAbsolutePath());
					FileOutputStream os = new FileOutputStream(
							finalPDFFile.getAbsolutePath());
					PdfStamper stamper = new PdfStamper(reportReader, os);
					Log.d("PAGESSSS", "" + photoReader.getNumberOfPages() + ",Reader PAge:" + reportReader.getNumberOfPages());
					for (int i = 1; i < photoReader.getNumberOfPages() + 1; i++) {
						PdfImportedPage page = stamper.getImportedPage(
								photoReader, i);
						stamper.insertPage(reportReader.getNumberOfPages() + i,
								photoReader.getPageSizeWithRotation(i));
						stamper.getOverContent(i + 1).addTemplate(page, 0, 0);   //Here we have to change i+1 as i+2 if template first two page exceeding

					}
					os.flush();
					stamper.close();
					photoReader.close();
					reportReader.close();
					os.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private void writePhotosToPdf(String incidentId, Document document) {
				try {

					if (incidentId.equalsIgnoreCase("MyPhotos")) {
						document.newPage();
						Chunk chunk_profileDetails = new Chunk(
								"PROFILE PHOTOS", headFont);
						document.add(chunk_profileDetails);
						document.add(Chunk.NEWLINE);

						String path = APP_FOLDERNAME + File.separator
								+ Constants.STR_PROFILE_FOLDER + File.separator
								+ "MyPhotos";
						File profileDir = new File(path);

						if (profileDir.exists()) {
							List<File> fileListinACategory = Utility
									.getFiles(profileDir.getPath());

							System.out.println(fileListinACategory.size());

							if (fileListinACategory != null
									&& fileListinACategory.size() > 0) {
								writeImageToPdf(fileListinACategory, document);
							} else {
								document.newPage();
								Paragraph paragraph = new Paragraph("");
								addEmptyLine(paragraph, 5);
								document.add(paragraph);
								Chunk chunk_profile = new Chunk(
										"               NO PROFILE PHOTOS PRESENT",
										headFont);
								document.add(chunk_profile);

							}
						} else {
							//document.newPage();
							Paragraph paragraph = new Paragraph("");
							addEmptyLine(paragraph, 5);
							document.add(paragraph);
							Chunk chunk_profile = new Chunk(
									"               NO PROFILE PHOTOS PRESENT",
									paraHeadFont);
							document.add(chunk_profile);
						}

					} else {
						document.newPage();
						Chunk chunk_profileDetails = new Chunk(
								"INCIDENT PHOTOS", headFont);
						document.add(chunk_profileDetails);

						// Accessing the incident directory
						String path = APP_FOLDERNAME + File.separator
								+ incidentId + File.separator;
						File incidentStorageDir = new File(path);

						// List of folders inside the incident directory
						List<File> categoryFolders = Utility.getFolders(
								incidentStorageDir.getPath(), true);
						Log.e("categoryFolders.size()",
								"" + categoryFolders.size());
						if (categoryFolders != null
								&& categoryFolders.size() > 0) {

							for (File eachCategoryFolder : categoryFolders) {
								List<File> fileListinACategory = Utility
										.getFiles(eachCategoryFolder.getPath());
								Log.e("fileListinACategory.size()", ""
										+ fileListinACategory.size());

								if (fileListinACategory != null
										&& fileListinACategory.size() > 0) {
									/*Chunk chunk_categoryName = new Chunk(
											eachCategoryFolder.getName(),
											headFont);
									Paragraph paragraph = new Paragraph(
											chunk_categoryName);
									paragraph
											.setAlignment(Paragraph.ALIGN_CENTER);
									document.add(paragraph);*/
									// Write the photos present in particular
									// folder to
									// pdf
									writeImageToPdf(fileListinACategory,
											document);
									document.newPage();
								}
							}
						} else {
							Paragraph paragraph = new Paragraph("");
							addEmptyLine(paragraph, 5);
							document.add(paragraph);
							Chunk chunk_incident = new Chunk(
									"               NO INCIDENT PHOTOS PRESENT",
									paraHeadFont);
							document.add(chunk_incident);
						}
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private void writeImageToPdf(List<File> fileListinACategory,
					Document document) {
				try {

					if (fileListinACategory != null
							&& fileListinACategory.size() > 0) {

						for (File photo : fileListinACategory) {
							Paragraph paragraph = new Paragraph("");
							addEmptyLine(paragraph, 1);
							document.add(paragraph);
							Log.e("writeImageToPdf", "filePath" + photo);
							FileInputStream fis = new FileInputStream(photo);
							InputStream ims = new BufferedInputStream(fis);
							Bitmap bmp = BitmapFactory.decodeStream(ims);

							ExifInterface exif = new ExifInterface(
									photo.getPath());
							int orientation = exif.getAttributeInt(
									ExifInterface.TAG_ORIENTATION,
									ExifInterface.ORIENTATION_UNDEFINED);
							Bitmap bmRotated = rotateBitmap(bmp, orientation);

							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							if(photo.getName().equalsIgnoreCase("myLocation.png"))
							{
								bmRotated.compress(Bitmap.CompressFormat.PNG, 50,
										stream); 
							}
							else
							{
								bmRotated.compress(Bitmap.CompressFormat.JPEG, 50,
										stream); 
							}
							// Reduce
												// the
												// size
												// of
												// image
							Image image = Image.getInstance(stream
									.toByteArray());

							Log.e("image.getWidth();", "" + image.getWidth());
							Log.e("image.getHeight()", "" + image.getHeight());
							float diff = 1.35f;
							if(photo.getName().equalsIgnoreCase("myLocation.png"))
							{
								diff = 2f;
							}
							float imageWidth = (float) (document.getPageSize()
									.getWidth() / diff);
							float imageHeight = (float) (document.getPageSize()
									.getHeight() / 3);
							image.scaleAbsolute(imageWidth, imageHeight);
							// image.scalePercent(16); // Reduce the width and
							// height
							// (Scaling)
							image.setAlignment(Image.ALIGN_CENTER
									| Image.ALIGN_MIDDLE);
							document.add(image);

							String dateString = Utility.getImageDate(photo);
							if(dateString==null) dateString = "";
							
							//File name parsing
							String fname = photo.getName();
							fname = fname.replace(".jpg", "");
							fname = fname.replace(".png", "");
							fname = fname.replace("_", "");
							//fname = fname.substring(0, fname.indexOf("_"));									
							
							Chunk chunk_photoName = new Chunk("Name:("+ fname + "), Date:" + dateString,
									imageNameFont);
							Paragraph para1 = new Paragraph(chunk_photoName);
							para1.setAlignment(Paragraph.ALIGN_CENTER);
							document.add(para1);

							Log.e("writeImageToPdf", "Done:");
						}

					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
				try {
					Matrix matrix = new Matrix();
					switch (orientation) {
					case ExifInterface.ORIENTATION_NORMAL:
						return bitmap;
					case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
						matrix.setScale(-1, 1);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						matrix.setRotate(180);
						break;
					case ExifInterface.ORIENTATION_FLIP_VERTICAL:
						matrix.setRotate(180);
						matrix.postScale(-1, 1);
						break;
					case ExifInterface.ORIENTATION_TRANSPOSE:
						matrix.setRotate(90);
						matrix.postScale(-1, 1);
						break;
					case ExifInterface.ORIENTATION_ROTATE_90:
						matrix.setRotate(90);
						break;
					case ExifInterface.ORIENTATION_TRANSVERSE:
						matrix.setRotate(-90);
						matrix.postScale(-1, 1);
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						matrix.setRotate(-90);
						break;
					default:
						return bitmap;
					}

					Bitmap bmRotated = Bitmap
							.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
					bitmap.recycle();
					return bmRotated;
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					return null;
				}
			}

			private void addEmptyLine(Paragraph paragraph, int lines) {
				for (int i = 0; i < lines; i++) {
					paragraph.add(new Paragraph(""));
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// After Async close progress dialog
				Utility.hideActivityIndicator();

				if (result == 1) {
					invokeSuccessCallback("Done");
				} else {
					invokeFailureCallback("Failed");
				}

				String reportPdf = APP_FOLDERNAME + File.separator + incidentID
						+ File.separator + "text.pdf";
				File reportPDFFile = new File(reportPdf);

				String photoPdf = APP_FOLDERNAME + File.separator + incidentID
						+ File.separator + "photos.pdf";
				File photoPDFFile = new File(photoPdf);
				if (reportPDFFile.delete() && photoPDFFile.delete()) {
					Log.d("Reports deleted", "Reports deleted");
				}

			}
		}.execute();
		return retVal;
	}

	public String recordAudio(String incidentID, String successCB,
			String failureCB) {
		Log.e("WRAPPER", "Request Reached Wrapper- recordAudio");
		String retVal = "Done";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;
		try {
			if (!isRecording) {
				AudioRecord.startRecording(incidentID);
				isRecording = true;
				retVal = "Recording Started";
			} else {
				AudioRecord.stopRecording();
				isRecording = false;
				retVal = "Recording Stopped";
			}
		} catch (Exception e) {
			e.printStackTrace();
			retVal = "ERROR";
			invokeFailureCallback(retVal);
		}
		invokeSuccessCallback(retVal);
		return retVal;
	}

	public String getLocation(String incidentID, String successCB,
			String failureCB) {

		Log.e("WRAPPER", "Request Reached Wrapper- getLocation");
		String retVal = "Done";
		this.CB_Success = successCB;
		this.CB_failure = failureCB;
		String defaultData;

		try {
			String path = APP_FOLDERNAME + File.separator + incidentID
					+ File.separator + "data.json";
			File incidentDir = new File(path);
			Log.e("Path", "Path" + incidentDir.getAbsolutePath()
					+ ", isExists:" + incidentDir.exists());
			if (incidentDir.isFile() && incidentDir.exists()) {
				FileReader reader = new FileReader(
						incidentDir.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line).append("\n");
				}
				bufferedReader.close();
				reader.close();
				defaultData = stringBuffer.toString();
				JSONObject obj = new JSONObject(defaultData);

				Log.d("GEOLOCATION","Before Geo-location Fetch:");
				GetLocation objGetLocation = new GetLocation(this.myActivity);
				if (objGetLocation.displayGpsStatus()) {
					Location location = objGetLocation.getLocationData();
					if (location != null) {
						obj.put("GPSLatitude", location.getLatitude());
						obj.put("GPSLongitude", location.getLongitude());
						defaultData = obj.toString();
						retVal = "" + location.getLatitude() + ","
								+ location.getLongitude();
						Log.d("Latitude", "" + location.getLatitude());
						Log.d("Longitude", "" + location.getLongitude());
					}

				} else {
					retVal = "OFF";
				}

				// Create data file inside Incident folder
				path = APP_FOLDERNAME + File.separator + incidentID
						+ File.separator + "data.json";
				File incidentDataFile = new File(path);
				if (incidentDataFile.isAbsolute() && incidentDataFile.exists())
					incidentDataFile.delete();
				incidentDataFile.createNewFile();

				// Write default JSON to incident data file
				FileWriter fileWritter = new FileWriter(incidentDataFile, false);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(defaultData);
				bufferWritter.close();
				fileWritter.close();
				invokeSuccessCallback(retVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
			retVal = "ERROR";
			invokeFailureCallback(retVal);
		}
		return retVal;

	}
	
	public void showLocation(String latitude, String longitude)
	{
		try
		{
			
		}
		catch(Exception e)
		{			
		}
	}
	
	//Read current location
	public String getLocationData()
	{
		String locationData = null;
		JSONObject data = new JSONObject();
		try
		{
			GetLocation objGetLocation = new GetLocation(this.myActivity);
			if (objGetLocation.displayGpsStatus()) 
			{
				Location location = objGetLocation.getLocationData();
				if (location != null) 
				{
					data.put("GPSLatitude", location.getLatitude());
					data.put("GPSLongitude", location.getLongitude());
					locationData = data.toString();
				}

			} 
			else 
			{
				locationData = "OFF";
			}
		}
		catch(Exception e)
		{			
		}
		return locationData;
	}
	
	/**
	 * Used to select date from date picker
	 * @param requestData
	 * @param successCB
	 * @param failureCB
	 * @return
	 */
	public int pickDate(String requestData, String successCB, String failureCB)
	{
		this.CB_Success = successCB;
		this.CB_failure = failureCB;
		
		try
		{			
			// Convert JSON request string to JSON Object
			JSONObject datePickerReq = new JSONObject(requestData);
			
			// Validate Session ID related folder exists or not
			String reqKey = "Request";
			final String Title = datePickerReq.getJSONObject(reqKey).getString("Title");
			
			final String DefaultDay = datePickerReq.getJSONObject(reqKey).getString("DefaultDay");
			
			final String DefaultMonth = datePickerReq.getJSONObject(reqKey).getString("DefaultMonth");
			
			final String DefaultYear = datePickerReq.getJSONObject(reqKey).getString("DefaultYear");
			
			
			//Calendar Control
			final Calendar c = Calendar.getInstance();
			final int mYear = DefaultYear!=null ? Integer.parseInt(DefaultYear): c.get(Calendar.YEAR);
			final int mMonth = DefaultMonth!=null? Integer.parseInt(DefaultMonth): c.get(Calendar.MONTH);
			final int mDay = DefaultDay!=null? Integer.parseInt(DefaultDay):c.get(Calendar.DAY_OF_MONTH);
			 
			int month =0 ;
			if((mMonth-1) >0) month = mMonth-1;
			
			DatePickerDialog dpd = new DatePickerDialog(myActivity,
			        new DatePickerDialog.OnDateSetListener() 
					{			 
			            @Override
			            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
			            {	
			            	String dateStr=  year + "/" + (monthOfYear+1) + "/" + dayOfMonth;
			            	SimpleDateFormat sm = new SimpleDateFormat("yyyy/MM/dd");
							try 
							{
								String resultStr =(new SimpleDateFormat("yyyy/MM/dd").format(sm.parse(dateStr))); 
								invokeSuccessCallback(resultStr);
							} 
							catch (ParseException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            }
			        }, mYear, month, mDay);
			
			if(Title!=null && Title.length()>0)
			{
				dpd.setTitle(Title);
			}
			else
			{
				dpd.setTitle("Select Date");
			}
			dpd.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 1;
		}
		return 1;
	}
	
	/**
	 * Used to select time from time picker
	 * @param requestData
	 * @param successCB
	 * @param failureCB
	 * @return
	 */
	public int pickTime(String requestData, String successCB, String failureCB)
	{
		this.CB_Success = successCB;
		this.CB_failure = failureCB;
		
		try
		{			
			// Convert JSON request string to JSON Object
			JSONObject datePickerReq = new JSONObject(requestData);
			
			// Validate Session ID related folder exists or not
			String reqKey = "Request";
			final String Title = datePickerReq.getJSONObject(reqKey).getString("Title");
			final String DefaultHour = datePickerReq.getJSONObject(reqKey).getString("DefaultHour");
			final String DefaultMin = datePickerReq.getJSONObject(reqKey).getString("DefaultMin");	
			final String DefaultAMPM = datePickerReq.getJSONObject(reqKey).getString("DefaultAMPM");
			
			//Calendar Control
			final Calendar c = Calendar.getInstance();
			final int mHour = DefaultHour!=null ? Integer.parseInt(DefaultHour): c.get(Calendar.HOUR);
			final int mMin = DefaultMin!=null? Integer.parseInt(DefaultMin): c.get(Calendar.MINUTE);
			
			//Convert from 12hours to 24 Hours
			final int mHour2 = DefaultAMPM.equalsIgnoreCase("PM")? (mHour + 12) : mHour;
			 
			TimePickerDialog timepick = new TimePickerDialog(myActivity, new OnTimeSetListener() 
			{
				@Override
				public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
					String dateStr=  selectedHour + ":" + selectedMinute;	  
					
					Log.e("TIME PICKER", "Selected Date:" + dateStr);
					
					int h = selectedHour;
					String ampm="AM";
					
					//Convert to 12 Hours Format
					if(selectedHour>12)
					{
						h = (selectedHour-12);
						ampm = " PM";
					}
					else if(selectedHour==12)
					{
						ampm = " PM";
					}
					else
					{
						ampm = " AM";
					}
					
					String mStr = selectedMinute<10?("0"+selectedMinute):(""+selectedMinute);
					String hStr = h <10?("0"+h ):(""+h);
					
					dateStr=  hStr + ":" + mStr + ampm;	
					
					invokeSuccessCallback(dateStr);					
				}
			}, mHour2, mMin, false);
			
			if(Title!=null && Title.length()>0)
			{
				timepick.setTitle(Title);
			}
			else
			{
				timepick.setTitle("Select Time");
			}
			timepick.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 1;
		}
		return 1;
	}
	
	/*
	 * Used to track Location
	 */
	public static String CB_Success_SMS = "success_callback";
	public static String CB_failure_SMS = "failure_callback";
	public int TrackLocaiton(String mobileNumber, String successCB, String failureCB)
	{
		CB_Success_SMS = successCB;
		CB_failure_SMS = failureCB;
		try
		{
			 SmsManager smsManager = SmsManager.getDefault();
             smsManager.sendTextMessage(mobileNumber, null, "GETLOCHASH", null, null);
             InvokeSMSResponse("Waiting..");
		}
		catch(Exception e)
		{			
		}
		return 1;
	}
	public static void InvokeSMSResponse(String msg)
	{
		try {
			Log.e("WRAPPER", "URL" + CB_Success_SMS);
			invokeURL("javascript:" + CB_Success_SMS + "('" + msg + "')");
		} catch (Exception e) {
			// write error log to file.
		} finally {
		}
	}
	
	public String getSecurityPin()
	{
		String pinNo = "";
		try
		{
			//Read Pin values from shared preferences
			SharedPreferences sharedpreferences = myActivity.getSharedPreferences("SETTINGS_PREFERENCES", Context.MODE_PRIVATE);
			pinNo = sharedpreferences.getString("PIN", "");
		}
		catch(Exception e)
		{
			pinNo = "";
		}
		return pinNo;
	}
	public String updateSecurityPin(String pinFromJS)
	{
		String retVal = "DONE";
		try
		{
			//Update pin values to shared preferences
			SharedPreferences sharedpreferences = myActivity.getSharedPreferences("SETTINGS_PREFERENCES", Context.MODE_PRIVATE);
			Editor editor = sharedpreferences.edit();
			editor.putString("PIN", pinFromJS);
			editor.commit();
			Log.e("WRAPPER", "Update Status: done");
			return retVal;
		}
		catch(Exception e)
		{
			retVal = "ERROR";
		}
		return retVal;
	}
	
	public void ShowMap(String latitude, String longitude, String incidentID)
	{
		try
		{
			/*Uri gmmIntentUri = Uri.parse("geo:" + location);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			if (mapIntent.resolveActivity(myActivity.getPackageManager()) != null) {
				myActivity.startActivity(mapIntent);
			}*/
			
			//Show customized map view activity
			Intent showMapIntent =new Intent(myActivity, ShowMap.class);
			showMapIntent.putExtra("SHOWLAT", latitude);
			showMapIntent.putExtra("SHOWLONG", longitude);
			showMapIntent.putExtra("INCIDENTID", incidentID);
			myActivity.startActivityForResult(showMapIntent, Constants.REQUEST_CODE_GET_LOCATION);
			
		}
		catch(Exception e)
		{			
		}
	}
	
	public void pickContacts(String requestData, String successCB, String failureCB) 
	{
		Log.e("IAWRAPPER", "Reached pickContacts Wrapper method");
		try 
		{
			this.CB_Success = successCB;
			this.CB_failure = failureCB;
			
			Log.e("WRAPPER INBOUND","CB Methods:" + this.CB_Success_SMS +"," + this.CB_failure_SMS);
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			myActivity.startActivityForResult(intent, Constants.REQUEST_CODE_PICK_CONTACT);
		}
		catch(Exception e)
		{
		}
	}
}