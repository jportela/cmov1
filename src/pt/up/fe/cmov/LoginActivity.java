package pt.up.fe.cmov;


import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.operations.DoctorOperations;
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
	
	ArrayList<Doctor> docs = new ArrayList<Doctor>();
	String username = new String();
    String password = new String();
    ProgressDialog dialog;
    boolean login = false;
    Thread thread;
	
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
        
        thread = new Thread(this);
        thread.start();
	}

	
	@Override
	public void run() {
		try{
			docs = DoctorOperations.queryDoctorsRemoteServer(this);
			for(Doctor doc:docs){
	            if(username.equals(doc.getUsername()) && password.equals(doc.getPassword())){
	                try
	                {
	                	login = true;
	        			RemoteSync.oneClickSync(this, null);
	                    handler.sendEmptyMessage(0);
	                }catch(Exception e){
	                	e.getStackTrace();
	                }
	            }
	        }
			
			if(!login){
				handler.sendEmptyMessage(1);
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
        		thread.stop();
        		if(!login){
        			Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        		}else{
        			Intent k = new Intent(LoginActivity.this, DoctorActivity.class);
        			startActivity(k);
        		}
        }
	};
}
