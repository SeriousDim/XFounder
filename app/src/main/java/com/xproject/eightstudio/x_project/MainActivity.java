package com.xproject.eightstudio.x_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.xproject.eightstudio.x_project.chat.ChatFragment;
import com.xproject.eightstudio.x_project.dataclasses.Company;
import com.xproject.eightstudio.x_project.dataclasses.Director;
import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.util.ArrayList;
import java.util.Random;

import io.gloxey.cfv.CFTextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int currentFragment;
    int lastFragment;
    Fragment now;
    Fragment[] fragments;
    Toolbar toolbar;
    CFTextView title;
    ListView lv;
    NewProjectListAdapter adapter;
    Intent intentAddCompany;
    MenuItem add, edit;

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

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title = (CFTextView) findViewById(R.id.my_title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerSlideAnimationEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragments = new Fragment[3];
        try {
            fragments[0] = CompanyHomeFragment.class.newInstance();
            fragments[1] = ChatFragment.class.newInstance();
            fragments[2] = TaskPager.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        intentAddCompany = new Intent(this, AddCompanyActivity.class);
        findViewById(R.id.add_comp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAddCompany);
            }
        });
        setFragmentClass(fragments[2]);
        navigation.setSelectedItemId(R.id.navigation_task);

        lv = findViewById(R.id.comp_list);
        Company[] c = new Company[5];
        for (int i = 1; i < 6; i++)
            c[i - 1] = new Company("My Company " + i, new Director(new Random().nextLong() + "", ""));
        adapter = new NewProjectListAdapter(this, c);
        lv.setAdapter(
                adapter
        );
        setCurrentCompany(0);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setCurrentCompany(i);
            }
        });

        lastFragment = 2;
    }

    public void setCurrentCompany(int company) {
        Company c = (Company) lv.getItemAtPosition(company);
        c.selected = true;
        adapter.currentComapny = company;
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "You select company", Toast.LENGTH_SHORT).show();
        // загрузка данных о другой компании здесь
    }

    public boolean setFragment(int item) {
        switch (item) {
            case R.id.navigation_home:
                setFragmentClass(fragments[0]);
                currentFragment = 0;
                title.setText(getResources().getString(R.string.title_home));
                invalidateOptionsMenu();
                return true;
            case R.id.navigation_chat:
                setFragmentClass(fragments[1]);
                currentFragment = 1;
                title.setText(getResources().getString(R.string.title_chat));
                invalidateOptionsMenu();
                return true;
            case R.id.navigation_task:
                setFragmentClass(fragments[2]);
                currentFragment = 2;
                title.setText(getResources().getString(R.string.title_tasks));
                invalidateOptionsMenu();
                return true;
        }
        return false;
    }

    public void openTask(Task task) {
        TaskViewFragment taskViewFragment = new TaskViewFragment();
        taskViewFragment.setTask(task);
        setFragmentClass(taskViewFragment);
        lastFragment = currentFragment;
        currentFragment = 3;
        title.setText(getResources().getString(R.string.current_task));
        invalidateOptionsMenu();
    }

    private void setFragmentClass(Fragment frag) {
        now = frag;
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contfrag, frag).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        /*if (currentFragment == 3 || currentFragment == 4)
            setFragment(R.id.navigation_task);*/
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (currentFragment>2){
            switch(lastFragment){
                case 0:
                    setFragment(R.id.navigation_home);
                    break;
                case 1:
                    setFragment(R.id.navigation_chat);
                    break;
                case 2:
                    setFragment(R.id.navigation_task);
                    break;
            }
        }
        /*else {
            super.onBackPressed();
        }*/
    }

    public void updateTasks() {
        ((TaskPager) fragments[2]).getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        add = menu.findItem(R.id.add_task);
        edit = menu.findItem(R.id.edit);
        switch (this.currentFragment) {
            case 0:
            case 1:
                add.setVisible(false);
                edit.setVisible(false);
                break;
            case 2:
                add.setVisible(true);
                edit.setVisible(false);
                break;
            case 3:
                add.setVisible(false);
                edit.setVisible(true);
                break;
            case 4:
                add.setVisible(false);
                edit.setVisible(false);
                break;
            case 5:
                add.setVisible(false);
                edit.setVisible(false);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                addTask();
                return true;
            case R.id.edit:
                openTaskEdit(((TaskViewFragment)now).getTask());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addTask() {
        setFragmentClass(new TaskCreateFragment());
        lastFragment = currentFragment;
        currentFragment = 4;
        invalidateOptionsMenu();
    }

    public void openTaskEdit(Task task) {
        TaskEdit te = new TaskEdit();
        te.setTask(task);
        setFragmentClass(te);
        currentFragment = 5;
        invalidateOptionsMenu();
    }
}
