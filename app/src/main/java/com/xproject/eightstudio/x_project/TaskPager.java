package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskPager extends Fragment {
    ViewPagerAdapter viewpager;
    View card;

    private void setupViewPager(ViewPager viewPager) {
        viewpager = new ViewPagerAdapter(getFragmentManager());
        viewpager.addFragment(new TaskFragment(), getResources().getString(R.string.pending));
        viewpager.addFragment(new TaskFragment(), getResources().getString(R.string.in_progress));
        viewpager.addFragment(new TaskFragment(), getResources().getString(R.string.done));
        viewPager.setAdapter(viewpager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (card == null) {
            card = inflater.inflate(R.layout.fragment_task_pager, container, false);
            ViewPager vp = card.findViewById(R.id.v_pager);
            setupViewPager(vp);
            ((TabLayout) card.findViewById(R.id.tabs)).setupWithViewPager(vp);
        }

        return card;
    }
}
