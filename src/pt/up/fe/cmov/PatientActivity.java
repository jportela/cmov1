package pt.up.fe.cmov;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Speciality;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PatientActivity extends ListActivity {
	
	ArrayList<Appointment> appointmentsPatients  = new ArrayList<Appointment>();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.patientview);
		
		try {
			appointmentsPatients = AppointmentOperations.getRemoteServerAllAppointment(PatientOperations.PATIENT_CONTROLER,LoginActivity.loginPatient.getId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> nameApp = new ArrayList<String>();
		
		for(int i = 0; i < appointmentsPatients.size();i++){
	        Doctor doc = DoctorOperations.getRemoteServerDoctor(this,appointmentsPatients.get(i).getDoctorId());
			nameApp.add(JSONOperations.completeDate.format(appointmentsPatients.get(i).getDate().getTime()) + "\n" 
						+ doc.getName() + "\n" + Speciality.Records.get(doc.getSpeciality().getId()));	 
		}

		View header = getLayoutInflater().inflate(R.layout.header, null);
		ListView listView = getListView();
		listView.addHeaderView(header);
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, nameApp));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		/*Intent k = new Intent(PatientActivity.this, PatientViewActivity.class);
		startActivity(k);*/
	}

}
