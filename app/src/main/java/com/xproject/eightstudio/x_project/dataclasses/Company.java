package com.xproject.eightstudio.x_project.dataclasses;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Company {

    public String name;
    public Bitmap logo;
    public Director director;
    public ArrayList<Order> orders;
    public ArrayList<Employee> employees;
    public boolean selected;

    public Company(String name, Director director) {
        this.name = name;
        this.director = director;
        this.orders = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.selected = false;
    }
}
