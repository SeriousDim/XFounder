package com.xproject.eightstudio.x_project.dataclasses;

public class TaskClass {

    public String title;
    public String creator;
    public String task_id;

    public TaskClass(String title, String creator, String task_id) {
        this.creator = creator;
        this.title = title;
        this.task_id = task_id;
    }
}
