package pt.up.fe.cmov.operations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RailsRestClient;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class AppointmentOperations {
	
public static final String APPOINTMENT_CONTROLER = "appointments";
	
	
	public static boolean createAppointment(Context context, Appointment appointment){
		
		try{	
			RailsRestClient.Post(APPOINTMENT_CONTROLER, JSONOperations.appointmentToJSON(appointment));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
				
		ContentValues values = new ContentValues();
		
		if (appointment.getId() > 0) {
			values.put(Appointment.APPOINTMENT_ID, appointment.getId());
		}

		values.put(Appointment.APPOINTMENT_PATIENT_ID, appointment.getPatientId());
		values.put(Appointment.APPOINTMENT_SCHEDULE_ID, appointment.getScheduleId());
		values.put(Appointment.APPOINTMENT_DOCTOR_ID, appointment.getDoctorId());
		values.put(Appointment.APPOINTMENT_DATE,JSONOperations.dbDateFormater.format(appointment.getDate()));
		Uri uri = context.getContentResolver().insert(Appointment.CONTENT_URI, values);		
		
		return (uri != null);
	}
	
	public static boolean updateAppointment(Context context, Appointment appointment){
		
		try{			
			RailsRestClient.Put(APPOINTMENT_CONTROLER,Integer.toString(appointment.getId()), JSONOperations.appointmentToJSON(appointment));
			
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
		
		ContentValues values = new ContentValues();
		
		values.put(Appointment.APPOINTMENT_ID,appointment.getId());
		values.put(Appointment.APPOINTMENT_PATIENT_ID, appointment.getPatientId());
		values.put(Appointment.APPOINTMENT_SCHEDULE_ID, appointment.getScheduleId());
		values.put(Appointment.APPOINTMENT_DOCTOR_ID, appointment.getDoctorId());
		values.put(Appointment.APPOINTMENT_DATE,JSONOperations.dbDateFormater.format(appointment.getDate()));
		Uri updateAppointmentPlanUri = ContentUris.withAppendedId(Appointment.CONTENT_URI, appointment.getId());
		context.getContentResolver().update(updateAppointmentPlanUri, values, null, null);
		
		return true;
	}
		
	public static boolean deleteAppointment(Context context, Appointment appointment){
		
		try{
			RailsRestClient.Delete(APPOINTMENT_CONTROLER, Integer.toString(appointment.getId()));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
		
		Uri deleteSchedulePlanUri = ContentUris.withAppendedId(Appointment.CONTENT_URI, appointment.getId()); 
		try{
			context.getContentResolver().delete(deleteSchedulePlanUri, null, null); 
			return true;
		}catch(Exception e){
			Log.wtf("DELETE", "Could not delete doctor");
			return false;
		}
	} 
	
	public static Appointment getRemoteServerAppointment(Context context, int id){
		JSONObject json = RailsRestClient.Get(APPOINTMENT_CONTROLER + "/" + Integer.toString(id));
		try {
			 return JSONOperations.JSONToAppointment(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Appointment> getRemoteServerAllAppointment(String controller,int person_id) {
		ArrayList<Appointment> queryAppointments = new ArrayList<Appointment>();
        JSONArray jsonArrays = RailsRestClient.GetArray(controller + "/" 
        					   + Integer.toString(person_id) + "/" + APPOINTMENT_CONTROLER);
        for(int i = 0; i < jsonArrays.length();i++){
        	try {
        		queryAppointments.add(JSONOperations.JSONToAppointment(jsonArrays.getJSONObject(i)));
        	} catch (JSONException e) {
				Log.e("SCHEDULE", "Couldn't decode JSON object");
			} catch (ParseException e) {
				Log.e("SCHEDULE", "Couldn't decode JSON object");
			}
        }
        return queryAppointments;
	}
	
	public static Appointment getAppointment(Context context, int id) {
				
		Uri querySchedulePlanUri = ContentUris.withAppendedId(Appointment.CONTENT_URI, id); 
		Cursor cAppointment = context.getContentResolver().query(querySchedulePlanUri, null, null, null, null); 
		Appointment app = null;
		while (cAppointment.moveToNext()) { 
			   Date dateTime = new Date(cAppointment.getString(cAppointment.getColumnIndex(Appointment.APPOINTMENT_DATE)));
			   String patient_id = cAppointment.getString(cAppointment.getColumnIndex(Appointment.APPOINTMENT_PATIENT_ID)); 
			   String schedule_id = cAppointment.getString(cAppointment.getColumnIndex(Appointment.APPOINTMENT_SCHEDULE_ID)); 
			   String doctor_id = cAppointment.getString(cAppointment.getColumnIndex(Appointment.APPOINTMENT_DOCTOR_ID)); 
			   app = new Appointment(id,Integer.parseInt(patient_id),Integer.parseInt(schedule_id),Integer.parseInt(doctor_id),dateTime);
			} 
		cAppointment.close();
		return app;
	}

}
