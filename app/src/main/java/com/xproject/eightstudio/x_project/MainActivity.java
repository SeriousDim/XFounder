package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.chat.ChatFragment;
import com.xproject.eightstudio.x_project.dataclasses.Project;
import com.xproject.eightstudio.x_project.home.HomeFragment;
import com.xproject.eightstudio.x_project.profile.ProfileFragment;
import com.xproject.eightstudio.x_project.task.Task;
import com.xproject.eightstudio.x_project.task.TaskCreateFragment;
import com.xproject.eightstudio.x_project.task.TaskPager;
import com.xproject.eightstudio.x_project.task.TaskViewFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.gloxey.cfv.CFTextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ProjectResponse {
    ArrayList<Project> projects;
    String name;
    String job;
}

public class MainActivity extends LocalData
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Projects pro = retrofit.create(Projects.class);

    int currentFragment;
    int lastFragment;
    ArrayList<Project> projects;
    Fragment now;
    Fragment[] fragments;
    Toolbar toolbar;
    CFTextView title;
    ListView lv;
    NewProjectListAdapter adapter;
    MenuItem add, edit, addProj, resume;
    BottomNavigationView navigation;

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

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title = findViewById(R.id.my_title);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerSlideAnimationEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser("");
                openLogin();
            }
        });
        findViewById(R.id.avatar).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProfile(loadUser());
                    }
                }
        );
        fragments = new Fragment[5];
        try {
            fragments[0] = HomeFragment.class.newInstance();
            fragments[1] = ChatFragment.class.newInstance();
            fragments[2] = TaskPager.class.newInstance();
            fragments[3] = SearchProjectFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.add_comp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProgress(true);
                setFragmentClass(new SearchProjectFragment());
                lastFragment = currentFragment;
                currentFragment = 7;
                title.setText(getResources().getString(R.string.projects));
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                invalidateOptionsMenu();
            }
        });
        if (loadUser() == "") {
            openLogin();
        } else {
            loginSuccess();
        }
        lv = findViewById(R.id.comp_list);
        setProgress(false);
        lastFragment = 2;
    }


    private void openLogin() {
        navigation.setVisibility(View.GONE);
        getSupportActionBar().hide();
        setFragmentClass(new LoginFragment());
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void addProjectsToNavigationView(ProjectResponse response) {
        adapter = new NewProjectListAdapter(this, response.projects);
        lv.setAdapter(adapter);
        setCurrentCompany(0);
        TextView name, job;
        name = findViewById(R.id.nav_name);
        job = findViewById(R.id.nav_job);
        name.setText(response.name);
        job.setText(response.job);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setCurrentCompany(i);
                updateTasks();
            }
        });
        ((TaskPager) fragments[2]).setLocalID(loadUser());
        navigation.setSelectedItemId(R.id.navigation_task);
    }

    public void setProgress(boolean vis) {
        int visible = vis ? View.VISIBLE : View.GONE;
        findViewById(R.id.loading).setVisibility(visible);
    }

    public void setCurrentCompany(int position) {
        Project p = (Project) lv.getItemAtPosition(position);
        p.selected = true;
        adapter.currentProject = position;
        saveProject(p.p_id);
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        adapter.notifyDataSetChanged();
    }

    public boolean setFragment(int item) {
        switch (item) {
            case R.id.navigation_home:
                setFragmentClass(fragments[0]);
                setProgress(true);
                ((HomeFragment) fragments[0]).getList(loadProject());
                currentFragment = 0;
                title.setText(getResources().getString(R.string.title_home));
                invalidateOptionsMenu();
                return true;
            case R.id.navigation_chat:
                setFragmentClass(fragments[1]);
                currentFragment = 1;
                Project p = (Project) lv.getItemAtPosition(adapter.currentProject);
                title.setText(getResources().getString(R.string.title_chat) + " " + p.title);
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


    public void openProfile(String id) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setID(id);
        title.setText(getResources().getString(R.string.title_employees));
        setProgress(true);
        setFragmentClass(profileFragment);
        lastFragment = currentFragment;
        currentFragment = 6;
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        invalidateOptionsMenu();
    }

    public void setFragmentClass(Fragment frag) {
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
        } else if (currentFragment > 2) {
            switch (lastFragment) {
                case 0:
                    setFragment(R.id.navigation_home);
                    break;
                case 1:
                    setFragment(R.id.navigation_chat);
                    break;
                case 2:
                    setFragment(R.id.navigation_task);
                    break;
                case 6:
                    setFragmentClass(new LoginFragment());
                    break;
                case 7:
                    setFragmentClass(fragments[3]);
                    lastFragment = currentFragment;
                    currentFragment = 7;
                    title.setText(getResources().getString(R.string.projects));
                    ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                    invalidateOptionsMenu();
                case 8:
                    setProgress(true);
                    setFragmentClass(new SearchProjectFragment());
                    lastFragment = currentFragment;
                    currentFragment = 7;
                    title.setText(getResources().getString(R.string.projects));
                    ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                    invalidateOptionsMenu();
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
        addProj = menu.findItem(R.id.create_proj);
        resume = menu.findItem(R.id.give_resume);
        switch (this.currentFragment) {
            case 0:
            case 1:
            case 8:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 2:
                add.setVisible(true);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 3:
                add.setVisible(false);
                edit.setVisible(true);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 4:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 5:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 6:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 7:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(true);
                resume.setVisible(false);
                break;
            case 9: // фрагмент с подачей заявки
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
                break;
            case 11:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(true);
                break;
            case 10:
                add.setVisible(false);
                edit.setVisible(false);
                addProj.setVisible(false);
                resume.setVisible(false);
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
                openTaskEdit(((TaskViewFragment) now).getTask());
                return true;
            case R.id.create_proj:
                setFragmentClass(new CreateProjectFragment());
                lastFragment = currentFragment;
                currentFragment = 8;
                title.setText(R.string.create_new_project);
                invalidateOptionsMenu();
                return true;
            case R.id.give_resume:
                openResume();
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

    public void openResume(){
        //setFragmentClass(...);
        Toast.makeText(this, "Resume", Toast.LENGTH_LONG).show();
        lastFragment = currentFragment;
        currentFragment = 9;
        //invalidateOptionsMenu();
    }

    public void addTask() {
        setFragmentClass(new TaskCreateFragment());
        lastFragment = currentFragment;
        currentFragment = 4;
        invalidateOptionsMenu();
    }

    public void openTaskEdit(Task task) {
        TaskCreateFragment te = new TaskCreateFragment();
        te.setTask(task);
        setFragmentClass(te);
        currentFragment = 5;
        invalidateOptionsMenu();
    }

    public void loginSuccess() {
        navigation.setVisibility(View.VISIBLE);
        getSupportActionBar().show();
        getProjectsForNav();
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void getProjectsForNav() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("user_id", loadUser());
        getDataParams.put("command", "getMyProjects");
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ProjectResponse resp = gson.fromJson(response.body().string(), ProjectResponse.class);
                    projects = resp.projects;
                    addProjectsToNavigationView(resp);
                } catch (IOException e) {
                    Log.d("tagged", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
