package com.xproject.eightstudio.x_project.dataclasses;

import android.graphics.Bitmap;

import java.util.ArrayList;

/*
 *  This class is written by
 *  (C) Dmitrii Lykov, 2019
 */

public class Company {

    public String name;
    public Bitmap logo;
    public Director director;
    public ArrayList<Project> projects;
    public ArrayList<Order> orders;
    public ArrayList<Employee> employees;
    public boolean selected;

    public Company(String name, Director director) {
        this.name = name;
        this.director = director;
        this.projects = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.selected = false;
    }

    public void addProject(Project p){
        this.projects.add(p);
    }

    public void deleteProject(int index){
        this.projects.remove(index);
    }

    public Project getProject(int index){
        return this.projects.get(index);
    }

    public int getEmployeeAmount(){
        return employees.size();
    }

}
