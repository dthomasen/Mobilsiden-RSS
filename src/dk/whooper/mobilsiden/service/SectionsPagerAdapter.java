package dk.whooper.mobilsiden.service;

import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.screens.AnmeldelserFragment;
import dk.whooper.mobilsiden.screens.NyhederFragment;
import dk.whooper.mobilsiden.screens.WebTvFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	private Context context;

	public SectionsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch(position){
		case 0:
			return new NyhederFragment();
		case 1:
			return new AnmeldelserFragment();
		case 2:
			return new WebTvFragment();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return context.getString(R.string.title_section1).toUpperCase();
		case 1:
			return context.getString(R.string.title_section2).toUpperCase();
		case 2:
			return context.getString(R.string.title_section3).toUpperCase();
		}
		return null;
	}
}