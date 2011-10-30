package pt.up.fe.cmov;


import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;

import pt.up.fe.cmov.display.Display;
import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.AppointmentOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoctorActivity extends Activity implements OnClickListener{

    public static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorview);
        String weekDay = new String("");
        boolean setPad = false;
        
		appointments = AppointmentOperations.getRemoteServerAllAppointment(LoginActivity.loginDoctor.getId());
		int size = 0;
		if(appointments.size() < 6) size = appointments.size(); else size = 5;
		for(int i = 0; i < size;i++){
			LinearLayout linear = (LinearLayout)findViewById(R.id.linearLayout1);
		
			if(!weekDay.equals(JSONOperations.weekDay.format(appointments.get(i).getDate().getTime()))){
				weekDay = JSONOperations.weekDay.format(appointments.get(i).getDate().getTime());
				TextView textWeekDay = Display.displayAppointment(weekDay,20,this,Color.BLUE);
				textWeekDay.setPadding(0, 10, 0, 10);
				View view = new View(this);
				LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.MATCH_PARENT);
				view.setPadding(0, 100, 0, 0);
				view.setLayoutParams(params);
				linear.addView(textWeekDay);
				linear.addView(view);
				setPad=true;
			}
				
	        Patient p = PatientOperations.getRemoteServerPatient(appointments.get(i).getPatientId());
	        TextView text = Display.displayAppointment(" " + JSONOperations.formatter.format(appointments.get(i).getDate().getTime()) + "  " + p.getName(),16,this);
	        if(!setPad){
	        	text.setPadding(0, 15, 0, 15);
	        }
	        linear.addView(text);
	        setPad=false;
		}
        
        final Button button = (Button) findViewById(R.id.appointmentsButton);
        button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent k = new Intent(DoctorActivity.this, ListAppointmentActivity.class);
		startActivity(k);
		
	}
	
}
