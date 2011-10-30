package pt.up.fe.cmov.providers;

import java.util.HashMap;

import pt.up.fe.cmov.entities.Appointment;

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

public class AppointmentContentProvider extends ContentProvider {
	
	private static final String TAG = "SchedulePlanContentProvider";

    private static final String DATABASE_NAME = "pclinic.db";

    private static final int DATABASE_VERSION = 2;

    private static final String APPOINTMENT_TABLE_NAME = "appointments";

    public static final String AUTHORITY = "pt.up.fe.cmov.common.providers.appointmentcontentprovider";

    private static final UriMatcher sUriMatcher;

    private static final int APPOINTMENT = 1;
    
    private static final int APPOINTMENT_ID = 2;

    private static HashMap<String, String> appointmentsPlanProjectionMap;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + APPOINTMENT_TABLE_NAME + " (" + Appointment.APPOINTMENT_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Appointment.APPOINTMENT_PATIENT_ID + " INTEGER," 
                    + Appointment.APPOINTMENT_DOCTOR_ID + " INTEGER," 
                    + Appointment.APPOINTMENT_SCHEDULE_ID + " INTEGER," + Appointment.APPOINTMENT_DATE + " DATETIME);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + APPOINTMENT_TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case APPOINTMENT:
                count = db.delete(APPOINTMENT_TABLE_NAME, where, whereArgs);
                break;
            case APPOINTMENT_ID:
            	String id = uri.getLastPathSegment();
                count = db.delete(APPOINTMENT_TABLE_NAME,  Appointment.APPOINTMENT_ID + "= ?", new String[]{id});
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
            case APPOINTMENT:
                return Appointment.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != APPOINTMENT) { throw new IllegalArgumentException("Unknown URI " + uri); }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(APPOINTMENT_TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Appointment.CONTENT_URI, rowId);
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
        qb.setTables(APPOINTMENT_TABLE_NAME);
        qb.setProjectionMap(appointmentsPlanProjectionMap);
        Cursor c = null;

        switch (sUriMatcher.match(uri)) {
            case APPOINTMENT:
                c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case APPOINTMENT_ID:
            	String id = uri.getLastPathSegment();
                c = qb.query(db, projection, Appointment.APPOINTMENT_ID + "= ?", new String[]{id}, null, null, null);
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
            case APPOINTMENT:
                count = db.update(APPOINTMENT_TABLE_NAME, values, where, whereArgs);
                break;
            case APPOINTMENT_ID:
            	String id = uri.getLastPathSegment();
                count = db.update(APPOINTMENT_TABLE_NAME, values,  Appointment.APPOINTMENT_ID + "= ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, APPOINTMENT_TABLE_NAME, APPOINTMENT);
        sUriMatcher.addURI(AUTHORITY, APPOINTMENT_TABLE_NAME + "/#", APPOINTMENT_ID);

        appointmentsPlanProjectionMap = new HashMap<String, String>();
        appointmentsPlanProjectionMap.put(Appointment.APPOINTMENT_ID, Appointment.APPOINTMENT_ID);
        appointmentsPlanProjectionMap.put(Appointment.APPOINTMENT_PATIENT_ID, Appointment.APPOINTMENT_PATIENT_ID);
        appointmentsPlanProjectionMap.put(Appointment.APPOINTMENT_SCHEDULE_ID,Appointment.APPOINTMENT_SCHEDULE_ID);
        appointmentsPlanProjectionMap.put(Appointment.APPOINTMENT_DOCTOR_ID,Appointment.APPOINTMENT_DOCTOR_ID);
        appointmentsPlanProjectionMap.put(Appointment.APPOINTMENT_DATE,Appointment.APPOINTMENT_DATE);
    }

}
