package pt.up.fe.cmov.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Schedule;
import pt.up.fe.cmov.entities.SchedulePlan;
import pt.up.fe.cmov.entities.Speciality;
import pt.up.fe.cmov.operations.SpecialityOperations;
import android.content.Context;

public class JSONOperations {
	
	public static final DateFormat dbDateFormater = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
	public static final SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM kk:mm");  
	public static final DateFormat dbDateTimeZoneFormater = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssz");

	public static JSONObject doctorToJSON(Doctor doctor) throws JSONException{
		
		String birthday = dbDateFormater.format(doctor.getBirthDate().getTime());
		
		JSONObject json = new JSONObject();
		json.put("birthdate", birthday);
		json.put("name", doctor.getName());
		json.put("password_md5", "LOL");
		json.put("username", doctor.getUsername());
		json.put("photo", doctor.getPhoto());
		json.put("speciality_id", doctor.getSpeciality().getId());
		return json;
	}
	
	public static Doctor JSONToDoctor(Context context, JSONObject json) throws JSONException, ParseException{
				
		int id = json.getInt("id");
		Date birthday =	dbDateFormater.parse(json.getString("birthdate"));
		String name = json.getString("name");
		String password = json.getString("password_md5");
		String username = json.getString("username");
		String photo = json.getString("photo");
		Speciality spec = SpecialityOperations.getSpeciality(context, json.getInt("speciality_id"));
		return new Doctor(id,name,birthday,username,photo,spec,password);
	}
	
	public static JSONObject patientToJSON(Patient patient) throws JSONException{
		
		String birthday = dbDateFormater.format(patient.getBirthDate().getTime());
		
		JSONObject json = new JSONObject();
		json.put("birthdate", birthday);
		json.put("name", patient.getName());
		json.put("password_md5", "LOL");
		json.put("username", patient.getUsername());
		json.put("address", patient.getAddress());
		json.put("sex", patient.getSex());
		return json;
	}
	
	public static Patient JSONToPatient(JSONObject json) throws JSONException, ParseException{
				
		int id = json.getInt("id");
		Date birthday =	dbDateFormater.parse(json.getString("birthdate"));
		String name = json.getString("name");
		String username = json.getString("username");
		String photo = json.getString("address");
		String sex = json.getString("sex");
		return new Patient(id,name,birthday,username,photo,sex);
	}
	
	public static Date JSONToDate(JSONObject json) throws JSONException, ParseException {
		Date time =	dbDateFormater.parse(json.getString("time"));
		return time;
	}
	
	public static JSONObject schedulePlanToJSON(SchedulePlan sch) throws JSONException{
		String startDate = dbDateFormater.format(sch.getStartDate().getTime());
		
		JSONObject json = new JSONObject();
		json.put(SchedulePlan.SCHEDULE_STARTDATE, startDate);
		json.put(SchedulePlan.SCHEDULE_DOCTOR_ID, sch.getDoctorId());
		return json;
	}
	
	public static SchedulePlan JSONToSchedulePlan(JSONObject json) throws JSONException, ParseException{
		int id = json.getInt("id");
		Date startime =	dbDateFormater.parse(json.getString(SchedulePlan.SCHEDULE_STARTDATE));
		int doctor_id = json.getInt(SchedulePlan.SCHEDULE_DOCTOR_ID);
		return new SchedulePlan(id,doctor_id,startime);
	}
	
	public static JSONObject appointmentToJSON(Appointment app) throws JSONException{
		String startDate = dbDateFormater.format(app.getDate().getTime());
		
		JSONObject json = new JSONObject();
		json.put(Appointment.APPOINTMENT_PATIENT_ID,app.getPatientId());
		json.put(Appointment.APPOINTMENT_SCHEDULE_ID,app.getScheduleId());
		json.put(Appointment.APPOINTMENT_DATE, startDate);
		return json;
	}
	
	public static Appointment JSONToAppointment(JSONObject json) throws JSONException, ParseException{
		int id = json.getInt("id");
		Date startime =	dbDateFormater.parse(json.getString(Appointment.APPOINTMENT_DATE));
		int patient_id = json.getInt(Appointment.APPOINTMENT_PATIENT_ID);
		int schedule_id = json.getInt(Appointment.APPOINTMENT_SCHEDULE_ID);
		return new Appointment(id,patient_id,schedule_id,startime);
	}

	public static JSONObject DateToJSON(Date date) throws JSONException, ParseException {
		JSONObject obj = new JSONObject();
		obj.put("time", dbDateFormater.format(date));
		return obj;
	}
	
	public static Speciality JSONToSpeciality(JSONObject json) throws JSONException, ParseException{
		int id = json.getInt("id");
		String name = json.getString("name");
		return new Speciality(id,name);
	}

	public static Schedule JSONToSchedule(JSONObject json) throws JSONException, ParseException{
		int id = json.getInt("id");
		Date startDate = dbDateFormater.parse(json.getString(Schedule.SCHEDULE_START_DATE));
		Date endDate = dbDateFormater.parse(json.getString(Schedule.SCHEDULE_END_DATE));
		return new Schedule(id, startDate, endDate);
	}
}
