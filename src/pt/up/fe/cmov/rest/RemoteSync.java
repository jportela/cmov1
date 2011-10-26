package pt.up.fe.cmov.rest;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Schedule;
import pt.up.fe.cmov.entities.SchedulePlan;
import pt.up.fe.cmov.entities.Speciality;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.operations.ScheduleOperations;
import pt.up.fe.cmov.operations.SchedulePlanOperations;
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
			needsUpdate |= syncDoctors(context, lastSyncTime);
			needsUpdate |= syncSchedulesPlans(context, lastSyncTime);
			needsUpdate |= syncSchedules(context, lastSyncTime);
			
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
	
	private static boolean syncSchedules(Context context, Date lastSyncTime) {
		String dateStr = getServerDateString(lastSyncTime);

		boolean needsUpdate = false;
		
		JSONArray schedules = RailsRestClient.GetArray("schedules/updated", "time="+dateStr);
		
		try {
			for (int i=0; i < schedules.length(); i++) {
				
				JSONObject record = (JSONObject) schedules.get(i);
				Schedule schedule = JSONOperations.JSONToSchedule(record);
				ScheduleOperations.createOrUpdateSchedule(context, schedule);
				Log.i("CMOV_SYNC", "Schedule " + schedule.getId());
				needsUpdate = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return needsUpdate;
		
	}

	private static boolean syncSchedulesPlans(Context context, Date lastSyncTime) {
		String dateStr = getServerDateString(lastSyncTime);

		boolean needsUpdate = false;
		
		JSONArray schedulePlans = RailsRestClient.GetArray("schedule_plans/updated", "time="+dateStr);
		
		try {
			for (int i=0; i < schedulePlans.length(); i++) {
				
				JSONObject record = (JSONObject) schedulePlans.get(i);
				SchedulePlan plan = JSONOperations.JSONToSchedulePlan(record);
				SchedulePlanOperations.createOrUpdateSchedulePlan(context, plan);
				Log.i("CMOV_SYNC", "Schedule Plan " + plan.getId());
				needsUpdate = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return needsUpdate;
		
	}

	private static boolean syncDoctors(Context context, Date lastSyncTime) {
		String dateStr = getServerDateString(lastSyncTime);
		
		boolean needsUpdate = false;
		
		JSONArray doctorsList = RailsRestClient.GetArray("doctors/updated", "time="+dateStr);
		
		try {
			for (int i=0; i < doctorsList.length(); i++) {
				
				JSONObject record = (JSONObject) doctorsList.get(i);
				Doctor doctor = JSONOperations.JSONToDoctor(context, record);
				DoctorOperations.createOrUpdateDoctor(context, doctor);
				Log.i("CMOV_SYNC", "Doctor " + doctor.getId() + ": " + doctor.getName());
				needsUpdate = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return needsUpdate;
		
	}

	private static boolean syncSpecialities(Context context, Date lastSyncTime) {
		
		String dateStr = getServerDateString(lastSyncTime);
		
		boolean needsUpdate = false;
		
		JSONArray specialitiesList = RailsRestClient.GetArray("doctors/specialities/updated", "time="+dateStr);
		
		try {
			for (int i=0; i < specialitiesList.length(); i++) {
				
				JSONObject record = (JSONObject) specialitiesList.get(i);
				Speciality speciality = JSONOperations.JSONToSpeciality(record);
				SpecialityOperations.createOrUpdateSpeciality(context, speciality);
				Log.i("CMOV_SYNC", "Speciality " + speciality.getId() + ": " + speciality.getName());
				needsUpdate = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return needsUpdate;
		
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
	
	private static String getServerDateString(Date date) {
		String dateStr = "";
		if (date != null) {
			JSONOperations.dbDateFormater.setTimeZone(TimeZone.getDefault());
			dateStr = JSONOperations.dbDateFormater.format(date);
		}
		return dateStr;
	}
	

}
