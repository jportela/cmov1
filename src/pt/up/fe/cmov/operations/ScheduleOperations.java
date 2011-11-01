package pt.up.fe.cmov.operations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Schedule;
import pt.up.fe.cmov.rest.JSONOperations;
import pt.up.fe.cmov.rest.RailsRestClient;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ScheduleOperations {
	
	public static final String SCHEDULE_CONTROLLER = "schedules";
	
	
	public static int createSchedule(Context context, Schedule schedule, boolean isRemote){
		
		if (isRemote) {
			try{	
				String controller = SchedulePlanOperations.SCHEDULE_PLAN_CONTROLLER + "/" + 
					schedule.getSchedulePlanId() + "/" + SCHEDULE_CONTROLLER;
				JSONObject obj = RailsRestClient.Post(controller, JSONOperations.scheduleToJSON(schedule));
				schedule.setId(obj.getInt("id"));
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
		
		ContentValues values = new ContentValues();
		
		if (schedule.getId() > 0) {
			values.put(Schedule.SCHEDULE_ID, schedule.getId());
		}
		
		values.put(Schedule.SCHEDULE_START_DATE, JSONOperations.dbDateFormater.format(schedule.getStartDate()));
		values.put(Schedule.SCHEDULE_END_DATE, JSONOperations.dbDateFormater.format(schedule.getEndDate()));
		values.put(Schedule.SCHEDULE_PLAN_ID, schedule.getSchedulePlanId());
		context.getContentResolver().insert(Schedule.CONTENT_URI, values);		
		
		return schedule.getId();
	}
	
	public static boolean updateSchedule(Context context, Schedule schedule, boolean isRemote){
		if (isRemote) {
			try{			
				String controller = SchedulePlanOperations.SCHEDULE_PLAN_CONTROLLER + "/" + 
					schedule.getSchedulePlanId() + "/" + SCHEDULE_CONTROLLER;
				RailsRestClient.Put(controller,Integer.toString(schedule.getId()), JSONOperations.scheduleToJSON(schedule));
				
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
		
		ContentValues values = new ContentValues();
		
		values.put(Schedule.SCHEDULE_ID, schedule.getId());
		values.put(Schedule.SCHEDULE_START_DATE, JSONOperations.dbDateFormater.format(schedule.getStartDate()));
		values.put(Schedule.SCHEDULE_END_DATE, JSONOperations.dbDateFormater.format(schedule.getEndDate()));
		values.put(Schedule.SCHEDULE_PLAN_ID, schedule.getSchedulePlanId());
		
		Uri updateScheduleUri = ContentUris.withAppendedId(Schedule.CONTENT_URI, schedule.getId());
		context.getContentResolver().update(updateScheduleUri, values, null, null);
		
		return true;
	}
		
	public static boolean deleteSchedule(Context context, Schedule schedule, boolean isRemote){
		if (isRemote) {
			try{
				String controller = SchedulePlanOperations.SCHEDULE_PLAN_CONTROLLER + "/" + 
					schedule.getSchedulePlanId() + "/" + SCHEDULE_CONTROLLER;
				RailsRestClient.Delete(controller, Integer.toString(schedule.getId()));
			}catch(Exception e){
				e.printStackTrace();
				Log.w("NO Internet", "You don't have a internet connection");
				Toast.makeText(context, "No internet connection... Retry later", Toast.LENGTH_LONG).show();
			}
		}
		
		Uri deleteScheduleUri = ContentUris.withAppendedId(Schedule.CONTENT_URI, schedule.getId()); 
		try{
			context.getContentResolver().delete(deleteScheduleUri, null, null); 
			return true;
		}catch(Exception e){
			Log.wtf("DELETE", "Could not delete doctor");
			return false;
		}
	} 
	
	public static Schedule getRemoteServerSchedule(Context context, int schedulePlanId, int id){
		String controller = SchedulePlanOperations.SCHEDULE_PLAN_CONTROLLER + "/" + 
			Integer.toString(schedulePlanId) + "/" + SCHEDULE_CONTROLLER;
		try {
			JSONObject json = RailsRestClient.Get(controller + "/" + Integer.toString(id));
			return JSONOperations.JSONToSchedule(json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
		}
		return null;
	}
	
	public static Schedule getSchedule(Context context, int id) throws ParseException {
				
		Uri queryScheduleUri = ContentUris.withAppendedId(Schedule.CONTENT_URI, id); 
		Cursor cSchedule = context.getContentResolver().query(queryScheduleUri, null, null, null, null); 
		Schedule sch = null;
		while (cSchedule.moveToNext()) { 
			   Date startDate = JSONOperations.dbDateFormater.parse(cSchedule.getString(cSchedule.getColumnIndex(Schedule.SCHEDULE_START_DATE)));
			   Date endDate = JSONOperations.dbDateFormater.parse(cSchedule.getString(cSchedule.getColumnIndex(Schedule.SCHEDULE_END_DATE)));
			   int schedule_plan_id = cSchedule.getInt(cSchedule.getColumnIndex(Schedule.SCHEDULE_PLAN_ID));
			   sch = new Schedule(id,startDate,endDate);
			   sch.setSchedulePlanId(schedule_plan_id);
			} 
		cSchedule.close();
		return sch;
	}
	
	public static ArrayList<Schedule> getRemoteSchedules(Context context, int schedulePlanId) {
		String controller = SchedulePlanOperations.SCHEDULE_PLAN_CONTROLLER + "/" + 
			Integer.toString(schedulePlanId) + "/" + SCHEDULE_CONTROLLER;
		ArrayList<Schedule> schedules = null;
		try {
			JSONArray jsonArray = RailsRestClient.GetArray(controller);
			schedules = new ArrayList<Schedule>();
			for (int i=0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					Schedule schedule = JSONOperations.JSONToSchedule(obj);
					schedules.add(schedule);
					Log.i("SCHEDULE", schedule.getStartDate().toString() + "---" + schedule.getEndDate().toString());
			}
		}  catch (JSONException e) {
			Log.e("SCHEDULE", "Couldn't decode JSON object");
		} catch (ParseException e) {
			Log.e("SCHEDULE", "Couldn't decode JSON object");
		} catch (ConnectTimeoutException e) {
		}
		
		return schedules;
	}
	
	public static boolean createOrUpdateSchedule(Context context, Schedule schedule) throws ParseException {
		if (getSchedule(context, schedule.getId()) == null) {
			createSchedule(context, schedule, false);
		}
		else {
			updateSchedule(context, schedule, false);
		}
		return true;
	}
	
	public static ArrayList<Schedule> querySchedules(Context context, String selection, String[] selectedArguments, String order){
        ArrayList<Schedule> querySchedules = new ArrayList<Schedule>();
        Cursor cSchedule = context.getContentResolver().query(Schedule.CONTENT_URI, null, selection, selectedArguments, order); 
        while (cSchedule.moveToNext()) { 
        	Date startDate = null;
        	Date endDate = null;
        	
        	int id = cSchedule.getInt(cSchedule.getColumnIndex(Schedule.SCHEDULE_PLAN_ID)); 
		    int schedule_plan_id = cSchedule.getInt(cSchedule.getColumnIndex(Schedule.SCHEDULE_PLAN_ID));
		    
		    try {
		    	startDate = JSONOperations.dbDateFormater.parse(cSchedule.getString(cSchedule.getColumnIndex(Schedule.SCHEDULE_START_DATE)));
		    	endDate = JSONOperations.dbDateFormater.parse(cSchedule.getString(cSchedule.getColumnIndex(Schedule.SCHEDULE_END_DATE)));
		    } catch (ParseException e) {
		    	e.printStackTrace();
		    }
		    
		    Schedule sch = new Schedule(id,startDate,endDate);
		    sch.setSchedulePlanId(schedule_plan_id);
    	 	
		    querySchedules.add(sch);
         } 
        cSchedule.close();
        return querySchedules;
     }
}
