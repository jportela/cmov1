package pt.up.fe.cmov;

import java.util.ArrayList;

import pt.up.fe.cmov.common.entities.Doctor;
import pt.up.fe.cmov.common.entities.Speciality;
import pt.up.fe.cmov.common.operations.DoctorOperations;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Cmov1_doctorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
               //Integer id = (Integer) jsonObjRecv.get("id");
        try {
		    Speciality.Records.populate();
	        /*Doctor d1 = new Doctor(0, "Joaquim", null, "poop", "like", Speciality.Records.get(3));
	        Doctor d2 = new Doctor(0, "Maria", null, "a", "boss", Speciality.Records.get(6));
	        Doctor d3 = new Doctor(0, "Joe", null, "calhou", "poop", Speciality.Records.get(11));
	        DoctorOperations.createDoctor(this, d1);
	        DoctorOperations.createDoctor(this, d2);
	        DoctorOperations.createDoctor(this, d3);*/
	        //DoctorOperations.updateDoctor(this, d);
		    //Doctor dude = DoctorOperations.getDoctor(this, 2);
		    
		    //if (dude == null) 
		    //	Log.i("DOCTOR", "nulo");
		    //else
		    //	Log.i("DOCTOR", dude.getName());
		    
		    //DoctorOperations.deleteDoctor(this, dude);
		    //ArrayList<Doctor> doctors = DoctorOperations.queryDoctors(this, null, null, null);
		    
		    //for (int i=0; i < doctors.size(); i++) {
		    //	Doctor d = doctors.get(i);
		    //	Log.i("DOCTOR", d.getName());
		    //}
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
		 
    }
}