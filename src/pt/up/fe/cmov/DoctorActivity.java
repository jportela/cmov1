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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorActivity extends ListActivity implements OnClickListener{

    public static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    ArrayList<Item> items = new ArrayList<Item>();
    private final int searchBtnId = Menu.FIRST;
    private final int statsId = Menu.FIRST + 1;

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
        
        appointments = AppointmentOperations.getRemoteServerAllAppointment(this, DoctorOperations.DOCTOR_CONTROLER,LoginActivity.loginDoctor.getId());
		
        if (appointments == null)
        {
        	TextView title = (TextView) findViewById(R.id.nextAppsTitle);
        	title.setTextSize(12.0f);
        	title.setText("No appointments... \nCheck your connection with the server");
        }
        else {
	        
	        int size = 0;
			if(appointments.size() < 4) size = appointments.size(); else size = 3;
			for(int i = 0; i < size;i++){
	
				if(!weekDay.equals(JSONOperations.weekDay.format(appointments.get(i).getDate().getTime()))){
					weekDay = JSONOperations.weekDay.format(appointments.get(i).getDate().getTime());
					items.add(new SectionItem(JSONOperations.weekDay.format(DoctorActivity.appointments.get(i).getDate().getTime())));
				}
	
		        Patient p = PatientOperations.getRemoteServerPatient(this, appointments.get(i).getPatientId());
				items.add(new EntryItem(i,JSONOperations.formatter.format(DoctorActivity.appointments.get(i).getDate().getTime()),p.getName()));			
			}
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
		if (appointments == null) {
			Toast.makeText(DoctorActivity.this, "No appointments in the system...", Toast.LENGTH_LONG).show();
		}
		else {
			Intent k = new Intent(DoctorActivity.this, ListAppointmentActivity.class);
			startActivity(k);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!items.get(position).isSection()){
			int pos = ((EntryItem)items.get(position)).getPos();
			ListAppointmentActivity.p = PatientOperations.getRemoteServerPatient(this, DoctorActivity.appointments.get(pos).getPatientId());
			Intent k = new Intent(DoctorActivity.this, PatientViewActivity.class);
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
	        	Intent k = new Intent(DoctorActivity.this, LoginActivity.class);
				startActivity(k);
	        break;
	        case statsId:
	        	Intent inte = new Intent(DoctorActivity.this, StatisticsActivity.class);
				startActivity(inte);
	        break;
	    }
	    return true;
	}
	
}
