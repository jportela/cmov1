package pt.up.fe.cmov.operations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
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
import android.widget.Toast;

public class PatientOperations {
	
	public static final String PATIENT_CONTROLER = "patients";
	
	public static boolean createPatient(Context context, Patient patient, boolean isRemote){
		if (isRemote) {
			try{	
				RailsRestClient.Post(PATIENT_CONTROLER, JSONOperations.patientToJSON(patient));
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
				
		ContentValues values = new ContentValues();
		
		if (patient.getId() > 0) {
			values.put(Person.PERSON_ID, patient.getId());
		}
		
		values.put(Person.PERSON_NAME,patient.getName());
		values.put(Person.PERSON_USERNAME, patient.getUsername());
		values.put(Patient.PATIENT_ADDRESS, patient.getAddress());
		values.put(Patient.PATIENT_SEX, patient.getSex());
		values.put("password_md5", patient.getPassword());
		values.put(Person.PERSON_BIRTHDATE, JSONOperations.dbDateFormater.format(patient.getBirthDate().getTime()));
		Uri uri = context.getContentResolver().insert(Patient.CONTENT_URI, values);		
		
		return (uri != null);
	}
	
	public static boolean updatePatient(Context context, Patient patient, boolean isRemote){
		if (isRemote) {
			try{			
				RailsRestClient.Put(PATIENT_CONTROLER,Integer.toString(patient.getId()), JSONOperations.patientToJSON(patient));
				
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
		ContentValues values = new ContentValues();
		
		values.put(Person.PERSON_NAME,patient.getId());
		values.put(Person.PERSON_NAME,patient.getName());

		values.put(Person.PERSON_USERNAME, patient.getUsername());
		values.put(Patient.PATIENT_ADDRESS, patient.getAddress());
		values.put(Patient.PATIENT_SEX, patient.getSex());
		values.put("password_md5", patient.getPassword());
		values.put(Person.PERSON_BIRTHDATE, JSONOperations.dbDateFormater.format(patient.getBirthDate().getTime()));
		Uri updateDoctorUri = ContentUris.withAppendedId(Patient.CONTENT_URI, patient.getId());
		context.getContentResolver().update(updateDoctorUri, values, null, null);
		
		return true;
	}
		
	public static boolean deletePatient(Context context, Patient patient){
		
		try{
			RailsRestClient.Delete(PATIENT_CONTROLER, Integer.toString(patient.getId()));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
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
	
	public static Patient getRemoteServerPatient(Context context, int id){
		try {	
			JSONObject json = RailsRestClient.Get(PATIENT_CONTROLER + "/" + Integer.toString(id));
			 return JSONOperations.JSONToPatient(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}  catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	public static Patient getPatient(Context context, int id) {
				
		Uri queryPatientUri = ContentUris.withAppendedId(Patient.CONTENT_URI, id); 
		Cursor cPatient = context.getContentResolver().query(queryPatientUri, null, null, null, null); 
		Patient d = null;
		while (cPatient.moveToNext()) { 
			Date birthdate = null;
			try {
				birthdate = JSONOperations.dbDateFormater.parse(cPatient.getString(cPatient.getColumnIndex(Person.PERSON_BIRTHDATE)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    String name = cPatient.getString(cPatient.getColumnIndex(Patient.PERSON_NAME));
		    String username = cPatient.getString(cPatient.getColumnIndex(Patient.PERSON_USERNAME)); 
		    String address = cPatient.getString(cPatient.getColumnIndex(Patient.PATIENT_ADDRESS)); 
		    String sex = cPatient.getString(cPatient.getColumnIndex(Patient.PATIENT_SEX)); 
            String password = cPatient.getString(cPatient.getColumnIndex("password_md5"));
		    d = new Patient(id,name,birthdate,username,address,sex, password);
		} 
		cPatient.close();
		return d;
	}
	
	public static ArrayList<Patient> queryPatientsRemoteServer(Context context) throws JSONException, ParseException{
		ArrayList<Patient> queryPatients = new ArrayList<Patient>();
        try {
			JSONArray jsonArrays = RailsRestClient.GetArray("patients");
	        for(int i = 0; i < jsonArrays.length();i++){
	        	queryPatients.add(JSONOperations.JSONToPatient(jsonArrays.getJSONObject(i)));
	        }
        } catch (ConnectTimeoutException e) {
			Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
		}
        return queryPatients;
	}
	
	public static ArrayList<Patient> queryPatients(Context context, String selection, String[] selectedArguments, String order){
        ArrayList<Patient> queryPatients = new ArrayList<Patient>();
        Cursor cDoctor = context.getContentResolver().query(Patient.CONTENT_URI, null, selection, selectedArguments, order); 
        while (cDoctor.moveToNext()) { 
        	Date birthdate = null;
			try {
				birthdate = JSONOperations.dbDateFormater.parse(cDoctor.getString(cDoctor.getColumnIndex(Person.PERSON_BIRTHDATE)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int id = cDoctor.getInt(cDoctor.getColumnIndex(Patient.PERSON_ID));

		    String name = cDoctor.getString(cDoctor.getColumnIndex(Patient.PERSON_NAME));
		    String username = cDoctor.getString(cDoctor.getColumnIndex(Patient.PERSON_USERNAME)); 
		    String address = cDoctor.getString(cDoctor.getColumnIndex(Patient.PATIENT_ADDRESS)); 
		    String sex = cDoctor.getString(cDoctor.getColumnIndex(Patient.PATIENT_SEX)); 
            String password = cDoctor.getString(cDoctor.getColumnIndex("password_md5"));

		    Patient p = new Patient(id,name,birthdate,username,address,sex, password);
            queryPatients.add(p);
         } 
        cDoctor.close();
        return queryPatients;
     }
	
	public static void createOrUpdatePatient(Context context, Patient patient) {
		if (getPatient(context, patient.getId()) == null) {
			createPatient(context, patient, false);
		}
		else {
			updatePatient(context, patient, false);
		}		
	}
}
