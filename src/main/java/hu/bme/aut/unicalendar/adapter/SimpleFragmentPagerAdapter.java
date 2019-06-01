package hu.bme.aut.unicalendar.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.fragments.EventFragment;
import hu.bme.aut.unicalendar.fragments.RequirementFragment;
import hu.bme.aut.unicalendar.fragments.SubjectFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    SparseArray<Fragment> registeredFragments;

    public void updateVisibility() {
        for(int i = 0; i < registeredFragments.size(); i++) {
            int key = registeredFragments.keyAt(i);
            Fragment fragment = registeredFragments.get(key);
            if (fragment instanceof EventFragment)
            {
                ((EventFragment)fragment).invalidate();
                break;
            }
        }
    }

    public void handleSelectDateClick(int position) {
        Fragment fragment = this.getRegisteredFragment(position);
        if (fragment instanceof EventFragment) {
            ((EventFragment)fragment).handleSelectDateClick();
        }
    }

    public interface Invalidatable {
        public void invalidate();
    }

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        registeredFragments = new SparseArray<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EventFragment();
        }
        else if (position == 1) {
            return new SubjectFragment();
        }
        else {
            return new RequirementFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return mContext.getString(R.string.first_title);
            case 1: return mContext.getString(R.string.second_title);
            default: return mContext.getString(R.string.third_title);
        }
    }

    public void updatePages() {
        for(int i = 0; i < registeredFragments.size(); i++) {
            int key = registeredFragments.keyAt(i);
            Fragment fragment = registeredFragments.get(key);
            if (fragment != null) {
                ((Invalidatable)fragment).invalidate();
            }
        }
    }
}
