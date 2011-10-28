package pt.up.fe.cmov;

import java.util.ArrayList;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListAppointmentActivity extends ListActivity {

	static public Patient p = new Patient();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		ArrayList<String> nameApp = new ArrayList<String>();
		for(int i = 0; i < DoctorActivity.appointments.size();i++){
	        Patient pat = PatientOperations.getRemoteServerPatient(DoctorActivity.appointments.get(i).getPatientId());
			nameApp.add(JSONOperations.completeDate.format(DoctorActivity.appointments.get(i).getDate().getTime()) + "  " + pat.getName());	 
		}
		View header = getLayoutInflater().inflate(R.layout.header, null);
		ListView listView = getListView();
		listView.addHeaderView(header);
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, nameApp));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        p = PatientOperations.getRemoteServerPatient(DoctorActivity.appointments.get(position).getPatientId());
		Intent k = new Intent(ListAppointmentActivity.this, PatientViewActivity.class);
		startActivity(k);
	}
}
