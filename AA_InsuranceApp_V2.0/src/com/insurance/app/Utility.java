package com.insurance.app;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class Utility {
	
	public static List<File> getFiles(String directoryName) throws FileNotFoundException 
	{
		File folder = new File(directoryName);
		Log.e("GETFILES", "FPATH:" + folder.getAbsolutePath());
		List<File> out = null;

		if(folder.exists()){
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String fileName) {
					if(((fileName.endsWith(".jpg")) || (fileName.endsWith(".png"))))
						return true;
					return false;
				}
			};
			out = Arrays.asList(folder.listFiles(filter));
			
			final Map<File, Long> staticLastModifiedTimes = new HashMap<File,Long>();
		    for(final File f : out) {
		        staticLastModifiedTimes.put(f, f.lastModified());
		    }
		   
		    Collections.sort(out, new Comparator<File>() {
		        @Override
		        public int compare(final File f1, final File f2) {
		            return staticLastModifiedTimes.get(f2).compareTo(staticLastModifiedTimes.get(f1));
		        }
		    });
			
			/*Function<File, Long> getLastModified = new Function<File, Long>() {
			    public Long apply(File file) {
			        return file.lastModified();
			    }
			};
			*/			
			
			//out = Ordering.natural().onResultOf(getLastModified).
              //      sortedCopy(out);
					
			//Arrays.sort(out, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
		}else{
			Log.d("Generate image", "getFiles : Folder not exist");
		}
		return out;
	}
	public static List<File> getIncidentFolders(String directoryName) throws FileNotFoundException 
	{
		File folder = new File(directoryName);
		Log.e("GETFILES", "FPATH:" + folder.getAbsolutePath());
		List<File> out = null;

		if(folder.exists()){
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String fileName) {
					if(fileName.startsWith("ID_") && dir.isDirectory())
						return true;
					return false;
				}
			};
			out = Arrays.asList(folder.listFiles(filter));
			
			final Map<File, Long> staticLastModifiedTimes = new HashMap<File,Long>();
		    for(final File f : out) {
		        staticLastModifiedTimes.put(f, f.lastModified());
		    }
		   
		    Collections.sort(out, new Comparator<File>() {
		        @Override
		        public int compare(final File f1, final File f2) {
		            return staticLastModifiedTimes.get(f2).compareTo(staticLastModifiedTimes.get(f1));
		        }
		    });
		}else{
			Log.d("Generate image", "getFiles : Folder not exist");
		}
		return out;
	}
	
	
	public static List<File> getFolders(String directoryName,final boolean isFolder)
			throws FileNotFoundException {
		File folder = new File(directoryName);
		Log.e("GETFILES", "FPATH:" + folder.getAbsolutePath());
		List<File> out = null;

		if (folder.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String fileName) {
					 return new File(dir, fileName).isDirectory();
				}
			};
			out = Arrays.asList(folder.listFiles(filter));
		} else {
			Log.d("Generate image", "getFiles : Folder not exist");
		}
		return out;
	}
	
	@SuppressWarnings("deprecation")
	public static String getImageDate(File file) 
	{
		String dateString = null;
		ExifInterface intf = null;
		
		try {
			Log.e("UTILITY", "dateString:" + file.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.TEMPLATE_DATE_FORMAT_TO);
			dateString=  sdf.format(file.lastModified());
			return dateString;
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			dateString ="";
			return dateString;
		}
	}

	
	public static String convertImageToBase64(final File file) {
		
		FileInputStream fis;
		String encodedImage = null;
		try {
			
		fis = new FileInputStream(file);
		InputStream ims = new BufferedInputStream(fis);
		Bitmap bmp = BitmapFactory.decodeStream(ims);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArrayImage = stream.toByteArray();
		encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodedImage;	
		
	}
	
	/**
	 * It helps to show Please wait or activity indicator
	 * 
	 * @param context
	 * @param message
	 * @param title
	 */
	public static ProgressDialog progDlg;
	public static final void showActivityIndicator(Context context, String message, String title) {
		progDlg = new ProgressDialog(context);

		if (message != null && message.length() > 0) {
			progDlg.setMessage(message);
		} else {
			progDlg.setMessage("Please wait..");
		}
		progDlg.setIndeterminate(true);
		progDlg.setCancelable(true);
		if (title != null && title.length() > 0) {
			progDlg.setTitle(title);
		} else {
			progDlg.setTitle(Constants.STR_APP_NAME);
		}
		progDlg.setCancelable(false);
		progDlg.show();
	}
	public static final void hideActivityIndicator() 
	{
		try
		{
		if (progDlg != null) {
			progDlg.dismiss();
			progDlg.cancel();
			progDlg = null;
		}
		}
		catch(Exception e)
		{
		}
	}
	
	public static String getApplicationFolder(Context context)
	{
		String appPath="";
		try
		{
			if(Constants.IsDebug)
			{
				appPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + Constants.STR_APP_FOLDER;
			}
			else 
			{
				appPath = context.getFilesDir().getAbsolutePath() + File.separator + Constants.STR_APP_FOLDER;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return appPath;
	}
}
