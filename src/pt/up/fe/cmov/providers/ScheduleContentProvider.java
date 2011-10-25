package pt.up.fe.cmov.providers;

import java.util.HashMap;

import pt.up.fe.cmov.entities.Schedule;

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

public class ScheduleContentProvider extends ContentProvider {
	
	private static final String TAG = "SchedulePlanContentProvider";

    private static final String DATABASE_NAME = "pclinic.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SCHEDULE_TABLE_NAME = "schedules";

    public static final String AUTHORITY = "pt.up.fe.cmov.common.providers.schedulescontentprovider";

    private static final UriMatcher sUriMatcher;

    private static final int SCHEDULE = 1;
    
    private static final int SCHEDULE_ID = 2;

    private static HashMap<String, String> scheduleProjectionMap;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + SCHEDULE_TABLE_NAME + " (" + Schedule.SCHEDULE_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Schedule.SCHEDULE_START_DATE + " DATETIME," + Schedule.SCHEDULE_END_DATE + " DATETIME,"
                    + Schedule.SCHEDULE_PLAN_ID + " INTEGER);"); 
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case SCHEDULE:
                count = db.delete(SCHEDULE_TABLE_NAME, where, whereArgs);
                break;
            case SCHEDULE_ID:
            	String id = uri.getLastPathSegment();
                count = db.delete(SCHEDULE_TABLE_NAME,  Schedule.SCHEDULE_ID + "= ?", new String[]{id});
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
            case SCHEDULE:
                return Schedule.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != SCHEDULE) { throw new IllegalArgumentException("Unknown URI " + uri); }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(SCHEDULE_TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Schedule.CONTENT_URI, rowId);
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
        qb.setTables(SCHEDULE_TABLE_NAME);
        qb.setProjectionMap(scheduleProjectionMap);
        Cursor c = null;

        switch (sUriMatcher.match(uri)) {
            case SCHEDULE:
                c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case SCHEDULE_ID:
            	String id = uri.getLastPathSegment();
                c = qb.query(db, projection, Schedule.SCHEDULE_ID + "= ?", new String[]{id}, null, null, null);
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
            case SCHEDULE:
                count = db.update(SCHEDULE_TABLE_NAME, values, where, whereArgs);
                break;
            case SCHEDULE_ID:
            	String id = uri.getLastPathSegment();
                count = db.update(SCHEDULE_TABLE_NAME, values,  Schedule.SCHEDULE_ID + "= ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, SCHEDULE_TABLE_NAME, SCHEDULE); 
        sUriMatcher.addURI(AUTHORITY, SCHEDULE_TABLE_NAME + "/#", SCHEDULE_ID);

        scheduleProjectionMap = new HashMap<String, String>();
        scheduleProjectionMap.put(Schedule.SCHEDULE_ID, Schedule.SCHEDULE_ID);
        scheduleProjectionMap.put(Schedule.SCHEDULE_START_DATE, Schedule.SCHEDULE_START_DATE);
        scheduleProjectionMap.put(Schedule.SCHEDULE_END_DATE,Schedule.SCHEDULE_END_DATE);
        scheduleProjectionMap.put(Schedule.SCHEDULE_PLAN_ID,Schedule.SCHEDULE_PLAN_ID);
    }

}
