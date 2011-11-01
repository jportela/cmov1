package pt.up.fe.cmov.operations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Person;
import pt.up.fe.cmov.entities.SchedulePlan;
import pt.up.fe.cmov.entities.Speciality;
import pt.up.fe.cmov.providers.DoctorContentProvider;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RailsRestClient;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class DoctorOperations {
	
	public static final String DOCTOR_CONTROLER = "doctors";
	private static String current_stats = "stats";
	
	
	public static boolean createDoctor(Context context, Doctor doctor, boolean isRemote){
		
		if (isRemote) {
			try{	
				RailsRestClient.Post(DOCTOR_CONTROLER, JSONOperations.doctorToJSON(doctor));
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
		
		ContentValues values = new ContentValues();
		
		if (doctor.getId() > 0) {
			values.put(Person.PERSON_ID, doctor.getId());
		}
		
		values.put(Person.PERSON_NAME,doctor.getName());
		values.put(Person.PERSON_USERNAME, doctor.getUsername());
		values.put(Doctor.DOCTOR_PHOTO, doctor.getPhoto());
		values.put("password_md5", doctor.getPassword());
		values.put(Doctor.DOCTOR_SPECIALITY, doctor.getSpeciality().getId());
		values.put(Person.PERSON_BIRTHDATE, JSONOperations.dbDateFormater.format(doctor.getBirthDate().getTime()));
		Uri uri = context.getContentResolver().insert(Doctor.CONTENT_URI, values);		
		
		return (uri != null);
	}
	
	public static boolean updateDoctor(Context context, Doctor doctor, boolean isRemote){
		
		if (isRemote) {
			try{			
				RailsRestClient.Put(DOCTOR_CONTROLER,Integer.toString(doctor.getId()), JSONOperations.doctorToJSON(doctor));
				
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}				

		}
		
		ContentValues values = new ContentValues();
		
		values.put(Person.PERSON_NAME,doctor.getId());
		values.put(Person.PERSON_NAME,doctor.getName());

		values.put(Person.PERSON_USERNAME, doctor.getUsername());
		values.put(Doctor.DOCTOR_PHOTO, doctor.getPhoto());
		values.put("password_md5", doctor.getPassword());
		values.put(Doctor.DOCTOR_SPECIALITY, doctor.getSpeciality().getId());
		values.put(Person.PERSON_BIRTHDATE, JSONOperations.dbDateFormater.format(doctor.getBirthDate().getTime()));
		Uri updateDoctorUri = ContentUris.withAppendedId(Doctor.CONTENT_URI, doctor.getId());
		context.getContentResolver().update(updateDoctorUri, values, null, null);
		
		return true;
	}
		
	public static boolean deleteDoctor(Context context, Doctor doctor, boolean isRemote){
		if (isRemote) {
			try{
				RailsRestClient.Delete(DOCTOR_CONTROLER, Integer.toString(doctor.getId()));
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
		
		Uri deleteDoctorUri = ContentUris.withAppendedId(Doctor.CONTENT_URI, doctor.getId()); 
		try{
			context.getContentResolver().delete(deleteDoctorUri, null, null); 
			return true;
		}catch(Exception e){
			Log.wtf("DELETE", "Could not delete doctor");
			return false;
		}
	} 
	
	public static Doctor getRemoteServerDoctor(Context context, int id){
		try {
			JSONObject json = RailsRestClient.Get(DOCTOR_CONTROLER + "/" + Integer.toString(id));
			return JSONOperations.JSONToDoctor(context, json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	public static Doctor getDoctor(Context context, int id) {
				
		Uri queryDoctorUri = ContentUris.withAppendedId(Doctor.CONTENT_URI, id); 
		Cursor cDoctor = context.getContentResolver().query(queryDoctorUri, null, null, null, null); 
		Doctor d = null;
		while (cDoctor.moveToNext()) { 
			   Date birthdate = null;
			   try {
				   birthdate = JSONOperations.dbDateFormater.parse(cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_BIRTHDATE)));
			   }
			   catch (ParseException e) {
				   
			   }
			   String name = cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_NAME));
			   String username = cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_USERNAME)); 
			   String photo = cDoctor.getString(cDoctor.getColumnIndex(Doctor.DOCTOR_PHOTO)); 
			   int specialityId = cDoctor.getInt(cDoctor.getColumnIndex(Doctor.DOCTOR_SPECIALITY)); 
			   d = new Doctor(id,name,birthdate,username,photo,
					   SpecialityOperations.getSpeciality(context, specialityId));
			} 
		cDoctor.close();
		return d;
	}
	
	public static ArrayList<Doctor> queryDoctorsRemoteServer(Context context) throws JSONException, ParseException{
        ArrayList<Doctor> queryDoctors = new ArrayList<Doctor>();
        try {
	        JSONArray jsonArrays = RailsRestClient.GetArray("doctors");
	        for(int i = 0; i < jsonArrays.length();i++){
	        	queryDoctors.add(JSONOperations.JSONToDoctor(context, jsonArrays.getJSONObject(i)));
	        }
        } catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
        return queryDoctors;
     }
		
	public static ArrayList<Doctor> queryDoctors(Context context, String selection, String[] selectedArguments, String order){
        ArrayList<Doctor> queryDoctors = new ArrayList<Doctor>();
        Cursor cDoctor = context.getContentResolver().query(Doctor.CONTENT_URI, null, selection, selectedArguments, order); 
        while (cDoctor.moveToNext()) { 
                 String id = cDoctor.getString( cDoctor.getColumnIndex(Doctor.PERSON_ID)); 
                 String password = cDoctor.getString(cDoctor.getColumnIndex("password_md5"));
                 String birthdate = cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_BIRTHDATE));
                 String name = cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_NAME));
                 String username = cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_USERNAME)); 
                 String photo = cDoctor.getString(cDoctor.getColumnIndex(Doctor.DOCTOR_PHOTO)); 
                 int specialityId = cDoctor.getInt(cDoctor.getColumnIndex(Doctor.DOCTOR_SPECIALITY)); 
                 
                 Date date = null;
  			     try {
  				     date = JSONOperations.dbDateFormater.parse(birthdate);
  			     }
  			     catch (ParseException e) {
  			    	 e.printStackTrace();
  			     }
                 
                 queryDoctors.add(new Doctor(Integer.parseInt(id),name,date,username,photo,
                                        SpecialityOperations.getSpeciality(context, specialityId),password));
         } 
        cDoctor.close();
        return queryDoctors;
     }
	
	 public static ArrayList<Doctor> queryInnerJoinDoctorSpeciality(){
        ArrayList<Doctor> queryDoctors = new ArrayList<Doctor>();
        Cursor cDoctor = DoctorContentProvider.queryDoctorInnerJoinSpeciality(); 
        while (cDoctor.moveToNext()) { 
            	 String id = cDoctor.getString(cDoctor.getColumnIndex(Person.PERSON_ID));
            	 String speciality_id = cDoctor.getString(cDoctor.getColumnIndex(Speciality.SPECIALITY_ID));
                 String doctorName = cDoctor.getString( cDoctor.getColumnIndex(Person.PERSON_NAME)); 
                 String SpecialityName = cDoctor.getString(cDoctor.getColumnIndex(Speciality.SPECIALITY_SNAME));
                 queryDoctors.add(new Doctor(Integer.parseInt(id),doctorName,null,SpecialityName,speciality_id,null));
         } 
        cDoctor.close();
        return queryDoctors;
     }
	 
	public static void createOrUpdateDoctor(Context context, Doctor doctor) {
		if (getDoctor(context, doctor.getId()) == null) {
			createDoctor(context, doctor, false);
		}
		else {
			updateDoctor(context, doctor, false);
		}		
	}
	
	public static ArrayList<String> getRemoteDoctorMonthsApps(Context context, int doctorId){
		JSONObject json = null;
		try {
			json = RailsRestClient.Get(DOCTOR_CONTROLER + "/" + Integer.toString(doctorId) + "/" + current_stats);
		} catch (ConnectTimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			 return JSONOperations.JSONToMonth(context, json);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
        return null;
	}
	
	public static Patient getRemoteDoctorMorePatient(Context context, int doctorId){
		JSONObject json = null;
		try {
			json = RailsRestClient.Get(DOCTOR_CONTROLER + "/" + Integer.toString(doctorId) + "/" + current_stats);
		} catch (ConnectTimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			 return JSONOperations.JSONToMoreAppsPatient(context, json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static SchedulePlan getRemoteCurrentPlan(Context context, int doctorId){
		try {	
			JSONObject json = RailsRestClient.Get(DOCTOR_CONTROLER + "/" + Integer.toString(doctorId) + "/" + "current_plan");
			return JSONOperations.JSONToSchedulePlan(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	public static ArrayList<SchedulePlan> getRemoteCurrentPlans(Context context, int doctorId){
		ArrayList<SchedulePlan> plans = new ArrayList<SchedulePlan>();
		try {
			JSONArray json = RailsRestClient.GetArray(DOCTOR_CONTROLER + "/" + Integer.toString(doctorId) + "/" + "current_plans");
			for (int i=0; i < json.length(); i++) {
				plans.add(JSONOperations.JSONToSchedulePlan(json.getJSONObject(i)));
			}
			return plans;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	public static Appointment getFurthestAppointment(Context context, int doctorId) {
		try {
			JSONObject json = RailsRestClient.Get(DOCTOR_CONTROLER + "/" + Integer.toString(doctorId) + "/furthest_appointment");
			return JSONOperations.JSONToAppointment(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
		return null;
	}
}
