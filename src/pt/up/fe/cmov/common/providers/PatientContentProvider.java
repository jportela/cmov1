package pt.up.fe.cmov.common.providers;

import java.util.HashMap;

import pt.up.fe.cmov.common.entities.Doctor;
import pt.up.fe.cmov.common.entities.Patient;
import pt.up.fe.cmov.common.entities.Person;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class PatientContentProvider extends ContentProvider {

	 private static final String TAG = "PatientContentProvider";

	 private static final String DATABASE_NAME = "pclinic.db";

	 private static final int DATABASE_VERSION = 1;

	 private static final String PATIENTS_TABLE_NAME = "patients";

	 public static final String AUTHORITY = "pt.up.fe.cmov.common.providers.patientcontentprovider";

	private static final UriMatcher sUriMatcher;

	 private static final int PATIENTS = 1;
	    
	 private static final int PATIENTS_ID = 2;

	 private static HashMap<String, String> patientsProjectionMap;
	
	 private static class DatabaseHelper extends SQLiteOpenHelper {

	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) {
	            db.execSQL("CREATE TABLE " + PATIENTS_TABLE_NAME + " (" + Person.PERSON_ID
	                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Person.PERSON_BIRTHDATE + " DATETIME," 
	                    + Person.PERSON_NAME + " VARCHAR(255)," + Person.PERSON_USERNAME +" VARCHAR(255), " 
	                    + Patient.PATIENT_ADDRESS + " VARCHAR(255)," + Patient.PATIENT_SEX  + " VARCHAR(255));");
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
	                    + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS " + PATIENTS_TABLE_NAME);
	            onCreate(db);
	        }
	    }

	    private DatabaseHelper dbHelper;

	    @Override
	    public int delete(Uri uri, String where, String[] whereArgs) {
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        int count;
	        switch (sUriMatcher.match(uri)) {
	            case PATIENTS:
	                count = db.delete(PATIENTS_TABLE_NAME, where, whereArgs);
	                break;
	            case PATIENTS_ID:
	            	String id = uri.getLastPathSegment();
	                count = db.delete(PATIENTS_TABLE_NAME,  Person.PERSON_ID + "= ?", new String[]{id});
	                break;
	            default:
	                throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	    }

	    @Override
	    public String getType(Uri uri) {
	        switch (sUriMatcher.match(uri)) {
	            case PATIENTS:
	                return Patient.CONTENT_TYPE;

	            default:
	                throw new IllegalArgumentException("Unknown URI " + uri);
	        }
	    }

	    @Override
	    public Uri insert(Uri uri, ContentValues initialValues) {
	        if (sUriMatcher.match(uri) != PATIENTS) { throw new IllegalArgumentException("Unknown URI " + uri); }

	        ContentValues values;
	        if (initialValues != null) {
	            values = new ContentValues(initialValues);
	        } else {
	            values = new ContentValues();
	        }

	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        long rowId = db.insert(PATIENTS_TABLE_NAME, null, values);
	        if (rowId > 0) {
	            Uri noteUri = ContentUris.withAppendedId(Doctor.CONTENT_URI, rowId);
	            getContext().getContentResolver().notifyChange(noteUri, null);
	            return noteUri;
	        }

	        throw new SQLException("Failed to insert row into " + uri);
	    }

	    @Override
	    public boolean onCreate() {
	        dbHelper = new DatabaseHelper(getContext());
	        return true;
	    }

	    @Override
	    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        qb.setTables(PATIENTS_TABLE_NAME);
	        qb.setProjectionMap(patientsProjectionMap);
	        Cursor c = null;

	        switch (sUriMatcher.match(uri)) {
	            case PATIENTS:
	                c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	                break;

	            case PATIENTS_ID:
	            	String id = uri.getLastPathSegment();
	                c = qb.query(db, projection, Person.PERSON_ID + "= ?", new String[]{id}, null, null, null);
	                break;

	            default:
	                throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        c.setNotificationUri(getContext().getContentResolver(), uri);
	        return c;
	    }

	    @Override
	    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        int count;
	        switch (sUriMatcher.match(uri)) {
	            case PATIENTS:
	                count = db.update(PATIENTS_TABLE_NAME, values, where, whereArgs);
	                break;
	            case PATIENTS_ID:
	            	String id = uri.getLastPathSegment();
	                count = db.update(PATIENTS_TABLE_NAME, values,  Person.PERSON_ID + "= ?", new String[]{id});
	                break;
	            default:
	                throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	    }

	    static {
	        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	        sUriMatcher.addURI(AUTHORITY, PATIENTS_TABLE_NAME, PATIENTS);
	        sUriMatcher.addURI(AUTHORITY, PATIENTS_TABLE_NAME + "/#", PATIENTS_ID);

	        patientsProjectionMap = new HashMap<String, String>();
	        patientsProjectionMap.put(Person.PERSON_ID, Person.PERSON_ID);
	        patientsProjectionMap.put(Person.PERSON_NAME, Person.PERSON_NAME);
	        patientsProjectionMap.put(Person.PERSON_USERNAME, Person.PERSON_USERNAME);
	        patientsProjectionMap.put(Person.PERSON_BIRTHDATE, Person.PERSON_BIRTHDATE);
	        patientsProjectionMap.put(Patient.PATIENT_ADDRESS, Patient.PATIENT_ADDRESS);
	        patientsProjectionMap.put(Patient.PATIENT_SEX, Patient.PATIENT_SEX);
	    }


}
