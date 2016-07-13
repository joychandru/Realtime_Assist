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
import android.view.Display;
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
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ImagePreviewActivity extends Activity //implements OnTouchListener // implements SimpleGestureListener
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//setContentView(R.layout.imagepreview);
		setContentView(R.layout.webview);
		
		Bundle b = getIntent().getExtras();
		String file = "/data/data/com.insurance.app/files/InsAssist/ID_20000101063646/OtherPhoto/IDCard_1.jpg";
		if(b !=null)
		{
			if(b.containsKey("imgFilePath"))
			{
				file = b.getString("imgFilePath");
			}
		}
		Uri objUri = Uri.parse(file);
		final WebView appWebView = (WebView) findViewById(R.id.webView1);
		appWebView.getSettings().setBuiltInZoomControls(true);
		appWebView.getSettings().setLoadWithOverviewMode(true);
		appWebView.getSettings().setUseWideViewPort(true);
		appWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);
		appWebView.setScrollbarFadingEnabled(true);
		appWebView.loadUrl("file://" + file);
		
		/*ImageView objPreviewImage = (ImageView)this.findViewById(R.id.imageView);
		Uri objUri = Uri.parse(file);
		//"/data/data/com.insurance.app/files/InsAssist/ID_20000101063646/OtherPhoto/IDCard_1.jpg");
		objPreviewImage.setImageURI(objUri);*/
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
}

