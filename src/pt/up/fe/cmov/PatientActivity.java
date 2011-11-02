package pt.up.fe.cmov;

import java.util.ArrayList;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.operations.SpecialityOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RemoteSync;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PatientActivity extends ListActivity {
	
	public static ArrayList<Appointment> appointmentsPatients  = new ArrayList<Appointment>();
	private ArrayList<Item> items = new ArrayList<Item>();
	static public Doctor doc;
	static public Appointment selectedAppointment;
	static public int positionSelected = -1;
	private static int tempPosition = -1;
	private final int searchBtnId = Menu.FIRST;
	private final int refreshId = Menu.FIRST + 1;

	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.patientview);
		
		View header = getLayoutInflater().inflate(R.layout.header, null);
		ListView listView = getListView();
		listView.addHeaderView(header);
		
		listItems();
		
		Button newAppointment = (Button) findViewById(R.id.newAppointment);
		newAppointment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent k = new Intent(PatientActivity.this, MakeNewAppointmentActivity.class);
    			startActivityForResult(k,2);
            }
        });
	}
	
	public void listItems(){
		appointmentsPatients = AppointmentOperations.getRemoteServerAllAppointment(this, PatientOperations.PATIENT_CONTROLER,LoginActivity.loginPatient.getId());
		
		if (appointmentsPatients == null) {
			appointmentsPatients = AppointmentOperations.queryAppointments(this, "patient_id = ?", new String[]{"" + LoginActivity.loginPatient.getId()}, null);
		}
		
		items = new ArrayList<Item>();
		String weekDay = new String();
		for(int i = 0; i < appointmentsPatients.size();i++){
			if(!weekDay.equals(JSONOperations.weekDay.format(appointmentsPatients.get(i).getDate().getTime()))){
				weekDay = JSONOperations.weekDay.format(appointmentsPatients.get(i).getDate().getTime());
				items.add(new SectionItem(JSONOperations.weekDay.format(appointmentsPatients.get(i).getDate().getTime())));
			}

	        Doctor doc = DoctorOperations.getRemoteServerDoctor(this,appointmentsPatients.get(i).getDoctorId());
	        
	        if (doc == null) {
	        	doc = DoctorOperations.getDoctor(this, appointmentsPatients.get(i).getDoctorId());
	        }
	        
			items.add(new EntryItem(i,doc.getName(),JSONOperations.formatter.format(appointmentsPatients.get(i).getDate().getTime()) + "\n" 
					  + "\n" + SpecialityOperations.getSpeciality(this,doc.getSpeciality().getId()).getName()));			
		}

		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}
	
	public static int isToDelete(){
		return positionSelected = tempPosition;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!items.get(position-1).isSection()){
			int pos = ((EntryItem)items.get(position-1)).getPos();
			PatientActivity.doc = DoctorOperations.getRemoteServerDoctor(this,appointmentsPatients.get(pos).getDoctorId());
			PatientActivity.selectedAppointment = appointmentsPatients.get(pos);
			tempPosition = position - 1;
			Intent k = new Intent(PatientActivity.this, DoctorViewActivity.class);
			startActivityForResult(k,0);
		}
	}
	
	public void itemsList(){
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(positionSelected != -1){
			appointmentsPatients = AppointmentOperations.getRemoteServerAllAppointment(this, PatientOperations.PATIENT_CONTROLER,LoginActivity.loginPatient.getId());
			itemsList();
			EntryAdapter adapter = new EntryAdapter(this, items);
			setListAdapter(adapter);
			positionSelected = -1;
		}
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem searchMItm = menu.add(Menu.NONE,searchBtnId ,searchBtnId,"Logout");
	    searchMItm.setIcon(R.drawable.logout);
	    MenuItem refreshMenu = menu.add(Menu.NONE,refreshId ,refreshId,"Refresh");
	    refreshMenu.setIcon(R.drawable.refresh);
	    return super.onCreateOptionsMenu(menu);
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case searchBtnId:
	        	finish();
	        	LoginActivity.loginPatient = null;
	        	Intent k = new Intent(PatientActivity.this, LoginActivity.class);
				startActivity(k);
	        break;
	        case refreshId:
	        	Toast.makeText(this, "Refreshing please wait", Toast.LENGTH_LONG).show();
	            listItems();	      
	        	RemoteSync.oneClickSync(this, null);
	        break;
	    }
	    return true;
	}
}
