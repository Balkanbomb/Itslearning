package com.example.mad2013_itslearning;

import java.util.HashMap;
import java.util.List;
 
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<Article> _listDataHeader; // header titles
    
    // child data in format of header title, child title
    private HashMap<Article, String> _listDataChild;
    
    boolean showHeaderText = true;
 
    public ExpandableListAdapter(Context context, List<Article> listDataHeader,
            HashMap<Article, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
 
        txtListChild.setText(childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        Article headerTitle = (Article) getGroup(groupPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
       
        lblListHeader.setText(headerTitle.getArticleHeader());
        
        TextView lblListHeaderDate = (TextView) convertView.findViewById(R.id.lblListHeaderDate);
        	
        lblListHeaderDate.setText(headerTitle.getArticleDate());
        
        TextView lblListHeaderText = (TextView) convertView.findViewById(R.id.lblListHeaderText);
        
        if (headerTitle.isTextVisible())
        {
        	lblListHeaderText.setVisibility(View.VISIBLE);
        	lblListHeaderText.setText(headerTitle.getArticleText());
        }
        else
        {
        	lblListHeaderText.setVisibility(View.GONE);
        	//lblListHeaderText.setText("");
        }
        
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupCollapsed(groupPosition);
		Article headerTitle = (Article) getGroup(groupPosition);
		headerTitle.setTextVisible(true);
		
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupExpanded(groupPosition);
		Article headerTitle = (Article) getGroup(groupPosition);
		headerTitle.setTextVisible(false);
		
	}
}
