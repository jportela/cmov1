package pt.up.fe.cmov.display;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ScheduleAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ScheduleButton> buttons;

    public ScheduleAdapter(Context context) {
        this.context = context;
        buttons = new ArrayList<ScheduleButton>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return buttons.get(position);
    }

	@Override
	public int getCount() {
		return buttons.size();
	}

	@Override
	public Object getItem(int pos) {
		return buttons.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
	
	public void addSchedule(ScheduleButton button) {
		buttons.add(button);
	}
}