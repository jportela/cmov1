package pt.up.fe.cmov.entities;

import java.util.Date;

import pt.up.fe.cmov.providers.PatientContentProvider;
import android.net.Uri;

public class Patient extends Person {

	private String address,sex;
	private String password = "";
		
	public static final String PATIENT_ADDRESS = "address";
	public static final String PATIENT_SEX = "sex";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + PatientContentProvider.AUTHORITY + "/patients");	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.patients";
	
	public Patient(int id, String name, Date birthDate, String username,String address,String sex) {
		super(id, name, birthDate, username);
		this.address = address;
		this.sex = sex;
	}
	
	public Patient(int id, String name, Date birthDate, String username,String address,String sex,String password) {
		super(id, name, birthDate, username);
		this.address = address;
		this.sex = sex;
		this.setPassword(password);
	}
	
	public Patient(){}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public void setSex(String sex){
		this.sex = sex;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getSex(){
		return this.sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
