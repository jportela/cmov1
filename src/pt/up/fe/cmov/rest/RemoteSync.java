package pt.up.fe.cmov.rest;

import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

import pt.up.fe.cmov.entities.Patient;

public class RemoteSync {
	
	public static boolean oneClickSync(Patient patient) {
		
		try {
			Date updateTime = getSystemTime();
			
			if (updateTime == null) {
				Log.e("REST", "Network connection error: Could not retrieve time");
				return false;
			}
			
			Log.i("REST", "System Time: " + updateTime.toString());
			
			
			boolean needsUpdate = false;
			needsUpdate |= syncSpecialities();
			needsUpdate |= syncDoctors();
			needsUpdate |= syncSchedules();
			
			if (patient != null) {
				needsUpdate |= syncPatientAppointments(patient);
			}

			return needsUpdate;
		}
		catch(Exception e) {
			//TODO: handle exception
		}
		return false;
	}

	private static boolean syncPatientAppointments(Patient patient) {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static boolean syncSchedules() {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static boolean syncDoctors() {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static boolean syncSpecialities() {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static Date getSystemTime() {
		try {
			JSONObject obj = RailsRestClient.Get("system/time");
			return JSONOperations.JSONToDate(obj);
		}
		catch(Exception e) {
			Log.e("REST", "No connection with the server");
		}
		return null;
	}
	

}
