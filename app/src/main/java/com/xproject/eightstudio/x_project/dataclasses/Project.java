package com.xproject.eightstudio.x_project.dataclasses;

import java.util.ArrayList;
import java.util.Random;

public class Project {

    public String name;
    int color;
    ArrayList<Task> tasks;
    public int priority;
    public Employee teamlead;
    public boolean favorite, complete;

    public Project(String name, int prority, Employee teamlead) {
        this.name = name;
        this.priority = prority;
        this.favorite = false;
        this.complete = false;
        this.teamlead = teamlead;
        this.tasks = new ArrayList<>();
        this.color = Colors.colors[new Random().nextInt(9)];
    }

    public void addTask(Task t){
        this.tasks.add(t);
    }

    public void deleteTask(int index){
        this.tasks.remove(index);
    }

    public Task getTask(int index){
        return this.tasks.get(index);
    }

    public int getEmployeesAmount(){
        int res = 0;
        for (Task t: tasks){
            res += t.getEmployeersAmount();
        }
        return res;
    }

    public int getTasksAmount(){
        int res = 0;
        for (Task t: tasks){
            res++;
        }
        return res;
    }

    public int getItemsAmount(){
        int res = 0;
        for (Task i: tasks){
            res += i.getItemsAmount();
        }
        return res;
    }

    public int getCompleteTasksAmount(){
        int res = 0;
        for (Task t:tasks){
            if (t.complete)
                res++;
        }
        return res;
    }

    public int getCompleteItemsAmount(){
        int res = 0;
        for (Task i: tasks){
            res += i.getCompletedItemsAmount();
        }
        return res;
    }

}
