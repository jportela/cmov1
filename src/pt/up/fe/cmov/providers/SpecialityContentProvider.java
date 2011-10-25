package pt.up.fe.cmov.providers;

import java.util.HashMap;

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

public class SpecialityContentProvider extends ContentProvider {


    private static final String TAG = "SpecialityContentProvider";

    private static final String DATABASE_NAME = "pclinic.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SPECIALITIES_TABLE_NAME = "specialities";

    public static final String AUTHORITY = "pt.up.fe.cmov.common.providers.specialitycontentprovider";

    private static final UriMatcher sUriMatcher;

    private static final int SPECIALITIES = 1;
    
    private static final int SPECIALITY_ID = 2;

    private static HashMap<String, String> specialitiesProjectionMap;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + SPECIALITIES_TABLE_NAME + " (" + Speciality.SPECIALITY_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Speciality.SPECIALITY_NAME + " VARCHAR(255));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SPECIALITIES_TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case SPECIALITIES:
                count = db.delete(SPECIALITIES_TABLE_NAME, where, whereArgs);
                break;
            case SPECIALITY_ID:
            	String id = uri.getLastPathSegment();
                count = db.delete(SPECIALITIES_TABLE_NAME,  Speciality.SPECIALITY_ID + "= ?", new String[]{id});
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
            case SPECIALITIES:
                return Speciality.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != SPECIALITIES) { throw new IllegalArgumentException("Unknown URI " + uri); }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(SPECIALITIES_TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Speciality.CONTENT_URI, rowId);
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
        qb.setTables(SPECIALITIES_TABLE_NAME);
        qb.setProjectionMap(specialitiesProjectionMap);
        Cursor c = null;

        switch (sUriMatcher.match(uri)) {
            case SPECIALITIES:
                c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case SPECIALITY_ID:
            	String id = uri.getLastPathSegment();
                c = qb.query(db, projection, Speciality.SPECIALITY_ID + "= ?", new String[]{id}, null, null, null);
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
            case SPECIALITIES:
                count = db.update(SPECIALITIES_TABLE_NAME, values, where, whereArgs);
                break;
            case SPECIALITY_ID:
            	String id = uri.getLastPathSegment();
                count = db.update(SPECIALITIES_TABLE_NAME, values,  Speciality.SPECIALITY_ID + "= ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, SPECIALITIES_TABLE_NAME, SPECIALITIES);
        sUriMatcher.addURI(AUTHORITY, SPECIALITIES_TABLE_NAME + "/#", SPECIALITY_ID);

        specialitiesProjectionMap = new HashMap<String, String>();
        specialitiesProjectionMap.put(Speciality.SPECIALITY_ID, Speciality.SPECIALITY_ID);
        specialitiesProjectionMap.put(Speciality.SPECIALITY_NAME, Speciality.SPECIALITY_NAME);
    }
}
