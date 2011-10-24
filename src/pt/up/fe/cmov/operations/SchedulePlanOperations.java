package pt.up.fe.cmov.operations;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.SchedulePlan;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RailsRestClient;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SchedulePlanOperations {
	
public static final String SCHEDULEPLAN_CONTROLER = "schedule_plans";
	
	
	public static boolean createSchedulePlan(Context context, SchedulePlan schedulePlan){
		
		try{	
			RailsRestClient.Post(SCHEDULEPLAN_CONTROLER, JSONOperations.schedulePlanToJSON(schedulePlan));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
				
		ContentValues values = new ContentValues();
		
		if (schedulePlan.getId() > 0) {
			values.put(SchedulePlan.SCHEDULE_PLAN_ID, schedulePlan.getId());
		}
		
		values.put(SchedulePlan.SCHEDULE_STARTDATE,JSONOperations.dbDateFormater.format(schedulePlan.getStartDate()));
		values.put(SchedulePlan.SCHEDULE_DOCTOR_ID, schedulePlan.getId());
		Uri uri = context.getContentResolver().insert(SchedulePlan.CONTENT_URI, values);		
		
		return (uri != null);
	}
	
	public static boolean updateSchedulePlan(Context context, SchedulePlan schedulePlan){
		
		try{			
			RailsRestClient.Put(SCHEDULEPLAN_CONTROLER,Integer.toString(schedulePlan.getId()), JSONOperations.schedulePlanToJSON(schedulePlan));
			
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
		
		ContentValues values = new ContentValues();
		
		values.put(SchedulePlan.SCHEDULE_PLAN_ID,schedulePlan.getId());
		values.put(SchedulePlan.SCHEDULE_STARTDATE,JSONOperations.dbDateFormater.format(schedulePlan.getStartDate()));
		values.put(SchedulePlan.SCHEDULE_DOCTOR_ID, schedulePlan.getId());
		Uri updateSchedulePlanUri = ContentUris.withAppendedId(SchedulePlan.CONTENT_URI, schedulePlan.getId());
		context.getContentResolver().update(updateSchedulePlanUri, values, null, null);
		
		return true;
	}
		
	public static boolean deleteSchedulePlan(Context context, SchedulePlan schedulePlan){
		
		try{
			RailsRestClient.Delete(SCHEDULEPLAN_CONTROLER, Integer.toString(schedulePlan.getId()));
		}catch(Exception e){
			e.printStackTrace();
			Log.w("NO Internet", "You don't have a internet connection");
		}
		
		Uri deleteSchedulePlanUri = ContentUris.withAppendedId(SchedulePlan.CONTENT_URI, schedulePlan.getId()); 
		try{
			context.getContentResolver().delete(deleteSchedulePlanUri, null, null); 
			return true;
		}catch(Exception e){
			Log.wtf("DELETE", "Could not delete doctor");
			return false;
		}
	} 
	
	public static SchedulePlan getRemoteServerDoctor(Context context, int id){
		JSONObject json = RailsRestClient.Get(SCHEDULEPLAN_CONTROLER + "/" + Integer.toString(id));
		try {
			 return JSONOperations.JSONToSchedulePlan(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static SchedulePlan getDoctor(Context context, int id) {
				
		Uri querySchedulePlanUri = ContentUris.withAppendedId(SchedulePlan.CONTENT_URI, id); 
		Cursor cSchedulePlan = context.getContentResolver().query(querySchedulePlanUri, null, null, null, null); 
		SchedulePlan sch = null;
		while (cSchedulePlan.moveToNext()) { 
			   Date dateTime = new Date(cSchedulePlan.getString(cSchedulePlan.getColumnIndex(SchedulePlan.SCHEDULE_STARTDATE)));
			   String doctor_id = cSchedulePlan.getString(cSchedulePlan.getColumnIndex(SchedulePlan.SCHEDULE_DOCTOR_ID)); 
			   sch = new SchedulePlan(id,Integer.parseInt(doctor_id),dateTime);
			} 
		cSchedulePlan.close();
		return sch;
	}
}
