package com.insurance.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.json.JSONObject;

import com.insurance.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class MainActivity extends Activity //implements OnTouchListener // implements SimpleGestureListener
{
	public Context myContext;
	public WebView appWebView;
	public Wrapper objWrapper;

	/****
	 * It helps to handle activity creation and initialisation steps.
	 **/
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	

		//Window settings
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		*/
		
		setContentView(R.layout.webview);
		
		//Validate Licence
		boolean isValid = ValidateLicence();
		if(!isValid)
		{
			final Handler handler = new Handler();
			handler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(myContext)
					.setTitle(Constants.STR_APP_NAME)
					.setMessage("Trail period got expired, Please upgrade licene version")
					.setPositiveButton("OK",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										
										finish();
			
										}		
								}).setCancelable(false).create().show();
				}

			});
			
		}
		
		appWebView = (WebView) findViewById(R.id.webView1);
		
		///Used to set on screen keyboard settings to focus HTML5 text boxes
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(appWebView, InputMethodManager.SHOW_IMPLICIT);
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		appWebView.setNetworkAvailable(true);
		//appWebView.setInitialScale(100);
		
		WebSettings settings = appWebView.getSettings();
		
		settings.setJavaScriptEnabled(true);
		
		settings.setLoadsImagesAutomatically(true);
		settings.setUseWideViewPort(true);
		settings.setAllowContentAccess(true);
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);
		/*appWebView.getSettings().setUseWideViewPort(true);
		appWebView.requestFocus(View.FOCUS_DOWN);*/
		
		appWebView.clearCache(true);
		appWebView.clearHistory();
		appWebView.clearFormData();
		appWebView.clearSslPreferences();

		//Read Application Context
		myContext = this;
	
		objWrapper = new Wrapper(this, appWebView);
		appWebView.addJavascriptInterface(objWrapper, "Wrapper");
		
		//Allow Access UrL
		WebViewClient obj = (WebViewClient)new MyBrowser();
		appWebView.setWebViewClient(obj);		

		/*appWebView.setOnTouchListener(new View.OnTouchListener() {           
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                case MotionEvent.ACTION_UP:
	                    if (!v.hasFocus()) {
	                        v.requestFocus();
	                    }
	                    break;
	            }
	            return false;
			}
	    });*/  
		
		final Activity activity = this;
		appWebView.setWebChromeClient(new WebChromeClient() 
		{
			   public void onProgressChanged(WebView view, int progress) {
			     // Activities and WebViews measure progress with different scales.
			     // The progress meter will automatically disappear when we reach 100%
			     activity.setProgress(progress * 1000);
			   }

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				// TODO Auto-generated method stub
				final JsResult myresult=  result;
					new AlertDialog.Builder(myContext)
					.setTitle(Constants.STR_APP_NAME)
					.setMessage(message)
					.setPositiveButton("OK",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										myresult.confirm();
			
										}		
								}).setCancelable(false).create().show();
					
	                return true; // super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				final JsResult res = result;
				 AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
				    builder.setMessage(message)
				       .setCancelable(false)
				       .setTitle(Constants.STR_APP_NAME)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   res.confirm();
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                res.cancel();
				           }
				       });
				    AlertDialog alert = builder.create();
				    alert.show();
				
				return true; //.super.onJsConfirm(view, url, message, result);
			}
			   
			
		});

		appWebView.loadUrl("file:///android_asset/www/home.html");
	}
	
	   /***
	 	 * This method helps to allow URL access in cusom brwoserclient
	 	 ***/
		
	 	public class MyBrowser extends WebViewClient 
	 	{
	 		@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				//super.onReceivedSslError(view, handler, error);
				handler.proceed();
			}
	 		
	 	     @Override
	 	     public boolean shouldOverrideUrlLoading(WebView view, String url) 
	 	     {	       
	 	        if (url.indexOf("tel:") > -1) 
	 	        {
	 	        	try
	 	        	{
	 	            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
	 	        	}
	 	        	catch(Exception e)
	 	        	{
	 	        		
	 	        	}
	 	            return true;
	 	        } 
	 	        //Add new condition to check mail address, normally mail address will have the prefix like "mail"
	 	        else if (url.indexOf("mail:") > -1) 
	 	        {
	 	        	try {
	 	            startActivity(new Intent(Intent.ACTION_SEND, Uri.parse(url)));
	 	        	}
	 	        	catch(Exception e)
	 	        	{
	 	        	}
	 	            return true;
	 	        }
	 	        else 
	 	        {
	 	        	view.loadUrl(url);
	 	            return true;
	 	        }
	 	     }
	 	}
	
	/****
	 * SSL security bypassing routines
	 * *****/
	public class SSLAcceptingWebViewClient extends WebViewClient 
	{
		public SSLAcceptingWebViewClient(Context ctx) {
			try {

			} catch (Exception e) {
				// Write error messages in error Log
			}
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

			try {
				handler.proceed();
			} catch (Exception e) {
				// Write error messages in error Log
			}
		}
	}

	@Override
	public void onBackPressed() 
	{		Log.d("onBackPressed", "onBackPressed");

		objWrapper.invokeURL("javascript:COMMON.backPressed()");
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("onDestroy", "onDestroy");
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("onPause", "onPause");
		if(AudioRecord.mRecorder!=null){
			AudioRecord.stopRecording();
		}
		super.onPause();
	}



	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.d("onRestart", "onRestart");

		super.onRestart();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("onResume", "onResume");

		super.onResume();
	}



	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Log.e("INSASSIST", "ON ACTION RESULT, ResultCode:" + resultCode + ", Request Code:" + requestCode); 
		switch (requestCode) 
		{
			case Constants.REQUEST_CODE_TAKE_PHOTO:
				if (resultCode == RESULT_OK) 
				{
					Bundle b = data.getExtras();
					int photosCount = b.getInt("RESULT");
					objWrapper.invokeSuccessCallback(""+photosCount);
				}
				break;
			case Constants.REQUEST_CODE_SEND_MAIL:
				if (resultCode == 0) 
				{
					objWrapper.invokeSuccessCallback("Done");
				}
				else
				{
					objWrapper.invokeSuccessCallback("ERROR");
				}
				break;
			case Constants.REQUEST_CODE_PICK_CONTACT:
				if (resultCode  == RESULT_OK)
				{
					 Uri contactData = data.getData();
					 String name ="";
					 String number = "";
			         Cursor c =  managedQuery(contactData, null, null, null, null);
			         if (c.moveToFirst()) 
			         {
			              name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			         }
			         JSONObject responseData = new JSONObject();
			         try
			         {
			        	 responseData.put("Name", name);
			        	 responseData.put("Number", number);
			         }
			         catch(Exception e){}
					 objWrapper.invokeSuccessCallback(responseData.toString());
				}
				else
				{
					objWrapper.invokeFailureCallback("ERROR");
				}
				break;
			case Constants.REQUEST_CODE_GET_LOCATION:  //Case for show location
				Log.e("WrapperActivity", "Case for Show location");
				Bundle b = data.getExtras();
				if(b!=null)
				{
					String lat = b.getString("updated_lat");
					String lang = b.getString("updated_lng");
					String isChanged = "true";
					objWrapper.invokeURL("javascript:NEWINCIDENT.pushLocation('" + lat + "','" + lang + "','" +isChanged +"')");
				}
				break;
		}	
	}
	
	private String key = "DESede";
	/**
	 * It used to encrypt licence text 
	 * @param inputNormalStr
	 * @return
	 */
	private String encryptLicence(String inputNormalStr)
	{
		if(true) return inputNormalStr;
		String encryptedStr="";
		try
		{
			Key symKey = KeyGenerator.getInstance(key).generateKey();
			Cipher c = Cipher.getInstance(key);
			c.init(Cipher.ENCRYPT_MODE, symKey);
			byte[] inputBytes =Base64.decode(inputNormalStr, Base64.DEFAULT); //.getBytes();
			byte[] encryptionBytes = c.doFinal(inputBytes);
			encryptedStr= new String(encryptionBytes);
			Log.e("WRAPPER","ENCKEY:" + encryptedStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			encryptedStr=inputNormalStr;
		}
		return encryptedStr;
	}
	/**
	 * Used to decrypt license details.
	 * @param inputEncryptedStr
	 * @return
	 */
	private String decrypteLicence(String inputEncryptedStr)
	{
		if(true) return inputEncryptedStr;
		String decryptedStr="";
		try
		{
			Log.e("WRAPPER", "Entered Decrypt License method");
			Key symKey = KeyGenerator.getInstance(key).generateKey();
			Cipher c = Cipher.getInstance(key);
			Log.e("WRAPPER", "Before init");
			c.init(Cipher.DECRYPT_MODE, symKey);	
			Log.e("WRAPPER", "After init");
			byte[] decrypt = c.doFinal(inputEncryptedStr.getBytes());
			//decryptedStr = new String(decrypt);
			Log.e("WRAPPER","DECKEY: " + decryptedStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			decryptedStr=inputEncryptedStr;
		}
		return decryptedStr;
	}
	
	private boolean ValidateLicence()
	{
		boolean retVal = false;
		String dateTemplate = "yyyyMMdd";
		try
		{
			String fileFolder = Utility.getApplicationFolder(this);
			String filePath = fileFolder + File.separator + "lic.dat"; //Temp
			
			File licenceFile = new File(filePath);
			JSONObject obj = new JSONObject();
			
			//Get current date
			Date currDate = new Date();
			SimpleDateFormat formatDate = new SimpleDateFormat(dateTemplate);
			String dateStr=  formatDate.format(currDate);
			
			File licenceFolder = new File(fileFolder);
			if(!licenceFolder.exists())
			{
				licenceFolder.mkdirs();
			}
			
			if(!licenceFile.exists()){
				licenceFile.createNewFile();
				//Write Details content
				obj.put("updateDate", dateStr);
				obj.put("Count", 1);
				
				if(licenceFile.exists()) licenceFile.delete();
				FileWriter fileWritter = new FileWriter(licenceFile, false);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				
				//String encrptedStr = encryptLicence(obj.toString());
				
				bufferWritter.write(obj.toString());
				bufferWritter.close();
				fileWritter.close();
				
				return true;
			}
			else
			{
				//Read existing date from file licence file
				FileReader reader = new FileReader(licenceFile.getAbsoluteFile());
				BufferedReader bufferedReader = new BufferedReader(reader);
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
				}
				bufferedReader.close();
				reader.close();
				String jsonStr = stringBuffer.toString();
				
				//String decrptedStr = decrypteLicence(jsonStr);
				
				JSONObject jsonObj = new JSONObject(jsonStr);
				int count = jsonObj.getInt("Count");
				Log.e("WRAPPER", "LICENCE DATA:" + jsonStr + ", count:" + count);
				if(count>17)
				{
					return false;
				}
				else
				{
					String dateStrFromFile = jsonObj.getString("updateDate");
					SimpleDateFormat fromformat = new SimpleDateFormat(dateTemplate);
					Log.e("WRAPPER", "Start Date Str:" + dateStrFromFile);
					String currDateStr = fromformat.format(currDate);
					Log.e("WRAPPER", "Curr Date Str:" + currDateStr);
					if(Double.parseDouble(dateStrFromFile) != Double.parseDouble(currDateStr))
					{
						Log.e("Wrapper","First Case");
						count = count+1;
						jsonObj = new JSONObject();
						jsonObj.put("updateDate", fromformat.format(currDate));
						jsonObj.put("Count", count);
					}
					else
					{
						Log.e("Wrapper","Second Case");
						jsonObj = new JSONObject();
						jsonObj.put("updateDate", fromformat.format(currDate));
						jsonObj.put("Count", count);
					}
					if(licenceFile.exists()) licenceFile.delete();
					FileWriter fileWritter = new FileWriter(licenceFile, false);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					
					//String encrptedStr = encryptLicence(jsonObj.toString());
					
					bufferWritter.write(jsonObj.toString());
					bufferWritter.close();
					fileWritter.close();
					
					return true;
				}
			}
		}
		catch(Exception e)
		{	
			e.printStackTrace();
			retVal =false;
		}
		return retVal;
	}

	/*@Override
	public boolean onTouch(View sender, MotionEvent e) {
		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_UP:
			View v = (View)sender;
			if (!v.hasFocus()) {
				v.requestFocus();
			}
			break;
		}
		return false;
	}*/
}

