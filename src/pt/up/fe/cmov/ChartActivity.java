package pt.up.fe.cmov;

import java.util.ArrayList;
import java.util.Date;

import pt.up.fe.cmov.display.GraphView;
import pt.up.fe.cmov.operations.DoctorOperations;
import pt.up.fe.cmov.rest.JSONOperations;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ChartActivity  extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<String> months = DoctorOperations.getRemoteDoctorMonthsApps(this, LoginActivity.loginDoctor.getId());
		
		ArrayList<Float> values = new ArrayList<Float>();
		ArrayList<String> horlabels = new ArrayList<String>();
		ArrayList<String> verlabels = new ArrayList<String>();

		for(int i = 0; i < months.size();i++){
			String[] date = months.get(i).split("-");
			Date data = new Date();
			data.setDate(0);
			data.setMonth(Integer.parseInt(date[0]) - 1);
			data.setYear(Integer.parseInt(date[1]) - 1900);
			Log.w(date[1], date[1]);
			horlabels.add(JSONOperations.monthFormater.format(data));
			i++;
			verlabels.add(months.get(i));
			values.add(Float.parseFloat(months.get(i)));
		}
				
		GraphView graphView = new GraphView(this, values, "Doctor Appointments Statistics",horlabels, verlabels, GraphView.LINE);
		setContentView(graphView);
	}
}