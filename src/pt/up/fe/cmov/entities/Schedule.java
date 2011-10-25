package pt.up.fe.cmov.entities;

import java.util.Date;

import android.net.Uri;

import pt.up.fe.cmov.providers.ScheduleContentProvider;

public class Schedule implements Comparable<Schedule>{
	
	private int id,schedule_plan_id;
	private Date startDate,endDate;
	
	public static final String 	SCHEDULE_ID = "_id";
	public static final String 	SCHEDULE_START_DATE = "start_date";
	public static final String 	SCHEDULE_END_DATE = "end_date";
	public static final String 	SCHEDULE_PLAN_ID = "schedule_plan_id";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + ScheduleContentProvider.AUTHORITY + "/schedules");	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.schedules";
	
	public Schedule(int id, Date startDate, Date endDate){
		this.id = id;
		this.endDate = endDate;
		this.startDate = startDate;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getSchedulePlanId(){
		return this.schedule_plan_id;
	}
	
	public Date getStartDate(){
		return this.startDate;
	}
	
	public Date getEndDate(){
		return this.endDate;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public void setSchedulePlanId(int schedule_plan_id){
		this.schedule_plan_id = schedule_plan_id;
	}
	
	public void setStarDate(Date startDate){
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	@Override
	public int compareTo(Schedule another) {
		if(another.getStartDate().after(this.startDate) && another.getStartDate().before(this.endDate))
			return 1;
		if(this.startDate.equals(another.getStartDate()) || another.getStartDate().equals(this.endDate)) 
			return 1;
		if(this.endDate.equals(another.getStartDate()) || another.getEndDate().equals(this.startDate)) 
			return 1;
		if(another.getEndDate().before(this.endDate) && another.getEndDate().after(this.startDate))
			return 1;
		if(another.getStartDate().before(this.startDate) && another.getEndDate().after(this.endDate))
			return 1;
		
		return 0;
	}
}
