package com.insurance.app;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GetLocation {

	private static final String TAG = "Debug";
	private Boolean flag = false;
	private Context context;

	public GetLocation(Activity myActivity) {
		this.context = myActivity;
	}
	
	public GetLocation(Context context) {
		this.context = context;
	}


	public Location getLocationData() {
		Location myLocation = null;
		flag = displayGpsStatus();
		if (flag) {

			Log.v(TAG, "onClick");
			myLocation = getLastKnownLocation();
//			Log.d("Latitude", "" + myLocation.getLatitude());
//			Log.d("Longitude", "" + myLocation.getLongitude());
		} else {
			Log.d("Gps Status!!", "Your GPS is: OFF");
			
			return myLocation;
		}
		return myLocation;
	}

	/*----Method to Check GPS is enable or disable ----- */
	@SuppressWarnings("deprecation")
	public Boolean displayGpsStatus() {
		ContentResolver contentResolver = context.getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(
				contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {
			return true;

		} else {
			return false;
		}
	}

	private Location getLastKnownLocation() {
		LocationManager locationManager = (LocationManager) context
				.getApplicationContext().getSystemService(
						Context.LOCATION_SERVICE);

		List<String> providers = locationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = locationManager.getLastKnownLocation(provider);
			if (l == null) {
				continue;
			}
			if (bestLocation == null
					|| l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}
		return bestLocation;
	}
}
