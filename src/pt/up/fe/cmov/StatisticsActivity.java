package pt.up.fe.cmov;

import java.util.ArrayList;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.listadapter.EntryAdapter;
import pt.up.fe.cmov.listadapter.EntryItem;
import pt.up.fe.cmov.listadapter.Item;
import pt.up.fe.cmov.listadapter.SectionItem;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class StatisticsActivity extends ListActivity {

	static public Patient p = new Patient();
	ArrayList<Item> items = new ArrayList<Item>();
	private final int searchBtnId = Menu.FIRST;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		items.add(new SectionItem("Statistics from " + LoginActivity.loginDoctor.getName()));
		
		items.add(new EntryItem(1,"Number of Appointments","Check the number of appointments in each month"));
		
		EntryAdapter adapter = new EntryAdapter(this, items);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!items.get(position).isSection()){
			if(((EntryItem)items.get(position)).getPos() == 1){
				Intent k = new Intent(StatisticsActivity.this, ChartActivity.class);
				startActivity(k);
			}
		}
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
