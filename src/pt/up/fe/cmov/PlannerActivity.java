package pt.up.fe.cmov;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class PlannerActivity extends Activity {
		
	private OnClickListener newRowListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
	        addPlannerRow();        
		}

    };
    
    private OnClickListener eraseRowListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
	        View row = (View) v.getParent(); 
	        LinearLayout parent = (LinearLayout)row.getParent();
	        if (parent.getChildCount() > 1)
	        	parent.removeView(row);
		}

    };
    
    private OnClickListener callDatePickerListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);
	        
	        showDialog(DATE_DIALOG_ID);
		}

    };
    
    private OnClickListener confirmListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mYear == 0)	//ERROR
				return;
			Date date = new Date(mYear, mMonth, mDay);
			
			Log.i("DATE", "" + date.toString());
			
			int WEEKDAY_POS = 0;
			int START_HOUR_POS = 1;
			int END_HOUR_POS = 3;

			LinearLayout plannerRows = (LinearLayout) findViewById(R.id.plannerRows);

			for (int i = 0; i < plannerRows.getChildCount(); i++) {
				LinearLayout row = (LinearLayout) plannerRows.getChildAt(i);
				
				Spinner weekdaySpinner = (Spinner)row.getChildAt(WEEKDAY_POS);
				int weekday = (int) weekdaySpinner.getSelectedItemId();
				Log.i("WEEK", "" + weekday);
				
			}
		}

    };
    

    public static final int DATE_DIALOG_ID = 0;

    private int mYear = 0, mMonth = 0, mDay = 0;
    
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	      new DatePickerDialog.OnDateSetListener() {

      public void onDateSet(DatePicker view, int year, 
                            int monthOfYear, int dayOfMonth) {
          mYear = year;
          mMonth = monthOfYear;
          mDay = dayOfMonth;
          
          Button b = (Button) findViewById(R.id.datePickerButton);
          String text = "" + mDay + "/" + mMonth+1 + "/" + mYear;
          b.setText(text);
      }

  };
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner);
        
        Button b = (Button) findViewById(R.id.datePickerButton);
        b.setOnClickListener(callDatePickerListener);
        
        Button confirm = (Button) findViewById(R.id.confirmButton);
        confirm.setOnClickListener(confirmListener);
        
        addPlannerRow();        
        

        
	}
	
	public void addPlannerRow() {
		LinearLayout plannerRow = new LinearLayout(this);
		
		plannerRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		plannerRow.setPadding(0, 10, 0, 10);
		
		Spinner weekdaySpinner = new Spinner(this);
		weekdaySpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		weekdaySpinner.setPromptId(R.string.weekdays_prompt);
	    weekdaySpinner.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.weekdays_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    weekdaySpinner.setAdapter(adapter);
	    
	    Spinner startHourSpinner = new Spinner(this);
	    startHourSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    startHourSpinner.setPromptId(R.string.hours_prompt);
	    startHourSpinner.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));

		ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(
	            this, R.array.hours_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    startHourSpinner.setAdapter(hourAdapter);
	    
	    TextView toLabel = new TextView(this);
	    toLabel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
	    toLabel.setText(R.string.plannerToLabel);
	    toLabel.setGravity(Gravity.CENTER_VERTICAL);
	    toLabel.setPadding(10, 0, 10, 0);
	    toLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
	    
	    Spinner endHourSpinner = new Spinner(this);
	    endHourSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    endHourSpinner.setPromptId(R.string.hours_prompt);
	    endHourSpinner.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
	    
	    endHourSpinner.setAdapter(hourAdapter);
	    
	    Button addRowButton = new Button(this);
	    addRowButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    addRowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.addbutton));
	    addRowButton.setOnClickListener(newRowListener);
	    
	    Button eraseRowButton = new Button(this);
	    eraseRowButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    eraseRowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.deletebutton));
	    eraseRowButton.setOnClickListener(eraseRowListener);

	    plannerRow.addView(weekdaySpinner);
	    plannerRow.addView(startHourSpinner);
	    plannerRow.addView(toLabel);
	    plannerRow.addView(endHourSpinner);
	    plannerRow.addView(addRowButton); 
	    plannerRow.addView(eraseRowButton);
	    
        LinearLayout plannerRows = (LinearLayout) findViewById(R.id.plannerRows);
        plannerRows.addView(plannerRow);
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
