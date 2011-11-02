package pt.up.fe.cmov;

import java.util.ArrayList;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ListAppointmentActivity extends ListActivity {

	static public Patient p = new Patient();
	ArrayList<Item> items = new ArrayList<Item>();
	private final int searchBtnId = Menu.FIRST;
    private final int statsId = Menu.FIRST + 1;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		String weekDay = new String();
		for(int i = 0; i < DoctorActivity.appointments.size();i++){
			if(!weekDay.equals(JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime()))){
				weekDay = JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime());
				items.add(new SectionItem(JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime())));
			}

	        Patient pat = PatientOperations.getRemoteServerPatient(this, DoctorActivity.appointments.get(i).getPatientId());

			
			items.add(new EntryItem(i,JSONOperations.formatter.format(DoctorActivity.appointments.get(i).getDate().getTime()),pat.getName()));			
		}
		
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!items.get(position).isSection()){
			int pos = ((EntryItem)items.get(position)).getPos();
			p = PatientOperations.getRemoteServerPatient(this, DoctorActivity.appointments.get(pos).getPatientId());
			Intent k = new Intent(ListAppointmentActivity.this, PatientViewActivity.class);
			startActivity(k);
		}
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem searchMItm = menu.add(Menu.NONE,searchBtnId ,searchBtnId,"Logout");
	    searchMItm.setIcon(R.drawable.logout);
	    MenuItem statsMenu = menu.add(Menu.NONE,statsId ,statsId,"Statistics");
	    statsMenu.setIcon(R.drawable.stats);
	    return super.onCreateOptionsMenu(menu);
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case searchBtnId:
	        	finish();
	        	LoginActivity.loginDoctor = null;
	        	Intent k = new Intent(ListAppointmentActivity.this, LoginActivity.class);
				startActivity(k);
	        break;
	        case statsId:
	        	Intent inte = new Intent(ListAppointmentActivity.this, StatisticsActivity.class);
				startActivity(inte);
	        break;
	    }
	    return true;
	}
}
