package com.xproject.eightstudio.x_project.dataclasses;

public class Project {

    public String name;
    public int priority;
    public Employee teamlead;
    public boolean favorite, complete;

    public Project(String name, int prority, Employee teamlead) {
        this.name = name;
        this.priority = prority;
        this.favorite = false;
        this.complete = false;
        this.teamlead = teamlead;
    }

}
