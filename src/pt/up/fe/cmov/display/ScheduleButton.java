package pt.up.fe.cmov.display;

import java.util.Date;

import pt.up.fe.cmov.entities.Appointment;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.Button;

public class ScheduleButton extends Button {

	public static final ColorFilter RED_COLOR_FILTER = new PorterDuffColorFilter(0xFFFF3300, PorterDuff.Mode.MULTIPLY);
    public static final ColorFilter GREEN_COLOR_FILTER = new LightingColorFilter(0xCC99CC00, 0x00000000);

    public static final int STATE_AVAILABLE = 1;
    public static final int STATE_BUSY = 2; 
    
    private int id;
    private int state;
    private Appointment appointment;
    private int scheduleId;
    private Date date;
    
	private OnClickListener scheduleButtonListener =    
		new OnClickListener() {

		@Override
		public void onClick(View v) {
			toggleState();
		}

    };

	public ScheduleButton(Context context, int id, int scheduleId, Date date) {
		super(context);
		this.id = id;
		this.state = STATE_AVAILABLE;
		this.scheduleId = scheduleId;
		this.date = date;
		this.appointment = null;
		this.setText(calculateTimeString());
		this.getBackground().setColorFilter(GREEN_COLOR_FILTER);
		//this.setOnClickListener(scheduleButtonListener);
	}
	
	public ScheduleButton(Context context, int id, int scheduleId, Date date, Appointment appointment) {
		super(context);
		this.id = id;
		this.state = STATE_BUSY;
		this.appointment = appointment;
		this.scheduleId = scheduleId;
		this.date = date;
		this.setText(calculateTimeString());
		this.getBackground().setColorFilter(RED_COLOR_FILTER);
		//this.setOnClickListener(scheduleButtonListener);
	}
	
	public void toggleState() {
		if (state == STATE_AVAILABLE) {
			state = STATE_BUSY;
			this.getBackground().setColorFilter(RED_COLOR_FILTER);
		}
		else if (state == STATE_BUSY) {
			state = STATE_AVAILABLE;
			this.getBackground().setColorFilter(GREEN_COLOR_FILTER);
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
	public String calculateTimeString() {
		int digit = id/2;
    	String digitFix = digit >= 10 ? "" : "0";
    	String minutes = (id % 2 == 1) ? "30" : "00";
    	String text = digitFix + Integer.toString(digit) + ":" + minutes;
    	return text;
	}
}
