package pt.up.fe.cmov;

import java.util.ArrayList;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MakeNewAppointmentActivity extends ListActivity {
	
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Doctor> docs = new ArrayList<Doctor>();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		docs = DoctorOperations.queryInnerJoinDoctorSpeciality();
		items.add(new SectionItem("Specialities"));

		for(int i = 0; i < docs.size();i++){
			items.add(new EntryItem(i,docs.get(i).getName(),""));			
		}
		
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		/*if(!items.get(position).isSection()){
			int pos = ((EntryItem)items.get(position)).getPos();
			p = PatientOperations.getRemoteServerPatient(DoctorActivity.appointments.get(pos).getPatientId());
			Intent k = new Intent(ListAppointmentActivity.this, PatientViewActivity.class);
			startActivity(k);
		}*/
	}

}
