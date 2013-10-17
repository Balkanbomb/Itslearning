package com.example.mad2013_itslearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
 
public class MainActivity extends Activity {
 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Article> listDataHeader;
    HashMap<Article, List<String>> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		//Change color actionbar
		ColorDrawable colorDrawable = new ColorDrawable();
        ActionBar actionBar = getActionBar();
		colorDrawable.setColor(0x000000);
	
        actionBar.setBackgroundDrawable(colorDrawable);

		
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
            //	parent.expandGroup(groupPosition, true);
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
            	
              /*  Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show(); */
                
              
            }
        });
 
        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
              /*  Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show(); */
              
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
 /*               Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show(); */
                parent.collapseGroup(groupPosition);
                return false;
            }
        });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<Article>();
        listDataChild = new HashMap<Article, List<String>>();
 
        // Adding child data
                
        listDataHeader.add(new Article("New", "2013-10-15 10.30", "For this we will need your User stories and one or more of them will be put into the first sprint."));
        listDataHeader.add(new Article("Not so new", "2013-10-14 10.30", "We will need your User stories and one or more of them will be put into the first sprint."));
        listDataHeader.add(new Article("Old", "2013-10-13 10.30", "For this we will need your User stories."));
        listDataHeader.add(new Article("Oldest", "2013-10-12 10.30", "For this we will need your User stories."));
        
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
    
        List<String> nowShowing = new ArrayList<String>();
        //nowShowing.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
        nowShowing.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
        
        List<String> comingSoon = new ArrayList<String>();
    //    comingSoon.add("For this we will need your User stories and one or more of them will be put into the first sprint.");
        comingSoon.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
        
        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), comingSoon);
    }
}
