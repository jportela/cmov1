package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.Date;

import pt.up.fe.cmov.display.GraphView;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ChartActivity  extends Activity {
	
	private final int searchBtnId = Menu.FIRST;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<String> months = DoctorOperations.getRemoteDoctorMonthsApps(this, LoginActivity.loginDoctor.getId());
		
		ArrayList<Float> values = new ArrayList<Float>();
		ArrayList<String> horlabels = new ArrayList<String>();
		ArrayList<String> verlabels = new ArrayList<String>();

		for(int i = 0; i < months.size();i++){
			String[] date = months.get(i).split("-");
			Date data = new Date();
			data.setDate(0);
			data.setMonth(Integer.parseInt(date[0]) - 1);
			data.setYear(Integer.parseInt(date[1]) - 1900);
			Log.w(date[1], date[1]);
			horlabels.add(JSONOperations.monthFormater.format(data));
			i++;
			verlabels.add(months.get(i));
			values.add(Float.parseFloat(months.get(i)));
		}
				
		GraphView graphView = new GraphView(this, values, "Doctor Appointments Statistics",horlabels, verlabels, GraphView.LINE);
		setContentView(graphView);
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem searchMItm = menu.add(Menu.NONE,searchBtnId ,searchBtnId,"Logout");
	    searchMItm.setIcon(R.drawable.logout);
	    return super.onCreateOptionsMenu(menu);
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case searchBtnId:
	        	Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	finish();
	        	startActivity(intent);
	        break;
	    }
	    return true;
	}
}