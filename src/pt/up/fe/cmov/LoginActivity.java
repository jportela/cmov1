package pt.up.fe.cmov;


import java.text.ParseException;
import java.util.ArrayList;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.PatientOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RailsRestClient;
import pt.up.fe.cmov.rest.RemoteSync;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements Runnable,OnClickListener{
	private String username = new String();
    private String password = new String();
    private ProgressDialog dialog;
    private boolean login = false;
    private Thread thread;
    public static Doctor loginDoctor = null;
    public static Patient loginPatient = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        final Button button = (Button) findViewById(R.id.loginConf);
        final Button register = (Button) findViewById(R.id.registerPatient);
        button.setOnClickListener(this);
        register.setOnClickListener(this);
    }

	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.loginConf:
			dialog = ProgressDialog.show(this, "Loading", "Wait for a few seconds", true,
	                false);
	    	username = ((AutoCompleteTextView) findViewById(R.id.usernameField)).getText().toString();
	        password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
	        
	        thread = new Thread(this);
	        thread.start();
		break;
		case R.id.registerPatient:
			Intent registerPatient = new Intent(LoginActivity.this, PatientRegisterActivity.class);
			startActivity(registerPatient);
		break;
		default:
		throw new RuntimeException("Unknow button ID");
		}
	}

	
	@Override
	public void run() {
			try {
				JSONObject obj = RailsRestClient.Get("system/auth", "user="+username+"&pass="+password);
				
				if (obj == null)
				{
					handleLocalLogin();
				}
				else {
					String logged = obj.getString("logged");
					
					if ("false".equals(logged)) {
						login = false;
						handler.sendEmptyMessage(1);
					}
					else if ("true".equals(logged)){
						login = true;
						String type = obj.getString("type");
						if ("doctor".equals(type)) {
							LoginActivity.loginDoctor = JSONOperations.JSONToDoctor(this, obj.getJSONObject("object"));
						}
						else if ("patient".equals(type)) {
							LoginActivity.loginPatient = JSONOperations.JSONToPatient(obj.getJSONObject("object"));
							PatientOperations.createOrUpdatePatient(this, LoginActivity.loginPatient);
						}
	                    handler.sendEmptyMessage(0);
					}
	    			RemoteSync.oneClickSync(this, LoginActivity.loginPatient);
				}
			}
			catch (ConnectTimeoutException e) {
				handleLocalLogin();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        		dialog.dismiss();
        		
        		if(!login){
        			if(thread != null){
        				Thread runner = thread;
        				thread = null;
        				runner.interrupt();
        			}
        			Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        		}else {
        			if(loginDoctor != null){
	        			Intent k = new Intent(LoginActivity.this, DoctorActivity.class);
	        			if(thread != null){
	        				Thread runner = thread;
	        				thread = null;
	        				runner.interrupt();
	        			}
	        			startActivity(k);
        			}else if(loginPatient != null){
        				Intent k = new Intent(LoginActivity.this, PatientActivity.class);
	        			if(thread != null){
	        				Thread runner = thread;
	        				thread = null;
	        				runner.interrupt();
	        			}
	        			startActivity(k);
        			}
        		}
        }
	};
	
	private void handleLocalLogin() {
		ArrayList<Doctor> doctors = DoctorOperations.queryDoctors(this, "username = ?", new String[] {username}, null);	
		
		if (doctors.isEmpty()) {
		ArrayList<Patient> patients = PatientOperations.queryPatients(this, "username = ?", new String[] {username}, null);	
			if (patients.isEmpty()) {
				login = false;
				handler.sendEmptyMessage(1);
			}
			else if (patients.get(0).getPassword().equals(password)){
				LoginActivity.loginPatient = patients.get(0);
				login = true;
				handler.sendEmptyMessage(0);
			}
			else {
				login = false;
				handler.sendEmptyMessage(1);
			}
		}
		else if (doctors.get(0).getPassword().equals(password)) {
			LoginActivity.loginDoctor = doctors.get(0);
			login = true;
			handler.sendEmptyMessage(0);
		}
		else {
			login = false;
			handler.sendEmptyMessage(1);
		}
	}
}
