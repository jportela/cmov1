package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.HashMap;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.DoctorOperations;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MakeNewAppointmentActivity extends ExpandableListActivity {

    ExpandableListAdapter mAdapter;
	ArrayList<Doctor> docs; 
	static public Doctor selectedDoctor;
	private final int searchBtnId = Menu.FIRST;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	docs = DoctorOperations.queryInnerJoinDoctorSpeciality(this);

        // Set up our adapter
        mAdapter = new MyExpandableListAdapter();
        getExpandableListView().setOnChildClickListener(this);
        setListAdapter(mAdapter);
        registerForContextMenu(getExpandableListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Sample menu");
        menu.add(0, 0, 0, "le menu");
    }
    
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
            int childPosition, long id) {
    		selectedDoctor = ((MyExpandableListAdapter)mAdapter).children.get(groupPosition).get(childPosition);
    		Log.i("DOO", "DOCTOR ID: " + selectedDoctor.getId());
    		Patient patient = LoginActivity.loginPatient;
            int patientId = patient.getId();
            Intent scheduleIntent = new Intent(MakeNewAppointmentActivity.this, ScheduleActivity.class);
            scheduleIntent.putExtra(ScheduleActivity.EXTRA_SCHEDULE_TYPE, ScheduleActivity.APPOINT_SCHEDULE);
            scheduleIntent.putExtra(ScheduleActivity.EXTRA_SCHEDULE_DOCTOR, selectedDoctor.getId());
            scheduleIntent.putExtra(ScheduleActivity.EXTRA_SCHEDULE_PATIENT, patientId);
            startActivity(scheduleIntent);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return false;
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
	        	LoginActivity.loginPatient = null;
	        	finish();
	        	startActivity(intent);
	        break;
	    }
	    return true;
	}
    
    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    
        private ArrayList<String> groups = getGroups();
        public ArrayList<ArrayList<Doctor>> children = getChildren();
        
        public ArrayList<String> getGroups(){
        	HashMap<String,String> specs = new  HashMap<String,String>();
        	ArrayList<String> spectls = new ArrayList<String>();
        	for(int i = 0; i < docs.size();i++){
        		if(!specs.containsKey(docs.get(i).getUsername())){
        			specs.put(docs.get(i).getUsername(),docs.get(i).getUsername());
        			spectls.add(docs.get(i).getUsername());
    			}			
    		}
        	return spectls;
        }
        
        public ArrayList<ArrayList<Doctor>> getChildren(){
        	int j = -1; String specName = new String();
        	ArrayList<ArrayList<Doctor>> doctors = new ArrayList<ArrayList<Doctor>>();
        	ArrayList<Doctor> docts;
        	for(int i = 0; i < docs.size();i++){
        		if(!specName.equals(docs.get(i).getUsername())){
        			specName = docs.get(i).getUsername();
        			j++;
        			docts = new ArrayList<Doctor>();
        			doctors.add(docts);
        		}
        		
        		if(specName.equals(docs.get(i).getUsername()))
        			doctors.get(j).add(new Doctor(docs.get(i).getId(),docs.get(i).getName(),null,null,null,null,null));			
    		}
        	return doctors;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition).get(childPosition).getName();
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return children.get(groupPosition).size();
        }

        public TextView getGenericView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(MakeNewAppointmentActivity.this);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setPadding(50, 10, 0, 10);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition));
            textView.setTextSize(18);
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }
}