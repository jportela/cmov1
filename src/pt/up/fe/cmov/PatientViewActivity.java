package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.Calendar;

import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PatientViewActivity extends ListActivity {

	ArrayList<Item> items = new ArrayList<Item>();
	long millis = Calendar.getInstance().getTime().getTime() - ListAppointmentActivity.p.getBirthDate().getTime();
	int year   = (int) ((millis / (1000*60*60*24*30)) % 12) * -1;
	private final int searchBtnId = Menu.FIRST;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		items.add(new SectionItem(ListAppointmentActivity.p.getName()));
			
		items.add(new EntryItem(0,"Address",ListAppointmentActivity.p.getAddress()));
		items.add(new EntryItem(0,"Age",Integer.toString(year) + " years old"));
		items.add(new EntryItem(0,"Sex",ListAppointmentActivity.p.getSex()));

		 
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem searchMItm = menu.add(Menu.NONE,searchBtnId ,searchBtnId,"Logout");
	    searchMItm.setIcon(R.drawable.logout);
	    return super.onCreateOptionsMenu(menu);
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case searchBtnId:
	        	Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	finish();
	        	startActivity(intent);
	        break;
	    }
	    return true;
	}
}
