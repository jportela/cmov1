package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.Calendar;

import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.SpecialityOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


public class DoctorViewActivity extends ListActivity {

	ArrayList<Item> items = new ArrayList<Item>();
	
	long millis = Calendar.getInstance().getTime().getTime() - PatientActivity.doc.getBirthDate().getTime();
	int year   = (int) ((millis / (1000*60*60*24*30)) % 12) * -1;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		items.add(new SectionItem(PatientActivity.doc.getName()));
			
		items.add(new EntryItem(0,"Speciality",SpecialityOperations.getSpeciality(this,PatientActivity.doc.getSpeciality().getId()).getName()));
		items.add(new EntryItem(0,"Age",Integer.toString(year) + " years old"));
		
		items.add(new SectionItem(JSONOperations.weekDay.format(PatientActivity.selectedAppointment.getDate())));
		items.add(new EntryItem(5,"Cancel Appointment","Press here if you which to cancel this appointment!"));
		 
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int pos = ((EntryItem)items.get(position)).getPos();
		if(pos == 5){
				//Toast.makeText(DoctorViewActivity.this, "Pressed " + Integer.toString(pos), Toast.LENGTH_LONG);
				new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Delete Appointment")
		        .setMessage("Do you wish to cancel " + JSONOperations.weekDay.format(PatientActivity.selectedAppointment.getDate()))
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	AppointmentOperations.deleteAppointment(DoctorViewActivity.this, PatientActivity.selectedAppointment);
		                DoctorViewActivity.this.finish();
		                
		            }
		        })
		        .setNegativeButton("No", null)
		        .show();
		}
	}
}
