package am.mapdemo.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
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
			
			return true;
		case R.id.action_location_found_by_Network:
			return true;
			
		case R.id.action_list_val:
			return true;
		case R.id.action_help:
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
}
