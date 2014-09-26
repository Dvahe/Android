package am.mapdemo.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity
{
	private Button GetLocationB;
	private LocationTracker locTracker;
	private TextView provider, longitude, latitude;
	private GoogleMap mGoogleMap;
    //create an handler
private final Handler myHandler = new Handler();
 
final Runnable updateRunnable = new Runnable() {
    public void run() {
        //call the activity method that updates the UI
        updateUI();
    }
};

// ... update the UI 
private void updateUI()
{
	if(locTracker.getCanGetLocation()){
		provider.setText("" + locTracker.mgetProvider());	
		longitude.setText("" + locTracker.getLongitude());
		latitude.setText("" + locTracker.getLatitude());
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		LatLng location = new LatLng(locTracker.getLatitude(), locTracker.getLongitude() );
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 19);
		mGoogleMap.animateCamera(update);
		mGoogleMap.addMarker(new MarkerOptions().position(location).title("Accurancy: " + locTracker.getAccuracy() + "m"));
		mGoogleMap.addCircle(new CircleOptions().center(location).radius(locTracker.getAccuracy()).fillColor(0x90000000).strokeWidth(3));
		
	}else{
		 showSettingsAlert();
	}
	MainActivity.this.setProgressBarIndeterminateVisibility(false);
	GetLocationB.setEnabled(true);
}

/**
 * Function to show settings alert dialog
 * */
private void showSettingsAlert(){
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
  
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
            MainActivity.this.startActivity(intent);
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
	//connect to provider and update User interface
    private void connectprovider()
    {
    	locTracker = LocationTracker.GetInstance(MainActivity.this);
    	locTracker.getLocation();
         myHandler.post(updateRunnable);
    }
    
    //initialization of variables
    private void initofVar(){
    	 GetLocationB = (Button)findViewById(R.id.getlocation);
         provider = (TextView)findViewById(R.id.valueProvider);
         longitude = (TextView)findViewById(R.id.valueLongitude);
         latitude = (TextView)findViewById(R.id.valueLatitude);
         mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		 requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initofVar();
       
        GetLocationB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.setProgressBarIndeterminateVisibility(true);
				GetLocationB.setEnabled(false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
					connectprovider();
				}
			}).start();
				
			}
		});
	}

	//change the action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_location_found_by_GPS:
			LocationTracker.setProviderName(LocationTracker.NETWORK);
			return true;
		case R.id.action_location_found_by_Network:
			LocationTracker.setProviderName(LocationTracker.NETWORK);
			return true;
			
		case R.id.action_list_val:
			Intent intentgll = new Intent(this, GLlist.class);
			startActivity(intentgll);
			return true;
		case R.id.action_help:
			Intent intenthelp = new Intent(this, Help.class);
			startActivity(intenthelp);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
}
