package com.xproject.eightstudio.x_project.dataclasses;

import java.util.ArrayList;

public class Task {

    public Project project; // reference
    public String name;
    public String description;
    boolean complete;
    public Employee teamlead;
    ArrayList<Item> items;

    public Task(Project project, String name, String description, Employee teamlead) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.complete = false;
        this.teamlead = teamlead;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public void setCompleteItem(int index, boolean com){
        this.items.get(index).comlete = com;
        if (com) {
            for (Item i : items) {
                if (!i.comlete) {
                    return;
                }
            }
            setComplete(true);
        }
        else if (this.complete)
            setComplete(false);
    }

    public void setComplete(boolean complete){
        this.complete = complete;
    }

    public int getCompletedItemsAmount(){
        int a = 0;
        for (Item i: items){
            if (i.comlete)
                a++;
        }
        return a;
    }

    public int getEmployeersAmount(){
        int res = 0;
        for (Item i: items){
            res += i.getEmployeersAmount();
        }
        return res;
    }

    public int getItemsAmount(){
        int res = 0;
        for (Item i:items){
            res++;
        }
        return res;
    }

    public class Item{
        String title;
        boolean comlete;
        ArrayList<Employee> employees; // references

        Item(String title){
            this.title = title;
            this.comlete = false;
            this.employees = new ArrayList<>();
        }

        public void addEmployee(Employee e){
            this.employees.add(e);
        }

        public void deleteEmployee(int index){
            this.employees.remove(index);
        }

        public Employee getEmployee(int index){
            return employees.get(index);
        }

        public int getEmployeersAmount(){
            return this.employees.size();
        }
    }

}
