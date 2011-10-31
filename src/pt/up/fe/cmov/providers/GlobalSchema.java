package pt.up.fe.cmov.providers;

import pt.up.fe.cmov.entities.Appointment;
import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Patient;
import pt.up.fe.cmov.entities.Person;
import pt.up.fe.cmov.entities.Schedule;
import pt.up.fe.cmov.entities.SchedulePlan;
import pt.up.fe.cmov.entities.Speciality;
import android.database.sqlite.SQLiteDatabase;


public class GlobalSchema {

    public static final String DOCTORS_TABLE_NAME = "doctors";
    public static final String APPOINTMENT_TABLE_NAME = "appointments";
	public static final String PATIENTS_TABLE_NAME = "patients";
    public static final String SCHEDULE_TABLE_NAME = "schedules";
    public static final String SCHEDULEPLAN_TABLE_NAME = "schedule_plans";
    public static final String SPECIALITIES_TABLE_NAME = "specialities";
    public static final String SYSTEM_TABLE_NAME = "system";

	public static void createSchema(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + SYSTEM_TABLE_NAME + " ( " + SystemContentProvider.SYSTEM_ID +
        		" INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        		SystemContentProvider.SYSTEM_LAST_SYNC + " VARCHAR(255));");
		
		db.execSQL("CREATE TABLE " + SPECIALITIES_TABLE_NAME + " (" + Speciality.SPECIALITY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Speciality.SPECIALITY_SNAME + " VARCHAR(255));");
		
		db.execSQL("CREATE TABLE " + DOCTORS_TABLE_NAME + " (" + Person.PERSON_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Person.PERSON_BIRTHDATE + " DATETIME," 
                + Person.PERSON_NAME + " VARCHAR(255)," + Person.PERSON_USERNAME +" VARCHAR(255), "
                + "password_md5 VARCHAR(255), "
                + Doctor.DOCTOR_PHOTO + " VARCHAR(255)," + Doctor.DOCTOR_SPECIALITY + " INTEGER);");
		
		db.execSQL("CREATE TABLE " + PATIENTS_TABLE_NAME + " (" + Person.PERSON_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Person.PERSON_BIRTHDATE + " DATETIME," 
                + Person.PERSON_NAME + " VARCHAR(255)," + Person.PERSON_USERNAME +" VARCHAR(255), " 
                + "password_md5 VARCHAR(255), "
                + Patient.PATIENT_ADDRESS + " VARCHAR(255)," + Patient.PATIENT_SEX  + " VARCHAR(255));");
		
		db.execSQL("CREATE TABLE " + SCHEDULEPLAN_TABLE_NAME + " (" + SchedulePlan.SCHEDULE_PLAN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + SchedulePlan.SCHEDULE_STARTDATE + " DATETIME," 
                + SchedulePlan.SCHEDULE_DOCTOR_ID + " INTEGER);");
		
		db.execSQL("CREATE TABLE " + SCHEDULE_TABLE_NAME + " (" + Schedule.SCHEDULE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Schedule.SCHEDULE_START_DATE + " DATETIME," + Schedule.SCHEDULE_END_DATE + " DATETIME,"
                + Schedule.SCHEDULE_PLAN_ID + " INTEGER);"); 
		
        db.execSQL("CREATE TABLE " + APPOINTMENT_TABLE_NAME + " (" + Appointment.APPOINTMENT_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Appointment.APPOINTMENT_PATIENT_ID + " INTEGER," 
                + Appointment.APPOINTMENT_DOCTOR_ID + " INTEGER," 
                + Appointment.APPOINTMENT_SCHEDULE_ID + " INTEGER," + Appointment.APPOINTMENT_DATE + " DATETIME);");

	}

}
