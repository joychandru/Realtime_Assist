package com.insurance.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressLint({"NewApi"})
public class ImagePreviewActivity extends Activity {
    public WebView appWebView;
    public Context myContext;

    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(PdfWriter.PageModeUseOC, PdfWriter.PageModeUseOC);
        setContentView(R.layout.webview);
        Bundle b = getIntent().getExtras();
        String file = "/data/data/com.insurance.app/files/InsAssist/ID_20000101063646/OtherPhoto/IDCard_1.jpg";
        if (b != null && b.containsKey("imgFilePath")) {
            file = b.getString("imgFilePath");
        }
        Uri objUri = Uri.parse(file);
        WebView appWebView = (WebView) findViewById(R.id.webView1);
        appWebView.getSettings().setBuiltInZoomControls(true);
        appWebView.getSettings().setLoadWithOverviewMode(true);
        appWebView.getSettings().setUseWideViewPort(true);
        appWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        appWebView.setScrollbarFadingEnabled(true);
        appWebView.loadUrl("file://" + file);
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "onDestroy");
    }

    protected void onPause() {
        Log.d("onPause", "onPause");
        if (AudioRecord.mRecorder != null) {
            AudioRecord.stopRecording();
        }
        super.onPause();
    }

    protected void onRestart() {
        Log.d("onRestart", "onRestart");
        super.onRestart();
    }

    protected void onResume() {
        Log.d("onResume", "onResume");
        super.onResume();
    }
}
