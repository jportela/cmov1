package pt.up.fe.cmov.entities;

import java.util.Date;

import pt.up.fe.cmov.providers.AppointmentContentProvider;
import android.net.Uri;

public class Appointment {
	
	int id,patient_id,schedule_id;
	Date date;
	
	public static final String APPOINTMENT_ID = "_id";
	public static final String APPOINTMENT_PATIENT_ID = "patient_id";
	public static final String APPOINTMENT_SCHEDULE_ID = "schedule_id";
	public static final String APPOINTMENT_DATE = "date";

	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AppointmentContentProvider.AUTHORITY + "/appointments");	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.appointments";
	
	public Appointment(int id, int patient_id, int schedule_id, Date date){
		this.id = id;
		this.patient_id = patient_id;
		this.schedule_id = schedule_id;
		this.date = date;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getPatientId(){
		return this.patient_id;
	}
	
	public int getScheduleId(){
		return this.schedule_id;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setPatientId(int patient_id){
		 this.patient_id = patient_id;
	}
	
	public void setScheduleId(int schedule_id){
		this.schedule_id = schedule_id;
	}
	
	public void setDate(Date date){
		this.date = date;
	}

}
