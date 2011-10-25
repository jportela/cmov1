package pt.up.fe.cmov.rest;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Speciality;
import pt.up.fe.cmov.operations.SpecialityOperations;
import pt.up.fe.cmov.operations.SystemOperations;
import android.content.Context;
import android.util.Log;

public class RemoteSync {
	
	public static boolean oneClickSync(Context context, Patient patient) {
		
		try {
			Date lastSyncTime = SystemOperations.getLastSync(context);
			Date updateTime = getSystemTime();
			
			if (updateTime == null) {
				Log.e("REST", "Network connection error: Could not retrieve time");
				return false;
			}
			
			Log.i("REST", "System Time: " + updateTime.toString());
			
			
			boolean needsUpdate = false;
			needsUpdate |= syncSpecialities(context, lastSyncTime);
			needsUpdate |= syncDoctors(lastSyncTime);
			needsUpdate |= syncSchedules(lastSyncTime);
			
			if (patient != null) {
				needsUpdate |= syncPatientAppointments(lastSyncTime, patient);
			}
			
			SystemOperations.updateLastSync(context, lastSyncTime, updateTime);

			return needsUpdate;
		}
		catch(Exception e) {
			//TODO: handle exception
		}
		return false;
	}

	private static boolean syncPatientAppointments(Date lastSyncTime, Patient patient) {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static boolean syncSchedules(Date lastSyncTime) {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static boolean syncDoctors(Date lastSyncTime) {
		// TODO Auto-generated method stub
		return false;
		
	}

	private static boolean syncSpecialities(Context context, Date lastSyncTime) {
		String dateStr = "";
		if (lastSyncTime != null) {
			lastSyncTime.setHours(lastSyncTime.getHours()-1);
			dateStr = JSONOperations.dbDateFormater.format(lastSyncTime);
		}
		
		JSONArray specialitiesList = RailsRestClient.GetArray("doctors/specialities/updated", "time="+dateStr);
		try {
			for (int i=0; i < specialitiesList.length(); i++) {
				
				JSONObject record = (JSONObject) specialitiesList.get(i);
				Speciality speciality = JSONOperations.JSONToSpeciality(record);
				SpecialityOperations.createOrUpdateSpeciality(context, speciality);
				Log.i("CMOV_SYNC", "Speciality " + speciality.getId() + ": " + speciality.getName());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
