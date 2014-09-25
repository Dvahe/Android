package am.mapdemo.map;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public abstract class MH  {

	private LocationTracker locTracker;
	private Context mContext;
    //create an handler
private final Handler myHandler = new Handler();
 
final Runnable updateRunnable = new Runnable() {
    public void run() {
        //call the activity method that updates the UI
        updateUI();
    }
};

public MH(Context context){
	mContext = context;
	new Thread(new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			connectprovider();
		}
	}).start();
}

// ... update the UI 
protected abstract  void updateUI();

	//connect to provider and update User interface
    private void connectprovider()
    {
    	locTracker = LocationTracker.GetInstance(mContext);
    	locTracker.getLocation();
        myHandler.post(updateRunnable);
    }
}
