package se.mah.kd330a.project.framework;


import se.mah.kd330a.project.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<String> {
	
	private LayoutInflater mInflater;
	private Context context;
	private String[] menuItems;
	private int viewResourceId;
	private TypedArray menuIcons;
	
	

	public MenuAdapter(Context context, int viewResourceId,
			String[] menuItems, TypedArray menuIcons) {
		super(context, viewResourceId, menuItems);
        this.context = context;
        this.menuItems = menuItems;
        this.viewResourceId = viewResourceId;
        this.menuIcons = menuIcons;
        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
       
    }
	
	@Override
	public int getCount() {
	    return menuItems.length;
	}
	

	@Override
	public String getItem(int position) {
	    return menuItems[position];
	}

	@Override
	public long getItemId(int position) {
	    return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    convertView = mInflater.inflate(viewResourceId, null);

	    ImageView iv = (ImageView)convertView.findViewById(R.id.menu_item_icon);
	    iv.setImageDrawable(menuIcons.getDrawable(position));

	    TextView tv = (TextView)convertView.findViewById(R.id.menu_item_text);
	    tv.setText(menuItems[position]);

	    return convertView;
	}
	
	

}