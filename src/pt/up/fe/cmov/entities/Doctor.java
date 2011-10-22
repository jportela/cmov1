package pt.up.fe.cmov.entities;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.providers.DoctorContentProvider;
import android.net.Uri;

public class Doctor extends Person {
	
	private String photo;
	private Speciality speciality;
	
	public static final String DOCTOR_PHOTO = "photo";
	public static final String DOCTOR_SPECIALITY = "speciality_id";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + DoctorContentProvider.AUTHORITY + "/doctors");	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.doctors";
	
	public Doctor(JSONObject obj) {
		super();
		try {
			this.id = obj.getInt("id");
			this.name = obj.getString("name");
			//String birthDate = obj.getString("date");
			this.photo = obj.getString("photo");
			this.speciality = Speciality.Records.get(id);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Doctor(int id, String name, Date birthDate, String username, String photo, Speciality speciality) {
		super(id,name,birthDate,username);
		this.photo = photo;
		this.speciality = speciality;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Speciality getSpeciality() {
		return speciality;
	}
	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

}
