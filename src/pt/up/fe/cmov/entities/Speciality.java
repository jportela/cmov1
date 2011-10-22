package pt.up.fe.cmov.entities;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pt.up.fe.cmov.rest.RailsRestClient;

public class Speciality {
	
	private int id;
	private String name;
	
	public Speciality(int id, String name) {
		this.id = id;
		this.name = name;
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

	public static Speciality.SpecialityList Records = new Speciality.SpecialityList();

	
	public static class SpecialityList {
		private static HashMap<Integer, Speciality> specialities = new HashMap<Integer, Speciality>();
		private static boolean isPopulated = false;
		
		public void populate() {
			JSONArray specialitiesList = RailsRestClient.GetArray("doctors/specialities");
			try {
				for (int i=0; i < specialitiesList.length(); i++) {
					
					JSONObject record = (JSONObject) specialitiesList.get(i);
					int id = record.getInt("id");
					String name = record.getString("name");
					
					Speciality speciality = new Speciality(id, name);
					
					specialities.put(id, speciality);
					Log.i("cmov", "Speciality " + id + ": " + name);
				}
				SpecialityList.isPopulated = true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Speciality get(int id) {
			if (!isPopulated)
				return null;
			
			return specialities.get(id);
		}
		
	}
}
