package com.material_photo_math.trees.dateconverter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by trees on 5/29/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];  //this store the tab collection and which is used by the pager strip later on
    int noOfTabs;           //this is for total number of tabs
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[],int mNumberOfTabs ) {
        super(fm);

        this.noOfTabs=mNumberOfTabs;
        this.titles=mTitles;
    }


    ///this fragment is implemented to return the fragment to be return on corresponding position
    //eg
    //if position is 1
    //the getItem() retuns the Tab1() instance
    //if the position is 2 \
    //then this method returns the 2nd fragment and similiar
    @Override
    public Fragment getItem(int position) {

        if(position==0)
        {
            return new Tab2();
        }
        if(position==1)
        {
            return new Tab1();
        }
        return  null;
    }


    //this returns the no of tabs for tab strips
    @Override
    public int getCount() {
        return noOfTabs;
    }

    //this returns the titles of the tabs for tab strips

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
