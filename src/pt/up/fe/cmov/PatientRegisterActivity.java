package pt.up.fe.cmov;

import java.text.ParseException;
import java.util.Calendar;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class PatientRegisterActivity extends Activity implements OnClickListener{
	
    private EditText mDateDisplay;
    private Button patientRegisterDB;
    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;
    
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientregister);
        
        mDateDisplay = (EditText) findViewById(R.id.pickDate);
        patientRegisterDB = (Button) findViewById(R.id.patientRegisterDB);

        mDateDisplay.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});
        
        patientRegisterDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String sex = new String();
                String name = ((EditText) findViewById(R.id.patientName)).getText().toString();
                String username = ((EditText) findViewById(R.id.patientRegisterUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.patientRegisterPassword)).getText().toString();
                String birthday = mDateDisplay.getText().toString();
                String address = ((EditText) findViewById(R.id.patientRegisterAddress)).getText().toString();
                RadioButton male = (RadioButton) findViewById(R.id.male);
                RadioButton female = (RadioButton) findViewById(R.id.female);
                if(male.isChecked())
                	sex = "Male";
                else if (female.isChecked())
                	sex = "Female";
                if(name.length() > 0 && sex.length() > 0 && username.length() > 0 && password.length() > 0
                	&& birthday.length() > 0 && address.length() > 0){
	                try {
						PatientOperations.createPatient(PatientRegisterActivity.this, new Patient(-1, name, JSONOperations.dbDateFormaterP.parse(birthday), 
								username,address,sex,password), true);
						Toast.makeText(PatientRegisterActivity.this, "Registration Completed. You may login now!", Toast.LENGTH_LONG).show();
						PatientRegisterActivity.this.finish();
					} catch (ParseException e) {
						Toast.makeText(PatientRegisterActivity.this, "Registration Failed. Please, try again later", Toast.LENGTH_LONG).show();
					}
                }else {
					Toast.makeText(PatientRegisterActivity.this, "Complete all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date (this method is below)
        updateDisplay();
        
    }
	
	private void updateDisplay() {
        mDateDisplay.setText(new StringBuilder().append(mYear).append("-").append(mDay).append("-").append(mMonth + 1));
    }
	
	 // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };
    
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

	@Override
	public void onClick(View v){
		switch(v.getId())
		{
		case R.id.patientRegisterDB:
			Toast.makeText(this, "Registration Completed. You may login now!", Toast.LENGTH_LONG).show();
			this.finish();
		break;
		default:
		throw new RuntimeException("Unknow button ID");
		}
		
	}

}
