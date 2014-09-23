package am.mapdemo.map;

import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

public class LocationTracker  implements LocationListener {
	private Location mLocation;
		// Declaring a Location Manager
	private LocationManager mLocationManager;
	private Context mContext;
	private Double longitude, latitude, altitude;
	private Float accuracy, speed, bearing;
	private boolean isGpsenabled = false;// flag for GPS status
	private boolean isNetworkEnabled = false; // flag for network status
	private boolean canGetLocation = false;
	private String provider, addresses, city, country;
	private Integer count_of_satelites = 0;
			//for getting address of current location
	private Geocoder mGeocoder;
	private List<Address> maddresses;
				// The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    			// The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 0 minute
 
	public LocationTracker(Context context){
		mContext = context;
		mGeocoder = new Geocoder(mContext, Locale.getDefault());
	}
	
	public Location getLocation(){
		mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		if(null != mLocationManager){
			// getting GPS status
			isGpsenabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			 // getting network status
			isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		
		if(!isGpsenabled && !isNetworkEnabled ){
			// no network provider is enabled
			showSettingsAlert();
		}else{
			
			this.canGetLocation = true;
			// First get location from GPS Provider
			if(isGpsenabled){
				for(int i = 0; i < 10*1000; ++i){
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if(null != mLocationManager){
					mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
			}
			}
			// if Network Enabled get location using Network Services
			if(isNetworkEnabled){
				if(null == mLocation){
					mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					if(null != mLocationManager){
						mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
			}
		}
		if(null != mLocation){
			longitude = mLocation.getLongitude();
			latitude = mLocation.getLatitude();
			altitude = mLocation.getAltitude();
			provider = mLocation.getProvider();
			accuracy = mLocation.getAccuracy();
			speed = mLocation.getSpeed();
			bearing = mLocation.getBearing();
			count_of_satelites = mLocationManager.getAllProviders().size();
			try {
				maddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			stopUsingGPS();
		return mLocation;
	}
	
 	public Double getLongitude(){
		if(null != mLocation){
			longitude = mLocation.getLongitude();
		}
		return longitude;
	}
 					//returns latitude
	public Double getLatitude(){
		if(null != mLocation){
			latitude = mLocation.getLatitude();
		}
		return latitude;
	}
					//returns altitude
	public Double getAltitude(){
		if(null != mLocation){
			altitude = mLocation.getAltitude();
		}
		return altitude;
	}
					//returns true if provider is enabled, other ways returns false
	public boolean getCanGetLocation(){
		return canGetLocation;
	}
					//get provider name
	public String mgetProvider(){
		if( null != mLocation){
			provider = mLocation.getProvider();
		}
		return provider;
	}
					//in best case of gps providers accuracy is 2 meters.Network accuracy is 20 meters
	public Float getAccuracy(){
		if(null != mLocation){
			accuracy = mLocation.getAccuracy();
		}
		return accuracy;
	}
					
	public Float getSpeed(){
		if(null != mLocation){
			speed = mLocation.getSpeed();
		}
		return speed;
	}
	
	public Float getBearing(){
		if(null != mLocation){
			bearing = mLocation.getBearing();
		}
		return bearing;
	}
				//returns address using getFromLocation function and takes as argument  LongLat 's instance 
	public String getAddress(){
		if(null != mLocation){
			try {
				maddresses = mGeocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			addresses = maddresses.get(0).getAddressLine(0);
		}
		return addresses;
	}
					//returns  name of city using getFromLocation function and takes as argument  LongLat 's instance 
	public String getCity(){
		if(null != mLocation){
			try {
				maddresses = mGeocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
			} catch (Exception e) {
				 e.printStackTrace();
			}
			
			city = maddresses.get(0).getAddressLine(1);
		}
		return city;
	}
				//returns  name of country using getFromLocation function and takes as argument  LongLat 's instance 
	public String getCountry(){
		if(null != mLocation){
			try {
				maddresses = mGeocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			country = maddresses.get(0).getAddressLine(2);
		}
		return country;
	}
				//returns count of providers
	public Integer getCountSatelites(){
		return count_of_satelites;
	}
	/**
     * Function to show settings alert dialog
     * */
	private void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }

	/**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(null != mLocationManager){
            mLocationManager.removeUpdates(LocationTracker.this);
        }      
    }
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
