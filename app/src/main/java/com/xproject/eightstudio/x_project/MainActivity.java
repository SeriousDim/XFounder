package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.xproject.eightstudio.x_project.chat.ChatFragment;
import com.xproject.eightstudio.x_project.profile.ProfileListFragment;

public class MainActivity extends AppCompatActivity {

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        storage = Storage.getInstance();

        fragments = new Fragment[5];
        try{
            fragments[0] = ChatFragment.class.newInstance();
            fragments[1] = CompanyListFragment.class.newInstance();
            fragments[2] = CompanyHomeFragment.class.newInstance();
            fragments[3] = ProfileListFragment.class.newInstance();
        } catch(Exception e){
            e.printStackTrace();
        }

        setFragmentClass(fragments[1]);
    }

    private boolean setFragment(int item){
        switch(item){
            case R.id.navigation_companies:
                setFragmentClass(fragments[1]);
                return true;
            case R.id.navigation_profile:
                setFragmentClass(fragments[3]);
                return true;
            case R.id.navigation_chat:
                setFragmentClass(fragments[0]);
                return true;
            case R.id.navigation_home:
                setFragmentClass(fragments[2]);
                return true;
        }
        return false;
    }

    private void setFragmentClass(Fragment frag){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contfrag, frag).commit();
    }

}
