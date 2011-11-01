package pt.up.fe.cmov.entities;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.providers.SpecialityContentProvider;
import pt.up.fe.cmov.rest.RailsRestClient;
import android.net.Uri;
import android.util.Log;

public class Speciality {
	
	private int id;
	private String name;
	
	public static final String SPECIALITY_ID = "_id";
	public static final String SPECIALITY_SNAME = "sname";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + SpecialityContentProvider.AUTHORITY + "/specialities");	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmov.specialities";
	
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
}
