package pt.up.fe.cmov.rest;

import org.json.JSONArray;
import org.json.JSONObject;

public class RailsRestClient {
	
	public final static String SERVER_URL = "http://192.168.1.3:3000/";


	public static JSONObject Get(String controller) {
		return HttpClient.SendHttpGet(SERVER_URL + controller + ".json");
	}
	
	public static JSONArray GetArray(String controller) {
		return HttpClient.SendHttpGetArray(SERVER_URL + controller + ".json");
	}
	
	public static JSONObject Get(String controller, String parameters) {
		return HttpClient.SendHttpGet(SERVER_URL + controller + ".json?"+parameters);
	}
	
	public static JSONArray GetArray(String controller, String parameters) {
		return HttpClient.SendHttpGetArray(SERVER_URL + controller + ".json?"+parameters);
	}
	
	public static JSONObject Post(String controller, JSONObject obj) {
		return HttpClient.SendHttpPost(SERVER_URL + controller + ".json", obj);
	}
	
	public static JSONObject Delete(String controller, String id) {
		return HttpClient.SendHttpDelete(SERVER_URL + controller + "/" + id + ".json");
	}
	
	public static JSONObject Put(String controller, String id, JSONObject obj) {
		return HttpClient.SendHttpPut(SERVER_URL + controller + "/" + id + ".json", obj);
	}
}
