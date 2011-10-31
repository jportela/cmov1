package pt.up.fe.cmov;

import java.util.ArrayList;

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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	docs = DoctorOperations.queryInnerJoinDoctorSpeciality();

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

    /**
     * A simple adapter which maintains an ArrayList of photo resource Ids. 
     * Each photo is displayed as an image. This adapter supports clearing the
     * list of photos and adding a new photo.
     *
     */
    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    
        private ArrayList<String> groups = getGroups();
        public ArrayList<ArrayList<Doctor>> children = getChildren();
        
        public ArrayList<String> getGroups(){
        	ArrayList<String> specs = new  ArrayList<String>();
        	for(int i = 0; i < docs.size();i++){
    			specs.add(docs.get(i).getUsername());			
    		}
        	return specs;
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
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(MakeNewAppointmentActivity.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(50, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public Object getGroup(int groupPosition) {
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
            textView.setText(getGroup(groupPosition).toString());
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