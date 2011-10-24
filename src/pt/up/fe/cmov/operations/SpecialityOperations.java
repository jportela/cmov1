package pt.up.fe.cmov.operations;

import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Speciality;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SpecialityOperations {
	
	public static boolean createSpeciality(Context context, Speciality speciality){
		
				
		ContentValues values = new ContentValues();
		
		if (speciality.getId() > 0) {
			values.put(Speciality.SPECIALITY_ID, speciality.getId());
		}
		
		values.put(Speciality.SPECIALITY_NAME, speciality.getName());
	
		Uri uri = context.getContentResolver().insert(Speciality.CONTENT_URI, values);		
		
		return (uri != null);
	}
	
	public static boolean updateSpeciality(Context context, Speciality speciality){
		
		ContentValues values = new ContentValues();
		
		values.put(Speciality.SPECIALITY_ID, speciality.getId());
		values.put(Speciality.SPECIALITY_NAME, speciality.getName());

		Uri updateUri = ContentUris.withAppendedId(Speciality.CONTENT_URI, speciality.getId());
		context.getContentResolver().update(updateUri, values, null, null);
		
		return true;
	}
		
	public static boolean deleteDoctor(Context context, Speciality speciality){
		
		Uri deleteUri = ContentUris.withAppendedId(Speciality.CONTENT_URI, speciality.getId());
		try{
			context.getContentResolver().delete(deleteUri, null, null); 
			return true;
		}catch(Exception e){
			Log.wtf("DELETE", "Could not delete speciality");
			return false;
		}
	} 
	
	public static Speciality getSpeciality(Context context, int id) {
				
		Uri queryUri = ContentUris.withAppendedId(Patient.CONTENT_URI, id); 
		Cursor c = context.getContentResolver().query(queryUri, null, null, null, null); 
		Speciality s = null;
		if (c.moveToNext()) { 
			   String name = c.getString(c.getColumnIndex(Patient.PERSON_NAME));
			   s = new Speciality(id,name);
			} 
		c.close();
		return s;
	}

}
