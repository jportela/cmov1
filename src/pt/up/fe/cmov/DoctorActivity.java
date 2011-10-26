package pt.up.fe.cmov;

import java.util.Calendar;

import pt.up.fe.cmov.display.Display;
import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
        SpannableStringBuilder displayFormat = Display.styleText(JSONOperations.formatter.format(app.getDate().getTime()) + " with " + p.getName(),Color.BLUE,Typeface.BOLD);
        text.setText(displayFormat);
        text.setTextSize(18);
        text.setTextColor(Color.BLUE);
        text.setPadding(8, 5, 0, 0);
        linear.addView(text);
	}

	@Override
	public void onClick(View v) {
		
		
	}
	
}
