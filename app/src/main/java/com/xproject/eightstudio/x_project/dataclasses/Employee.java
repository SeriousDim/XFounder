package com.xproject.eightstudio.x_project.dataclasses;

import java.util.ArrayList;

public class Employee {

    public String name, surname;
    public String about;
    public ArrayList<TaskClass> tasks; // reference

    public Employee(String name, String surname){
        this.name = name;
        this.surname = surname;
        this.tasks = new ArrayList<>();
    }

    public void addTask(TaskClass p){
        tasks.add(p);
    }

    public void deleteTask(int index){
        tasks.remove(index);
    }

    public TaskClass getTask(int index){
        return this.tasks.get(index);
    }

    /*public void addTask(TaskClass.Item p){
        items.add(p);
    }

    public void deleteTask(int index){
        items.remove(index);
    }

    public TaskClass.Item getTask(int index){
        return this.items.get(index);
    }*/

}
