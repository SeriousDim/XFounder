package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Storage storage;
    Fragment[] fragments;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return setFragment(item.getItemId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        storage = Storage.getInstance();

        fragments = new Fragment[5];
        try{
            fragments[0] = ChatFragment.class.newInstance();
            fragments[1] = CompanyListFragment.class.newInstance();
            fragments[2] = CompanyHomeFragment.class.newInstance();
            fragments[3] = EmployeeListFragment.class.newInstance();
            fragments[4] = ProjectListFragment.class.newInstance();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean setFragment(int item){
        switch(item){
            case R.id.navigation_companies:
                setFragmentClass(fragments[1]);
                return true;
            case R.id.navigation_employees:
                setFragmentClass(fragments[3]);
                return true;
            case R.id.navigation_chat:
                setFragmentClass(fragments[0]);
                return true;
            case R.id.navigation_projects:
                setFragmentClass(fragments[4]);
                return true;
            case R.id.navigation_home:
                setFragmentClass(fragments[2]);
                return true;
        }
        return false;
    }

    private void setFragmentClass(Fragment frag){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, frag).commit();
    }

}
