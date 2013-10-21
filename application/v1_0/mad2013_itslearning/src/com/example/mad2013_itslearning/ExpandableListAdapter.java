package com.example.mad2013_itslearning;

import java.util.List;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/* @author asampe
 * 
 * 
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter
{
	private Context _context;
	private List<Article> _listDataHeader; // header titles

	public ExpandableListAdapter(Context context, List<Article> listDataHeader)
	{
		this._context = context;
		this._listDataHeader = listDataHeader;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon)
	{
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
		txtListChild.setText(this._listDataHeader.get(groupPosition).getArticleText());

		return convertView;
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		Article headerTitle = (Article) getGroup(groupPosition);
		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		TextView lblListHeaderDate = (TextView) convertView.findViewById(R.id.lblListHeaderDate);
		TextView lblListHeaderText = (TextView) convertView.findViewById(R.id.lblListHeaderText);
		TextView lblListCode = (TextView) convertView.findViewById(R.id.lblListCode);
		
		lblListHeader.setText(headerTitle.getArticleHeader());
		lblListHeaderDate.setText(headerTitle.getArticleDate().toString());
		lblListCode.setText(headerTitle.getArticleCourseCode());
		
		if (headerTitle.isTextVisible())
		{
			lblListHeaderText.setVisibility(View.VISIBLE);
			lblListHeaderText.setText(headerTitle.getArticleSummary());
		}
		else
		{
			lblListHeaderText.setVisibility(View.GONE);
		}

		/*
		 * filter example
		 */
		if (headerTitle.getArticleCourseCode().equals("320B"))
		{
			//convertView.setVisibility(View.GONE);	
		}

		return convertView;

	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	@Override
	public void onGroupCollapsed(int groupPosition)
	{
		super.onGroupCollapsed(groupPosition);
		Article headerTitle = (Article) getGroup(groupPosition);
		headerTitle.setTextVisible(true);
	}

	@Override
	public void onGroupExpanded(int groupPosition)
	{
		super.onGroupExpanded(groupPosition);
		Article headerTitle = (Article) getGroup(groupPosition);
		headerTitle.setTextVisible(false);
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return 1;
	}
}
