package com.xproject.eightstudio.x_project.main;

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
import com.xproject.eightstudio.x_project.auth.LoginFragment;
import com.xproject.eightstudio.x_project.general.Project;
import com.xproject.eightstudio.x_project.general.Projects;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.home.RequestFragment;
import com.xproject.eightstudio.x_project.chat.ChatFragment;
import com.xproject.eightstudio.x_project.home.HomeFragment;
import com.xproject.eightstudio.x_project.profile.ProfileFragment;
import com.xproject.eightstudio.x_project.project.CreateProjectFragment;
import com.xproject.eightstudio.x_project.project.SearchProjectFragment;
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

    public int currentFragment = -1;
    public ArrayList<Project> projects;
    public Fragment now;
    public Fragment[] fragments;
    public Toolbar toolbar;
    public CFTextView title;
    public ListView lv;
    public NewProjectListAdapter adapter;
    public MenuItem add, edit, addProj, request;
    public MenuItem[] icons;
    public int currentIcon = 0;
    public BottomNavigationView navigation;

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
                saveProject("");
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
        initFragments();
        findViewById(R.id.add_comp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProgress(true);
                setFragmentClass(new SearchProjectFragment());
                currentFragment = 7;
                title.setText(getResources().getString(R.string.projects));
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                invalidateOptionsMenu();
            }
        });
        if (loadUser().equals("")) {
            openLogin();
        } else {
            loginSuccess();
        }
        lv = findViewById(R.id.comp_list);
        setProgress(false);
    }


    private void openLogin() {
        navigation.setVisibility(View.GONE);
        getSupportActionBar().hide();
        setFragmentClass(new LoginFragment());
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        currentFragment = 13;
    }

    public void addProjectsToNavigationView(ProjectResponse response) {
        adapter = new NewProjectListAdapter(this, response.projects);
        lv.setAdapter(adapter);
        if (response.projects.size() > 0)
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
                initFragments();
                navigation.setSelectedItemId(R.id.navigation_task);

            }
        });
        navigation.setSelectedItemId(R.id.navigation_task);
        navigation.setVisibility(View.VISIBLE);
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void initFragments() {
        fragments = new Fragment[4];
        try {
            fragments[0] = HomeFragment.class.newInstance();
            fragments[1] = ChatFragment.class.newInstance();
            fragments[2] = TaskPager.class.newInstance();
            fragments[3] = SearchProjectFragment.class.newInstance();
        } catch (Exception e) {
            Log.d("tagged", e.toString());
        }
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

    public void acceptRequest(String id, int pos) {
        setProgress(true);
        ((RequestFragment) now).acceptRequest(id, pos);
    }

    public void deleteRequest(String id, int pos) {
        setProgress(true);
        ((RequestFragment) now).deleteRequest(id, pos);
    }

    public boolean setFragment(int item) {
        if (loadProject().equals("")) {
            Toast.makeText(this, "Присоединитесь к существующему или создайте свой проект", Toast.LENGTH_SHORT).show();
            setFragmentClass(fragments[2]);
            currentFragment = 2;
            title.setText(getResources().getString(R.string.title_tasks));
        } else {
            switch (item) {
                case R.id.navigation_home:
                    setFragmentClass(fragments[0]);
                    setProgress(true);
                    ((HomeFragment) fragments[0]).getList(loadProject());
                    currentFragment = 0;
                    title.setText(getResources().getString(R.string.title_home));
                    return true;
                case R.id.navigation_chat:
                    setFragmentClass(fragments[1]);
                    currentFragment = 1;
                    Project p = (Project) lv.getItemAtPosition(adapter.currentProject);
                    title.setText(getResources().getString(R.string.title_chat) + " " + p.title);
                    return true;
                case R.id.navigation_task:
                    setFragmentClass(fragments[2]);
                    currentFragment = 2;
                    title.setText(getResources().getString(R.string.title_tasks));
                    return true;
            }
        }
        return false;
    }

    public void openTask(Task task) {
        TaskViewFragment taskViewFragment = new TaskViewFragment();
        taskViewFragment.setTask(task);
        setFragmentClass(taskViewFragment);
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
        currentFragment = 6;
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        invalidateOptionsMenu();
    }

    public void setFragmentClass(Fragment frag) {
        now = frag;
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contfrag, frag).commit();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            switch (currentFragment) {
                case 3:
                case 4:
                    setFragment(R.id.navigation_task);
                    updateTasks();
                    break;
                case 5:
                    openTask(((TaskCreateFragment) now).getTask());
                    break;
                case 8:
                    setProgress(true);
                    setFragmentClass(fragments[3]);
                    currentFragment = 7;
                    title.setText(getResources().getString(R.string.projects));
                    break;
                case 9:
                    setFragment(R.id.navigation_home);
                    break;
                case 13:
                    openLogin();
                    break;
            }
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
            invalidateOptionsMenu();
        }
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
        request = menu.findItem(R.id.requests);
        add.setVisible(false);
        edit.setVisible(false);
        addProj.setVisible(false);
        request.setVisible(false);
        icons = new MenuItem[]{add, edit, addProj, request};
        if (loadProject().equals("")) {
            if (currentFragment == 7) {
                addProj.setVisible(true);
                currentIcon = 2;
            }
        } else {
            icons[currentIcon].setVisible(false);
            switch (this.currentFragment) {
                case 2:
                    icons[0].setVisible(true); // add task
                    currentIcon = 0;
                    break;
                case 3:
                    icons[1].setVisible(true); // edit task
                    currentIcon = 1;
                    break;
                case 7:
                    icons[2].setVisible(true); // add project
                    currentIcon = 2;
                    break;
                case 0:
                    icons[3].setVisible(true); // requests
                    currentIcon = 3;
                    break;
                default:
                    break;
            }
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
                openCreateProject();
                return true;
            case R.id.requests:
                openRequests();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCreateProject() {
        setFragmentClass(new CreateProjectFragment());
        currentFragment = 8;
        title.setText(R.string.create_new_project);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openRequests() {
        now = new RequestFragment();
        setFragmentClass(now);
        title.setText(getResources().getString(R.string.requests));
        currentFragment = 9;
        invalidateOptionsMenu();
    }

    public void addTask() {
        setFragmentClass(new TaskCreateFragment());
        currentFragment = 4;
        invalidateOptionsMenu();
    }

    public void openTaskEdit(Task task) {
        TaskCreateFragment te = new TaskCreateFragment();
        te.setTask(task);
        now = te;
        setFragmentClass(te);
        currentFragment = 5;
        invalidateOptionsMenu();
    }

    public void loginSuccess() {
        getSupportActionBar().show();
        setProgress(true);
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getProjectsForNav();
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

