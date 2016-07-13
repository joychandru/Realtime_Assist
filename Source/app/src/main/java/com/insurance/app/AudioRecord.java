package com.insurance.app;

import android.media.MediaRecorder;
import android.util.Log;
import java.io.File;

public class AudioRecord {
    private static final String LOG_TAG = "AudioRecord";
    public static MediaRecorder mRecorder;

    static {
        mRecorder = null;
    }

    public static void startRecording(String incidentId) {
        //File reportsStorageDir = new File(new File(Wrapper.APP_FOLDERNAME + File.separator + incidentId + File.separator), new StringBuilder(String.valueOf(incidentId)).append(".3gp").toString());
        File reportsStorageDir = new File(new File("/sdcard/InsAssist/" + File.separator + incidentId + File.separator), new StringBuilder(String.valueOf(incidentId)).append(".3gp").toString());
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(1);
        mRecorder.setOutputFormat(1);
        mRecorder.setOutputFile(reportsStorageDir.getPath());
        mRecorder.setAudioEncoder(1);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
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
