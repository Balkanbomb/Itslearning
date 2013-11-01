package se.mah.kd330a.project.itsl;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import se.mah.kd330a.project.itsl.TabFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment fragment = new TabFragment();
		Bundle args = new Bundle();
		args.putInt(TabFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		
		//return FeedManager.getCorses().size();
		return 5;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return "All Courses";
		case 1:
			return "section 2";
		case 2:
			return "section 3";
		case 3:
			return "section 4";
		case 4:
			return "fifth really really long tab";
		}
		return null;
	}
}