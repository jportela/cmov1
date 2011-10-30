package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import pt.up.fe.cmov.display.ScheduleAdapter;
import pt.up.fe.cmov.display.ScheduleButton;
import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Schedule;
import pt.up.fe.cmov.entities.SchedulePlan;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.ScheduleOperations;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class ScheduleActivity extends Activity {

	public static final int PLANNER_SCHEDULE = 1;
	public static final int APPOINT_SCHEDULE = 2;
    public static final int DIALOG_CONFIRM_APPOINTMENT = 1;
    
    public static final String EXTRA_SCHEDULE_TYPE = "scheduleType";
    public static final String EXTRA_SCHEDULE_START_HOUR = "scheduleStartHour";
    public static final String EXTRA_SCHEDULE_END_HOUR = "scheduleEndHour";
    public static final String EXTRA_SCHEDULE_DOCTOR = "scheduleDoctor";
	private static final String EXTRA_SCHEDULE_PATIENT = "schedulePatient";
    
    private int doctorId = -1;
    private int patientId = -1;

    private HashMap<String, ScheduleAdapter> days;
    private ArrayList<String> panelOrder;
        
    private String[] weekdays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
    
    private int selectedPanel = 0;
    private ScheduleButton selectedSchedule = null;
    
    private OnClickListener backButtonListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
	        selectedPanel--;
	        buildGrid();
		}

    };   
    
    private OnClickListener nextButtonListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
	        selectedPanel++;
	        buildGrid();
		}

    };  
    
    private OnClickListener scheduleButtonListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
			selectedSchedule = (ScheduleButton) v;
	        showDialog(DIALOG_CONFIRM_APPOINTMENT);
		}

    }; 
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduleplanner);
        
        int scheduleType = getIntent().getIntExtra(EXTRA_SCHEDULE_TYPE, APPOINT_SCHEDULE);
        //int scheduleType = getIntent().getIntExtra(EXTRA_SCHEDULE_TYPE, PLANNER_SCHEDULE);
        
        days = new HashMap<String, ScheduleAdapter>();
        panelOrder = new ArrayList<String>();
        
        Button backButton = (Button) findViewById(R.id.backScheduleButton);
		Button nextButton = (Button) findViewById(R.id.nextScheduleButton);

		backButton.setOnClickListener(backButtonListener);
		nextButton.setOnClickListener(nextButtonListener);
		
        buildTableLayout(scheduleType);
        
        
        //linear.addView(text);
	}
	
	private void buildTableLayout(int scheduleType) {
		
		switch(scheduleType) {
		case PLANNER_SCHEDULE:
            break;
		case APPOINT_SCHEDULE:
			buildAppoint();
			buildGrid();
			break;
		default:
			break;
		}
	}
	
	private void buildAppoint() {
		doctorId = getIntent().getIntExtra(EXTRA_SCHEDULE_DOCTOR, 0);
		patientId = getIntent().getIntExtra(EXTRA_SCHEDULE_PATIENT, 0);
		
		patientId = 2;
		doctorId = 4; //TODO: Dummy
		
		SchedulePlan plan = DoctorOperations.getRemoteCurrentPlan(this, doctorId);
		
		ArrayList<Schedule> schedules = ScheduleOperations.getRemoteSchedules(this, plan.getId());
		
		ArrayList<Appointment> appointments = AppointmentOperations.getRemoteServerAllAppointment(DoctorOperations.DOCTOR_CONTROLER, doctorId);
		
		HashMap<Integer, ArrayList<Appointment>> scheduleAppointments = new HashMap<Integer, ArrayList<Appointment>>();

		for (int i=0; i < appointments.size(); i++) {
			Appointment appointment = appointments.get(i);
			int scheduleId = appointment.getScheduleId();
			
			if (scheduleAppointments.containsKey(scheduleId)) {
				ArrayList<Appointment> apps = scheduleAppointments.get(scheduleId);
				apps.add(appointment);
			}
			else {
				ArrayList<Appointment> apps = new ArrayList<Appointment>();
				apps.add(appointment);
				scheduleAppointments.put(scheduleId, apps);
			}
		}
		
		for (int i=0; i < schedules.size(); i++) {
			Schedule schedule = schedules.get(i);
			Date startDate = schedule.getStartDate();	
			Date endDate = schedule.getEndDate();
			
			ArrayList<Appointment> apps = scheduleAppointments.get(schedule.getId());
			
			if (apps == null) {
				apps = new ArrayList<Appointment>();
			}

			
			long blocks = (endDate.getTime() - startDate.getTime()) / (1000*60*30);
			
			Calendar c1 = Calendar.getInstance();
			c1.setTime(startDate);

			for (int j=0; j < blocks; j++) {
				Appointment selectedApp = null;
				for (int k=0; k < apps.size(); k++) {
					Appointment app = apps.get(k);
					long appBlock = blocks - ((endDate.getTime() - app.getDate().getTime()) / (1000*60*30));
					if (appBlock == j) {
						selectedApp = app;
						break;
					}
				}
				
				
				
				int id = c1.get(Calendar.HOUR) * 2 + c1.get(Calendar.MINUTE)/30;
				ScheduleButton button = null;
				if (selectedApp == null) {
					button = new ScheduleButton(this, id, schedule.getId(), (Date) c1.getTime().clone());
					button.setOnClickListener(scheduleButtonListener);
				}
				else
					button = new ScheduleButton(this, id, schedule.getId(), (Date) c1.getTime().clone(), selectedApp);
				
				String label = weekdays[c1.get(Calendar.DAY_OF_WEEK) - 1] + " - " + c1.get(Calendar.DAY_OF_MONTH) + "/" + (c1.get(Calendar.MONTH) + 1);
				
				if (!panelOrder.contains(label))
				{
					panelOrder.add(label);
				}
				
				if (days.containsKey(label)) {
					ScheduleAdapter adapter = days.get(label);
					adapter.addSchedule(button);
				}
				else {
					ScheduleAdapter adapter = new ScheduleAdapter(this);
					adapter.addSchedule(button);
					days.put(label, adapter);
				}
				
				c1.add(Calendar.MINUTE, 30);
				
			}
		}	
	}
	
	private void buildGrid() {
		String label = panelOrder.get(selectedPanel);
		ScheduleAdapter adapter = days.get(label);
		GridView scheduleTable = (GridView)findViewById(R.id.scheduleTable);
		scheduleTable.setAdapter(adapter);
		
		TextView scheduleLabel = (TextView) findViewById(R.id.scheduleLabel);
		scheduleLabel.setText(label);
		
		Button backButton = (Button) findViewById(R.id.backScheduleButton);
		Button nextButton = (Button) findViewById(R.id.nextScheduleButton);

		if (selectedPanel == 0) {
			backButton.setVisibility(View.INVISIBLE);
		}
		else {
			backButton.setVisibility(View.VISIBLE);
		}
		
		if (selectedPanel >= panelOrder.size() - 1) {
			nextButton.setVisibility(View.INVISIBLE);
		}
		else {
			nextButton.setVisibility(View.VISIBLE);
		}		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    switch(id) {
	    case DIALOG_CONFIRM_APPOINTMENT:
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("You are scheduling an appointment at " + selectedSchedule.getDate().toString() + ". Schedule this appointment?")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                scheduleAppointment();
	    	           }
	    	       })
	    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    	dialog = builder.create();
	    	break;
	    default:
	        break;
	    }
	    return dialog;
	}
	
	@Override
	protected void onPrepareDialog (int id, Dialog dialog, Bundle args) {
		AlertDialog alertDialog = (AlertDialog) dialog;
		alertDialog.setMessage("You are scheduling an appointment at " + selectedSchedule.getDate().toString() + ". Schedule this appointment?");
	}
	
	private void scheduleAppointment() {
		Appointment appointment = new Appointment(-1, patientId, selectedSchedule.getScheduleId(), doctorId, selectedSchedule.getDate());
		
		AppointmentOperations.createAppointment(this, appointment);
		
		selectedSchedule.setAppointment(appointment);
		selectedSchedule.toggleState();
		selectedSchedule.setOnClickListener(null);
		
	}
	
    
