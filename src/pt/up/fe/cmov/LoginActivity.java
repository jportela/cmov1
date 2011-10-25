package pt.up.fe.cmov;


import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.rest.RemoteSync;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements Runnable,OnClickListener{
	
	ArrayList<Doctor> docs = new ArrayList<Doctor>();
	String username = new String();
    String password = new String();
    ProgressDialog dialog;
    Context context = this;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        final Button button = (Button) findViewById(R.id.loginConfirmation);
        button.setOnClickListener(this);
    }

	public void onClick(View v) {
		dialog = ProgressDialog.show(this, "Loading", "Wait a few seconds", true,
                false);
    	username = ((AutoCompleteTextView) findViewById(R.id.usernameField)).getText().toString();
        password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
        
        Thread thread = new Thread(this);
        thread.start();
	}

	
	@Override
	public void run() {
		try{
			docs = DoctorOperations.queryDoctorsRemoteServer();
			for(Doctor doc:docs){
	            if(username.equals(doc.getUsername()) && password.equals(doc.getPassword())){
	                try
	                {	
	        			RemoteSync.oneClickSync(this, null);
	                    handler.sendEmptyMessage(0);
	                }catch(Exception e){
	                	e.getStackTrace();
	                }
	            }
	        }
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}

	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                dialog.dismiss();
                Intent k = new Intent(LoginActivity.this, Cmov1_doctorActivity.class);
            	startActivity(k);
        }
	};
}
