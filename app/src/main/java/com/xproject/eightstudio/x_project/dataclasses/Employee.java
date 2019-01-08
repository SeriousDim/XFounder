package com.xproject.eightstudio.x_project.dataclasses;

import android.util.Log;

import java.util.ArrayList;

public class Employee {

    public String name, surname;
    public String job;
    public int level;
    public int salary, score;
    ArrayList<Project> projects; // reference
    ArrayList<Task.Item> items; // reference

    public Employee(String name, String surname, String job, int salary){
        this.name = name;
        this.surname = surname;
        this.job = job;
        this.salary = salary;
        this.projects = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public void addProject(Project p){
        projects.add(p);
    }

    public void deleteProject(int index){
        projects.remove(index);
    }

    public Project getProject(int index){
        return this.projects.get(index);
    }

    public void addTask(Task.Item p){
        items.add(p);
    }

    public void deleteTask(int index){
        items.remove(index);
    }

    public Task.Item getTask(int index){
        return this.items.get(index);
    }

}
