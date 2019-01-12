package com.xproject.eightstudio.x_project.dataclasses;

import java.util.ArrayList;

public class Employee {

    public String name, surname;
    public String about;
    public ArrayList<Task> tasks; // reference

    public Employee(String name, String surname){
        this.name = name;
        this.surname = surname;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task p){
        tasks.add(p);
    }

    public void deleteTask(int index){
        tasks.remove(index);
    }

    public Task getTask(int index){
        return this.tasks.get(index);
    }

    /*public void addTask(Task.Item p){
        items.add(p);
    }

    public void deleteTask(int index){
        items.remove(index);
    }

    public Task.Item getTask(int index){
        return this.items.get(index);
    }*/

}
