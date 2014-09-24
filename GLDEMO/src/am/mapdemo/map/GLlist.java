package am.mapdemo.map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GLlist extends Activity {

	private Button GetLocationB;
	private LocationTracker locTracker;
	private TextView provider, longitude, latitude, accuracy, altitude, bearing, speed, address;
	private Thread thread;
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
	provider.setText("" + locTracker.mgetProvider());	
	longitude.setText("" + locTracker.getLongitude());
	latitude.setText("" + locTracker.getLatitude());
	accuracy.setText("" + locTracker.getAccuracy());
	altitude.setText("" + locTracker.getAltitude());
	bearing.setText("" + locTracker.getBearing());
	speed.setText("" + locTracker.getSpeed());
	address.setText("" + locTracker.getAddress());
	GLlist.this.setProgressBarIndeterminateVisibility(false);
	GetLocationB.setEnabled(true);
	
}
	//connect to provider and update User interface
    private void connectprovider()
    {
    	locTracker = LocationTracker.GetInstance(GLlist.this);
    	locTracker.getLocation();
        myHandler.post(updateRunnable);
    }
    
    //initialization of variables
    private void initofVar(){
    	 GetLocationB = (Button)findViewById(R.id.GetLocation);
         provider = (TextView)findViewById(R.id.valueProvider);
         longitude = (TextView)findViewById(R.id.valueLongitude);
         latitude = (TextView)findViewById(R.id.valueLatitude);
         accuracy = (TextView)findViewById(R.id.valueAccuracy);
         altitude = (TextView)findViewById(R.id.valueAltitude);
         bearing = (TextView)findViewById(R.id.valueBearing);
         speed = (TextView)findViewById(R.id.valueSpeed);
         address = (TextView)findViewById(R.id.valueAddress); 
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gllist);
        initofVar();
        
        GetLocationB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			GLlist.this.setProgressBarIndeterminateVisibility(true);
			GetLocationB.setEnabled(false);
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
					connectprovider();
				}
			});
			thread.start();	
			}
		});
	}

}
