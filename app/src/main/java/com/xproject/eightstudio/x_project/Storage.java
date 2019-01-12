package com.xproject.eightstudio.x_project;

import com.xproject.eightstudio.x_project.dataclasses.Company;
import com.xproject.eightstudio.x_project.dataclasses.Director;
import com.xproject.eightstudio.x_project.dataclasses.Employee;
import com.xproject.eightstudio.x_project.dataclasses.Project;
import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.util.ArrayList;

public class Storage {
    private static final Storage ourInstance = new Storage();
    public int currentCompany;
    public ArrayList<Company> companies;

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
        companies = new ArrayList<>();
        currentCompany = 0;
        companies.add(new Company("My Company", new Director("Name Surname", "CEO")));
        Employee e = new Employee("Василий", "Пупкин");
        e.about = "It`s me";
        companies.get(0).employees.add(
                e
        );

        Company c = companies.get(0);
        c.addProject(new Project(
                "Our super project",
                1,
                c.employees.get(0)
        ));

        Project p = c.getProject(0);
        p.addTask(new Task(
                p,
                "Make adapters 7",
                "Just do it 7",
                c.employees.get(0))
        );
        e.addTask(p.tasks.get(0));
    }
}
