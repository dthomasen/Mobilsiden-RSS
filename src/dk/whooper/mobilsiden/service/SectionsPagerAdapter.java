package dk.whooper.mobilsiden.service;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.screens.AnmeldelserFragment;
import dk.whooper.mobilsiden.screens.NyhederFragment;
import dk.whooper.mobilsiden.screens.TipsFragment;
import dk.whooper.mobilsiden.screens.WebTvFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NyhederFragment();
            case 1:
                return new AnmeldelserFragment();
            case 2:
                return new WebTvFragment();
            case 3:
                return new TipsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
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
            case 3:
                return context.getString(R.string.title_section4).toUpperCase();
        }
        return null;
    }
}