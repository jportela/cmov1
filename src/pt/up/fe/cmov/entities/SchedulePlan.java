package pt.up.fe.cmov.entities;

import java.util.Date;
import java.util.HashMap;

public class SchedulePlan {
	
	private int id,doctor_id;
	private Date startDate;
	private HashMap<Schedule,Boolean> scheduleBlocks;

	public static final String 	SCHEDULE_PLAN_ID = "_id";
	public static final String 	SCHEDULE_DOCTOR_ID = "doctor_id";
	public static final String 	SCHEDULE_STARTDATE = "start_date";
	
	//public static final Uri CONTENT_URI = Uri.parse("content://" + ScheduleContentProvider.AUTHORITY + "/schedule_plans");	
	//public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.schedule_plans";
	
	public SchedulePlan(int id, int doctor_id, Date startDate){
		this.id = id;
		this.doctor_id = doctor_id;
		this.startDate = startDate;
		scheduleBlocks = new HashMap<Schedule,Boolean>();
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setDoctorId(int doctor_id){
		this.doctor_id = doctor_id;
	}
	
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getDoctorId(){
		return this.doctor_id;
	}
	
	public Date getStartDate(){
		return this.startDate;
	}
	
	public boolean addScheduleBlock(Schedule sch){
		if(!this.scheduleBlocks.containsKey(sch))
			return this.scheduleBlocks.put(sch, true);
		else
			return false;
	}
	
	public boolean removeScheduleBlock(Schedule sch){
		if(this.scheduleBlocks.containsKey(sch))
			return this.scheduleBlocks.remove(sch);
		else
			return false;
	}

}
