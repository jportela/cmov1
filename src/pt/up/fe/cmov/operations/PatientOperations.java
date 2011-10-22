package pt.up.fe.cmov.operations;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Person;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RailsRestClient;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class PatientOperations {
	
	public static final String PATIENT_CONTROLER = "patients";
	
	public static boolean createDoctor(Context context, Patient patient){
		
		try{	
			RailsRestClient.Post(PATIENT_CONTROLER, JSONOperations.patientToJSON(patient));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
				
		ContentValues values = new ContentValues();
		
		if (patient.getId() > 0) {
			values.put(Person.PERSON_ID, patient.getId());
		}
		
		values.put(Person.PERSON_NAME,patient.getName());
		values.put(Person.PERSON_USERNAME, patient.getUsername());
		values.put(Patient.PATIENT_ADDRESS, patient.getAddress());
		values.put(Patient.PATIENT_SEX, patient.getSex());
		values.put(Person.PERSON_BIRTHDATE, JSONOperations.dbDateFormater.format(patient.getBirthDate().getTime()));
		Uri uri = context.getContentResolver().insert(Patient.CONTENT_URI, values);		
		
		return (uri != null);
	}
	
	public static boolean updateDoctor(Context context, Patient patient){
		
		try{			
			RailsRestClient.Put(PATIENT_CONTROLER,Integer.toString(patient.getId()), JSONOperations.patientToJSON(patient));
			
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
		
		ContentValues values = new ContentValues();
		
		values.put(Person.PERSON_NAME,patient.getId());
		values.put(Person.PERSON_NAME,patient.getName());

		values.put(Person.PERSON_USERNAME, patient.getUsername());
		values.put(Patient.PATIENT_ADDRESS, patient.getAddress());
		values.put(Patient.PATIENT_SEX, patient.getSex());
		values.put(Person.PERSON_BIRTHDATE, JSONOperations.dbDateFormater.format(patient.getBirthDate().getTime()));
		Uri updateDoctorUri = ContentUris.withAppendedId(Patient.CONTENT_URI, patient.getId());
		context.getContentResolver().update(updateDoctorUri, values, null, null);
		
		return true;
	}
		
	public static boolean deleteDoctor(Context context, Patient patient){
		
		try{
			RailsRestClient.Delete(PATIENT_CONTROLER, Integer.toString(patient.getId()));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
		
		Uri deleteDoctorUri = ContentUris.withAppendedId(Patient.CONTENT_URI, patient.getId()); 
		try{
			context.getContentResolver().delete(deleteDoctorUri, null, null); 
			return true;
		}catch(Exception e){
			Log.wtf("DELETE", "Could not delete doctor");
			return false;
		}
	} 
	
	public static Patient getRemoteServerDoctor(Context context, int id){
		JSONObject json = RailsRestClient.Get(PATIENT_CONTROLER + "/" + Integer.toString(id));
		try {
			 return JSONOperations.JSONToPatient(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Patient getDoctor(Context context, int id) {
				
		Uri queryPatientUri = ContentUris.withAppendedId(Patient.CONTENT_URI, id); 
		Cursor cPatient = context.getContentResolver().query(queryPatientUri, null, null, null, null); 
		Patient d = null;
		while (cPatient.moveToNext()) { 
			   //Date birthdate = cDoctor.getString(cDoctor.getColumnIndex(Doctor.PERSON_BIRTHDATE));
			   String name = cPatient.getString(cPatient.getColumnIndex(Patient.PERSON_NAME));
			   String username = cPatient.getString(cPatient.getColumnIndex(Patient.PERSON_USERNAME)); 
			   String address = cPatient.getString(cPatient.getColumnIndex(Patient.PATIENT_ADDRESS)); 
			   String sex = cPatient.getString(cPatient.getColumnIndex(Patient.PATIENT_SEX)); 
			   d = new Patient(id,name,null,username,address,sex);
			} 
		cPatient.close();
		return d;
	}

}
