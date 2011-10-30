package pt.up.fe.cmov;


import java.util.ArrayList;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class DoctorActivity extends ListActivity implements OnClickListener{

    public static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    ArrayList<Item> items = new ArrayList<Item>();

	private OnClickListener scheduleButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Doctor doctor = LoginActivity.loginDoctor;
			int doctorId = doctor.getId();
			Intent scheduleIntent = new Intent(DoctorActivity.this, ScheduleActivity.class);
			scheduleIntent.putExtra(ScheduleActivity.EXTRA_SCHEDULE_TYPE, ScheduleActivity.VIEW_SCHEDULE);
			scheduleIntent.putExtra(ScheduleActivity.EXTRA_SCHEDULE_DOCTOR, doctorId);
			startActivity(scheduleIntent);
		}
	};
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorview);
        String weekDay = new String("");
        
        appointments = AppointmentOperations.getRemoteServerAllAppointment(DoctorOperations.DOCTOR_CONTROLER,LoginActivity.loginDoctor.getId());
		int size = 0;
		if(appointments.size() < 4) size = appointments.size(); else size = 3;
		for(int i = 0; i < size;i++){

			if(!weekDay.equals(JSONOperations.weekDay.format(appointments.get(i).getDate().getTime()))){
				weekDay = JSONOperations.weekDay.format(appointments.get(i).getDate().getTime());
				items.add(new SectionItem(JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime())));
			}

	        Patient p = PatientOperations.getRemoteServerPatient(appointments.get(i).getPatientId());
			items.add(new EntryItem(i,JSONOperations.formatter.format(DoctorActivity.appointments.get(i).getDate().getTime()),p.getName()));			
		}
		
        
        EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
        
        final Button button = (Button) findViewById(R.id.appointmentsButton);
        button.setOnClickListener(this);
        
        Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
		scheduleButton.setOnClickListener(scheduleButtonListener);
	}

	@Override
	public void onClick(View v) {
		Intent k = new Intent(DoctorActivity.this, ListAppointmentActivity.class);
		startActivity(k);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!items.get(position-1).isSection()){
			int pos = ((EntryItem)items.get(position-1)).getPos();
			ListAppointmentActivity.p = PatientOperations.getRemoteServerPatient(DoctorActivity.appointments.get(pos).getPatientId());
			Intent k = new Intent(DoctorActivity.this, PatientViewActivity.class);
			startActivity(k);
		}
	}
	
}
