package pt.up.fe.cmov.entities;

import java.util.Date;

public class Person {
	protected int id;
	protected String name;
	protected Date birthDate;
	protected String username;
	
	public static final String PERSON_ID = "_id";
	public static final String PERSON_NAME = "name";
	public static final String PERSON_BIRTHDATE = "birthdate";
	public static final String PERSON_USERNAME = "username";

	
	Person(int id, String name, Date birthDate, String username){
		this.id = id;
		this.name = name;
		this. birthDate = birthDate;
		this.username = username;
	}
	
	public Person() {
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
