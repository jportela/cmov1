package pt.up.fe.cmov.listadapter;

import java.util.ArrayList;

import pt.up.fe.cmov.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Item> {
	 
    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater vi;
 
    public EntryAdapter(Context context,ArrayList<Item> items) {
        super(context,0, items);
        this.setContext(context);
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
 
        final Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                v = vi.inflate(R.layout.listitemsection, null);
 
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
 
                final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getTitle());
                sectionView.setTextSize(18);
            }else{
                EntryItem ei = (EntryItem)i;
                v = vi.inflate(R.layout.itemlistlayout, null);
                final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
                final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
 
                if (title != null)
                    title.setText(ei.title);
                if(subtitle != null)
                    subtitle.setText(ei.subtitle);
            }
        }
        return v;
    }

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
