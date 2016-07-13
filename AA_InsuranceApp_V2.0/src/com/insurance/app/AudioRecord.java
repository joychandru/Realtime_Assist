package com.insurance.app;

import java.io.File;


import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AudioRecord {
	
	public static  MediaRecorder mRecorder=null;
	private  final static String LOG_TAG = "AudioRecord";
	
	public static void startRecording(String incidentId)
	{
		//Create Incident Folder
		String path = Wrapper.APP_FOLDERNAME + File.separator + incidentId + File.separator;
		File incidentDir = new File(path);
		File reportsStorageDir=new File(incidentDir,incidentId+".3gp");

		mRecorder=new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);		
		mRecorder.setOutputFile(reportsStorageDir.getPath());
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		
		try {
			mRecorder.prepare();
		} catch(Exception e) {
			Log.d(LOG_TAG, "prepare() failed");
		}		
		mRecorder.start();
		Log.d(LOG_TAG, "RECORD STARTED");
	}

	 public static void stopRecording() {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
	    	Log.d(LOG_TAG, "RECORD STOPPED");
	    }

}
