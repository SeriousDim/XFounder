package com.xproject.eightstudio.x_project.main;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

public class LocalData extends AppCompatActivity {
    SharedPreferences sPref;

    public void saveUser(String user_id) {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("user_id", user_id);
        ed.commit();
    }

    public void saveProject(String project_id) {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("project_id", project_id);
        ed.commit();
    }

    public String loadUser() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString("user_id", "");
        return savedText;
    }

    public String loadProject() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString("project_id", "");
        return savedText;
    }
}
