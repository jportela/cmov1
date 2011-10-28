package pt.up.fe.cmov;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PatientViewActivity extends ListActivity {

	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		long millis = Calendar.getInstance().getTime().getTime() - ListAppointmentActivity.p.getBirthDate().getTime();
		int year   = (int) ((millis / (1000*60*60*24*30)) % 12) * -1;
		
		String[] names = new String[] { "Name\n" + ListAppointmentActivity.p.getName(),
		"Address\n" + ListAppointmentActivity.p.getAddress(),"Age\n" + 
				Integer.toString(year) + " years old",
				"Sex\n" + ListAppointmentActivity.p.getSex()};

		View header = getLayoutInflater().inflate(R.layout.patientdetails, null);
		ListView listView = getListView();
		listView.addHeaderView(header);
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names));
		ListAppointmentActivity.p = null;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
	}
}
