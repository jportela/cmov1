package pt.up.fe.cmov;

import java.util.Calendar;

import pt.up.fe.cmov.display.ScheduleButton;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ScheduleActivity extends Activity {

	public static final int PLANNER_SCHEDULE = 1;
	public static final int APPOINT_SCHEDULE = 2;
    public static final int DATE_DIALOG_ID = 0;
    
    public static final String EXTRA_SCHEDULE_TYPE = "scheduleType";
    public static final String EXTRA_SCHEDULE_START_HOUR = "scheduleStartHour";
    public static final String EXTRA_SCHEDULE_END_HOUR = "scheduleEndHour";
    public static final String EXTRA_SCHEDULE_APPOINTMENT = "scheduleAppointment";

    private int mYear;
    private int mMonth;
    private int mDay;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduleplanner);
        
        int scheduleType = getIntent().getIntExtra(EXTRA_SCHEDULE_TYPE, PLANNER_SCHEDULE);
        
        buildTableLayout(scheduleType);
        
        //linear.addView(text);
	}
	
	private void buildTableLayout(int scheduleType) {
		
		switch(scheduleType) {
		case PLANNER_SCHEDULE:
            buildPlanner();
            break;
		case APPOINT_SCHEDULE:
			buildAppoint();
			break;
		default:
			break;
		}
	}
	
	private void buildPlanner() {
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
		
		showDialog(DATE_DIALOG_ID);
		
		Log.i("DATE", "Day: " + Integer.toString(mDay));
		
	}
	
	private void buildAppoint() {
		// TODO Auto-generated method stub
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	      new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, 
                              int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            
            buildPlannerTable();
        }

    };
    
    private void buildPlannerTable() {
        TableLayout scheduleTable = (TableLayout)findViewById(R.id.scheduleTable);

        int startTime = getIntent().getIntExtra(EXTRA_SCHEDULE_START_HOUR, 0);
        int endTime = getIntent().getIntExtra(EXTRA_SCHEDULE_END_HOUR, 24);
    	TableRow row = null;

        for (int i=startTime; i < (endTime-startTime)*2; i++) {
        	if (i%2==0)
        		row = new TableRow(this);
        	
        	row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        	
        	ScheduleButton fullHour = new ScheduleButton(this, i);
        	
        	fullHour.setLayoutParams(new TableRow.LayoutParams(i % 2));	//android:layout_column="0/1"
        	fullHour.setPadding(10, 10, 10, 10);
        	fullHour.setGravity(Gravity.CENTER);
        	fullHour.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);        	
        	
        	row.addView(fullHour);
        	
        	if (i % 2 == 1)
        		scheduleTable.addView(row);
        }
        
        
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this,
	                    mDateSetListener,
	                    mYear, mMonth, mDay);
	    }
	    return null;
	}
}
