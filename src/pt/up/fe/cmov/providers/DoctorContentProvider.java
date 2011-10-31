package pt.up.fe.cmov.providers;

import java.util.HashMap;

import pt.up.fe.cmov.entities.Doctor;
import pt.up.fe.cmov.entities.Person;
import pt.up.fe.cmov.entities.Speciality;

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

public class DoctorContentProvider extends ContentProvider {


    private static final String TAG = "DoctorContentProvider";

    private static final String DATABASE_NAME = "pclinic.db";

    private static final int DATABASE_VERSION = 3;

    private static final String DOCTORS_TABLE_NAME = "doctors";

    public static final String AUTHORITY = "pt.up.fe.cmov.common.providers.doctorcontentprovider";

    private static final UriMatcher sUriMatcher;

    private static final int DOCTORS = 1;
    
    private static final int DOCTOR_ID = 2;

    private static HashMap<String, String> doctorsProjectionMap;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DOCTORS_TABLE_NAME + " (" + Person.PERSON_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Person.PERSON_BIRTHDATE + " DATETIME," 
                    + Person.PERSON_NAME + " VARCHAR(255)," + Person.PERSON_USERNAME +" VARCHAR(255), " 
                    + Doctor.DOCTOR_PHOTO + " VARCHAR(255)," + Doctor.DOCTOR_SPECIALITY + " INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DOCTORS_TABLE_NAME);
            onCreate(db);
        }
    }

    private static DatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case DOCTORS:
                count = db.delete(DOCTORS_TABLE_NAME, where, whereArgs);
                break;
            case DOCTOR_ID:
            	String id = uri.getLastPathSegment();
                count = db.delete(DOCTORS_TABLE_NAME,  Person.PERSON_ID + "= ?", new String[]{id});
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
            case DOCTORS:
                return Doctor.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != DOCTORS) { throw new IllegalArgumentException("Unknown URI " + uri); }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(DOCTORS_TABLE_NAME, null, values);
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
    
    public static Cursor queryDoctorInnerJoinSpeciality(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    	final String MY_QUERY = "SELECT d." + Person.PERSON_NAME + ",s."+ Speciality.SPECIALITY_SNAME + ",d._id,s._id AS speciality_id" + 
    			" FROM " + DOCTORS_TABLE_NAME + " d INNER JOIN  " + SpecialityContentProvider.SPECIALITIES_TABLE_NAME +
    			" s ON d.speciality_id=s._id ORDER BY s." + Speciality.SPECIALITY_SNAME;
    	return db.rawQuery(MY_QUERY, new String[]{});
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        qb.setTables(DOCTORS_TABLE_NAME);
        qb.setProjectionMap(doctorsProjectionMap);
        Cursor c = null;

        switch (sUriMatcher.match(uri)) {
            case DOCTORS:
                c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case DOCTOR_ID:
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
            case DOCTORS:
                count = db.update(DOCTORS_TABLE_NAME, values, where, whereArgs);
                break;
            case DOCTOR_ID:
            	String id = uri.getLastPathSegment();
                count = db.update(DOCTORS_TABLE_NAME, values,  Person.PERSON_ID + "= ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DOCTORS_TABLE_NAME, DOCTORS);
        sUriMatcher.addURI(AUTHORITY, DOCTORS_TABLE_NAME + "/#", DOCTOR_ID);

        doctorsProjectionMap = new HashMap<String, String>();
        doctorsProjectionMap.put(Person.PERSON_ID, Person.PERSON_ID);
        doctorsProjectionMap.put(Person.PERSON_NAME, Person.PERSON_NAME);
        doctorsProjectionMap.put(Person.PERSON_USERNAME, Person.PERSON_USERNAME);
        doctorsProjectionMap.put(Person.PERSON_BIRTHDATE, Person.PERSON_BIRTHDATE);
        doctorsProjectionMap.put(Doctor.DOCTOR_PHOTO, Doctor.DOCTOR_PHOTO);
        doctorsProjectionMap.put(Doctor.DOCTOR_SPECIALITY, Doctor.DOCTOR_SPECIALITY);
    }
}
