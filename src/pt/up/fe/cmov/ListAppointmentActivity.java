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
import android.view.View;
import android.widget.ListView;

public class ListAppointmentActivity extends ListActivity {

	static public Patient p = new Patient();
	ArrayList<Item> items = new ArrayList<Item>();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		String weekDay = new String();
		for(int i = 0; i < DoctorActivity.appointments.size();i++){
			if(!weekDay.equals(JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime()))){
				weekDay = JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime());
				items.add(new SectionItem(JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime())));
			}

	        Patient pat = PatientOperations.getRemoteServerPatient(DoctorActivity.appointments.get(i).getPatientId());

			
			items.add(new EntryItem(i,JSONOperations.formatter.format(DoctorActivity.appointments.get(i).getDate().getTime()),pat.getName()));			
		}
		
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!items.get(position-1).isSection()){
			int pos = ((EntryItem)items.get(position-1)).getPos();
			p = PatientOperations.getRemoteServerPatient(DoctorActivity.appointments.get(pos).getPatientId());
			Intent k = new Intent(ListAppointmentActivity.this, PatientViewActivity.class);
			startActivity(k);
		}
	}
}
