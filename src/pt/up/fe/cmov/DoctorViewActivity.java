package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.Calendar;

import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import pt.up.fe.cmov.operations.SpecialityOperations;
import android.app.ListActivity;
import android.os.Bundle;


public class DoctorViewActivity extends ListActivity {

	ArrayList<Item> items = new ArrayList<Item>();
	
	long millis = Calendar.getInstance().getTime().getTime() - PatientActivity.doc.getBirthDate().getTime();
	int year   = (int) ((millis / (1000*60*60*24*30)) % 12) * -1;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		items.add(new SectionItem(PatientActivity.doc.getName()));
			
		items.add(new EntryItem(0,"Speciality",SpecialityOperations.getSpeciality(this,PatientActivity.doc.getSpeciality().getId()).getName()));
		items.add(new EntryItem(0,"Age",Integer.toString(year) + " years old"));
		 
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
		
	}
}
