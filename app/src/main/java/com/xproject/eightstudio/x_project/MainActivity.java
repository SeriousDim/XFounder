package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.xproject.eightstudio.x_project.chat.ChatFragment;
import com.xproject.eightstudio.x_project.dataclasses.TaskClass;

public class MainActivity extends AppCompatActivity {

    int currentFragment;
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

        fragments = new Fragment[4];
        try {
            fragments[0] = CompanyHomeFragment.class.newInstance();
            fragments[1] = ChatFragment.class.newInstance();
            fragments[2] = TaskPager.class.newInstance();
            fragments[3] = TaskViewFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFragmentClass(fragments[2]);
        navigation.setSelectedItemId(R.id.navigation_task);
    }

    public boolean setFragment(int item) {
        switch (item) {
            case R.id.navigation_home:
                setFragmentClass(fragments[0]);
                currentFragment = 0;
                return true;
            case R.id.navigation_chat:
                setFragmentClass(fragments[1]);
                currentFragment = 1;
                return true;
            case R.id.navigation_task:
                setFragmentClass(fragments[2]);
                currentFragment = 2;
                return true;
        }
        return false;
    }

    public void openTask(TaskClass task) {
        TaskViewFragment taskViewFragment = (TaskViewFragment) fragments[3];
        taskViewFragment.setTask(task);
        setFragmentClass(taskViewFragment);
        currentFragment = 3;
    }

    private void setFragmentClass(Fragment frag) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contfrag, frag).commit();
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == 3)
            setFragment(R.id.navigation_task);
    }
}