//    private void buildPlannerTable() {
//        TableLayout scheduleTable = (TableLayout)findViewById(R.id.scheduleTable);
//
//        int startTime = getIntent().getIntExtra(EXTRA_SCHEDULE_START_HOUR, 0);
//        int endTime = getIntent().getIntExtra(EXTRA_SCHEDULE_END_HOUR, 24);
//    	TableRow row = null;
//
//        for (int i=startTime; i < (endTime-startTime)*2; i++) {
//        	if (i%2==0)
//        		row = new TableRow(this);
//        	
//        	row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        	
//        	ScheduleButton fullHour = new ScheduleButton(this, i);
//        	
//        	fullHour.setLayoutParams(new TableRow.LayoutParams(i % 2));	//android:layout_column="0/1"
//        	fullHour.setPadding(10, 10, 10, 10);
//        	fullHour.setGravity(Gravity.CENTER);
//        	fullHour.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);        	
//        	
//        	row.addView(fullHour);
//        	
//        	if (i % 2 == 1)
//        		scheduleTable.addView(row);
//        }
//	}
    
    /*private void buildTable() {
        GridView scheduleTable = (GridView)findViewById(R.id.scheduleTable);

        int startTime = getIntent().getIntExtra(EXTRA_SCHEDULE_START_HOUR, 0);
        int endTime = getIntent().getIntExtra(EXTRA_SCHEDULE_END_HOUR, 24);

        ScheduleAdapter adapter = new ScheduleAdapter(this);
        
        for (int i=startTime; i < (endTime-startTime)*2; i++) {
        	        	        	
        	ScheduleButton fullHour = new ScheduleButton(this, i);
        	
        	fullHour.setPadding(10, 10, 10, 10);
        	fullHour.setGravity(Gravity.CENTER);
        	fullHour.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);        	
        	
        	adapter.addSchedule(fullHour);
        }
        
        scheduleTable.setAdapter(adapter);
	}*/
	
}
