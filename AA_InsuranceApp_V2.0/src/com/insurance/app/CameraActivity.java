package com.insurance.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class CameraActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private PictureCallback mPicture;
	private Button capture, switchCamera,flash;
	private Context myContext;
	private FrameLayout cameraPreview;
	private boolean cameraFront = false;
	private boolean safeToTakePicture=false;
	private boolean isFlashOn=false;
	private static String incidentId;
	private static String category;
	private static String prefix;
	private int photoCount = 1;
	File mediaStorageDir;
	private ZoomControls zoomControls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_port);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myContext = this;
		getData();
		createFolder();
		initialize();

	}

	private void createFolder() {
		// make a new file directory inside the "sdcard" folder
		String path = Wrapper.APP_FOLDERNAME + File.separator + incidentId
				+ File.separator + category + File.separator;
		mediaStorageDir = new File(path);

		Log.e("INSURANCE", "Path:" + mediaStorageDir);
		if (!mediaStorageDir.exists()) {
			mediaStorageDir.mkdirs();
			mediaStorageDir.setWritable(true);
		}
		photoCount = mediaStorageDir.list().length;  //Fix for file name increment
		photoCount++;
		MediaScannerConnection.scanFile(myContext, new String[] {mediaStorageDir.getPath()}, null, null);
	}

	private void getData() {
		Bundle bundle = getIntent().getExtras();
		incidentId = bundle.getString("IncidentId");
		category = bundle.getString("Category");
		prefix = bundle.getString("Prefix");
	}

	public void initialize() {
		cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
		mPreview = new CameraPreview(myContext, mCamera);
		cameraPreview.addView(mPreview);

		capture = (Button) findViewById(R.id.button_capture);
		capture.setOnClickListener(captrureListener);
		
		flash=(Button)findViewById(R.id.button_flash);
		flash.setOnClickListener(flashListener);

		switchCamera = (Button) findViewById(R.id.button_ChangeCamera);
		switchCamera.setOnClickListener(switchCameraListener);
		
		zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
		zoomControls.setIsZoomInEnabled(true);
		zoomControls.setIsZoomOutEnabled(true);
		zoomControls.setOnZoomInClickListener(zoomInListener);
		zoomControls.setOnZoomOutClickListener(zoomOutListener);
	}
	
	OnClickListener zoomInListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			zoomCamera(true);
		}
	};

	OnClickListener zoomOutListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			zoomCamera(false);

		}
	};

	
	OnClickListener flashListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(hasFlash()){				
				Parameters p = mCamera.getParameters();				
				if (isFlashOn) {
					Log.i("info", "torch is turn off!");
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(p);
					isFlashOn = false;
				} else {
					Log.i("info", "torch is turn on!");
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(p);
					isFlashOn = true;
				}
			} else {
				Toast.makeText(myContext, "No Camera flash present", Toast.LENGTH_LONG);
				Log.d("No Flash","Camera doesnt have flash");
				}
		}
	};
	
	private boolean hasFlash(){
	    Parameters params = mCamera.getParameters();
	    List<String> flashModes = params.getSupportedFlashModes();
	    if(flashModes == null) {
	        return false;
	    } else {
	    	return true;
	    }
	}

	OnClickListener switchCameraListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// get the number of cameras
			int camerasNumber = Camera.getNumberOfCameras();
			if (camerasNumber > 1) {
				// release the old camera instance
				// switch camera, from the front and the back and vice versa
				releaseCamera();
				chooseCamera();
			} else {
				Toast toast = Toast.makeText(myContext,
						"No camera",
						Toast.LENGTH_LONG);
				toast.show();
			}
		}
	};

	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(safeToTakePicture==true){
				mCamera.takePicture(null, null, mPicture);
				safeToTakePicture=false;
			}					}
	};
	
	public void zoomCamera(boolean zoomInOrOut) {
		if (mCamera != null) {
			Parameters parameter = mCamera.getParameters();

			if (parameter.isZoomSupported()) {
				int MAX_ZOOM = parameter.getMaxZoom();
				int currnetZoom = parameter.getZoom();
				if (zoomInOrOut && (currnetZoom < MAX_ZOOM && currnetZoom >= 0)) {
					parameter.setZoom(currnetZoom + 4);
				} else if (!zoomInOrOut
						&& (currnetZoom <= MAX_ZOOM && currnetZoom > 0)) {
					parameter.setZoom(currnetZoom - 4);
				}
			} else
				Toast.makeText(myContext, "Zoom Not Avaliable",
						Toast.LENGTH_LONG).show();

			mCamera.setParameters(parameter);
		}
	}


	@SuppressLint("NewApi")
	public void chooseCamera() {
		// if the camera preview is the front
		if (cameraFront) {
			int cameraId = findBackFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview

				mCamera = Camera.open(cameraId);
				mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera);
			}
		} else {
			int cameraId = findFrontFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview

				mCamera = Camera.open(cameraId);
				mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera);
			}
		}
	}

	private PictureCallback getPictureCallback() {
		PictureCallback picture = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				// make a new picture file
				File pictureFile = getOutputMediaFile(mediaStorageDir,
						photoCount);

				if (pictureFile == null) {
					return;
				}
				try {
					// write the file
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.flush();
					fos.close();
					Toast toast = Toast.makeText(myContext, "Picture saved: "
							+ pictureFile.getName(), Toast.LENGTH_LONG);
					toast.show();
					photoCount++;
					
					MediaScannerConnection.scanFile(myContext, new String[] {pictureFile.getPath()}, null, null);

				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}

				// refresh camera to continue preview
				mPreview.refreshCamera(mCamera);
				safeToTakePicture=true;
			}
		};
		return picture;
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}


	private static File getOutputMediaFile(File folder, int count) {

		File imageFile;
		String imageFileName = prefix + "_" + count + ".jpg";
		
		imageFile = new File(folder, imageFileName);
		return imageFile;
	}

	private void releaseCamera() {
		// stop and release camera
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void onResume() {
		super.onResume();
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext,
					"Sorry, your phone does not have a camera!",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		if (mCamera == null) {
			// if the front facing camera does not exist
			if (findFrontFacingCamera() < 0) {
				Toast.makeText(this, "No front facing camera found.",
						Toast.LENGTH_LONG).show();
				switchCamera.setVisibility(View.GONE);
			}
			mCamera = Camera.open(findBackFacingCamera());
			mPicture = getPictureCallback();
			mPreview.refreshCamera(mCamera);
			safeToTakePicture=true;

		}
	}

	private boolean hasCamera(Context context) {
		// check if the device has camera
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// when on Pause, release camera in order to be used from other
		// applications
		releaseCamera();
	}

	@Override
	public void onBackPressed() 
	{
		//Turn off Flash Light
		try
		{
			Parameters p = mCamera.getParameters();				
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(p);
		}
		catch(Exception e){}
		//Read images count 
		int filesCount =0;
		try
		{
			String path = Wrapper.APP_FOLDERNAME + File.separator + incidentId 	+ File.separator + category + File.separator;
			File mediaStorageDir = new File(path);
			filesCount = mediaStorageDir.list().length;
		}
		catch(Exception e){}
		
		Bundle camData = new Bundle();
		camData.putInt("RESULT", filesCount);
		Intent intent = new Intent();
	    intent.putExtras(camData);
	    setResult(RESULT_OK, intent);
		this.finish();
	}
}

