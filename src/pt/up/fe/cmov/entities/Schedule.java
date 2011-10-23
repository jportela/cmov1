package pt.up.fe.cmov.entities;

import java.util.Date;

public class Schedule implements Comparable<Schedule>{
	
	private int id,schedule_plan_id;
	private Date startHour,endHour,dateTime;
	
	public static final String 	SCHEDULE_ID = "_id";
	public static final String 	SCHEDULE_STARHOUR = "start_hour";
	public static final String 	SCHEDULE_ENDHOUR = "end_hour";
	public static final String 	SCHEDULE_DATETIME = "date_time";
	public static final String 	SCHEDULE_PLAN_ID = "schedule_plan_id";
	
	//public static final Uri CONTENT_URI = Uri.parse("content://" + ScheduleContentProvider.AUTHORITY + "/schedules");	
	//public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.schedules";
	
	public Schedule(int id, Date startHour, Date endHour, Date dateTime){
		this.id = id;
		this.endHour = endHour;
		this.startHour = startHour;
		this.dateTime = dateTime;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getSchedulePlanId(){
		return this.schedule_plan_id;
	}
	
	public Date getStartHour(){
		return this.startHour;
	}
	
	public Date getEndHour(){
		return this.endHour;
	}
	
	public Date getDateTime(){
		return this.dateTime;
	}
	

	public void setId(int id){
		this.id = id;
	}
	
	public void setSchedulePlanId(int schedule_plan_id){
		this.schedule_plan_id = schedule_plan_id;
	}
	
	public void setStarHour(Date startHour){
		this.startHour = startHour;
	}
	
	public void setEndHour(Date endHour){
		this.endHour = endHour;
	}
	
	public void setDatetime(Date dateTime){
		this.dateTime = dateTime;
	}

	@Override
	public int compareTo(Schedule another) {
		if(this.dateTime.equals(another.dateTime)){
			if(another.getStartHour().after(this.startHour) && another.getStartHour().before(this.endHour))
				return 1;
			if(this.startHour.equals(another.getStartHour()) || another.getStartHour().equals(this.endHour)) 
				return 1;
			if(another.getEndHour().before(this.endHour) && another.getEndHour().after(this.startHour))
				return 1;
			if(another.getStartHour().before(this.startHour) && another.getEndHour().after(this.endHour))
				return 1;
		}
		
		return 0;
	}
}
