package com.example.myapplication.Adapters;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.myapplication.Fragments.HomePageFragment;
import com.example.myapplication.Fragments.PoliticalNewsFragment;
import com.example.myapplication.Fragments.BusinessNewsFragment;
import com.example.myapplication.Fragments.ScienceNewsFragment;
import com.example.myapplication.Fragments.SportsNewsFragment;
import com.example.myapplication.Fragments.TechnologicalNewsFragment;
import com.example.myapplication.Fragments.WorldNewsFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 6;

    //Stores the titles of the strings in an array
    private String tabTitles[] = new String[] { "WORLD", "BUSINESS", "POLITICS", "SPORTS", "TECHNOLOGY", "SCIENCE"};
    private Context context;


    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
           System.out.println("please god");
        return new  WorldNewsFragment();
        } else if (position == 1){
            return new BusinessNewsFragment();
        } else if (position == 2){
            return new PoliticalNewsFragment();
        } else if (position == 3){
            return new SportsNewsFragment();
        } else if (position == 4){
            return new TechnologicalNewsFragment();
        } else if (position == 5){
            return new ScienceNewsFragment();
        } else {
            return new WorldNewsFragment();
        }
    }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

