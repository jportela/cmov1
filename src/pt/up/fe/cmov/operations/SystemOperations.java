package pt.up.fe.cmov.operations;

import java.text.ParseException;
import java.util.Date;

import pt.up.fe.cmov.providers.SystemContentProvider;
import pt.up.fe.cmov.rest.JSONOperations;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SystemOperations {
		
	public static Date getLastSync(Context context) {
		Cursor c = context.getContentResolver().query(SystemContentProvider.CONTENT_URI, null, null, null, null); 
		Date lastSync = null;
		if (c.moveToNext()) { 
			String dateStr = c.getString(c.getColumnIndex(SystemContentProvider.SYSTEM_LAST_SYNC));
			try {
				lastSync = JSONOperations.dbDateFormater.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		c.close();
		return lastSync;
	}
	
	public static void updateLastSync(Context context, Date lastSyncTime, Date newSync) {
		ContentValues values = new ContentValues();
		
		values.put(SystemContentProvider.SYSTEM_LAST_SYNC, newSync.toString());
		
		if (lastSyncTime == null)
		{
			context.getContentResolver().insert(SystemContentProvider.CONTENT_URI, values);		
		}
		else {
			context.getContentResolver().update(SystemContentProvider.CONTENT_URI, values, null, null);		
		}
	}

}
