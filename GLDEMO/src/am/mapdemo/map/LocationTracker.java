package am.mapdemo.map;

import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTracker  implements LocationListener {
	private Location mLocation;
		// Declaring a Location Manager
	private LocationManager mLocationManager;
	private static Context mContext;
	private Double longitude, latitude, altitude;
	private Float accuracy, speed, bearing;
	private boolean isGpsenabled = false;// flag for GPS status
	private boolean isNetworkEnabled = false; // flag for network status
	private boolean canGetLocation = false;
	private String provider, addresses, city, country;
	private Integer count_of_providers = 0;
	private static String ProviderName;
			//for getting address of current location
	private static Geocoder mGeocoder;
	private List<Address> maddresses;
				// The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    			// The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 0 minute
    
    private static LocationTracker mLocationTracker;
    public static String GPS = "GPS", NETWORK = "NETWORK";
    
    
    
    
	 public static LocationTracker GetInstance(Context context){
		if(null == mLocationTracker){
			synchronized (LocationTracker.class) {
				  if(null == mLocationTracker){
					  mContext = context;
						mGeocoder = new Geocoder(mContext, Locale.getDefault());
						mLocationTracker = new LocationTracker();
				  }
			}
		}
		return mLocationTracker;
	}
	
    private LocationTracker(){
    	
    }
	
    public static void setProviderName(final String provider){
    	ProviderName = provider; 
    }
    
    public void UseOnlyGPS(){
   
				int count = 10;
				while(null == mLocation && 0 != count){
					--count;
					mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, LocationTracker.this);
					if(null != mLocationManager){
						mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					}
					try {
						Thread.currentThread().sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				stopUsingGPS();
    	
    }
    
    public void UseOnlyNETWORK(){
    	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		if(null != mLocationManager){
			mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
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
		}else{
			
			this.canGetLocation = true;
		if(GPS == ProviderName){
			UseOnlyGPS();
		}else{
			if(NETWORK == ProviderName){
				UseOnlyNETWORK();
			}else{
				// First get location from GPS Provider
				if(isGpsenabled){
					UseOnlyGPS();
				}
				// if Network Enabled get location using Network Services
				if(isNetworkEnabled){
					if(null == mLocation){
						UseOnlyNETWORK();
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
				count_of_providers = mLocationManager.getAllProviders().size();
				try {
					maddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
			}
		}
			
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
		return count_of_providers;
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
