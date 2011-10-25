package pt.up.fe.cmov;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoctorActivity extends Activity implements OnClickListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorview);
        
        LinearLayout linear = (LinearLayout)findViewById(R.id.linearLayout1);
        Appointment app = AppointmentOperations.getRemoteServerAppointment(this, 1);
        Patient p = PatientOperations.getRemoteServerPatient(app.getPatientId());
        TextView text = new TextView(this);
        text.setText("Appointment in: " + JSONOperations.dbDateFormater.format(app.getDate().getTime()) 
        			 + " with the Patient " + p.getName());
        text.setTextSize(18);
        text.setTextColor(Color.BLACK);
        linear.addView(text);
	}

	@Override
	public void onClick(View v) {
		
		
	}
	
}
