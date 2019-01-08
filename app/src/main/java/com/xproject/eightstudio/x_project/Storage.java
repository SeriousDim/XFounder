package com.xproject.eightstudio.x_project;

import com.xproject.eightstudio.x_project.dataclasses.Company;
import com.xproject.eightstudio.x_project.dataclasses.Director;
import com.xproject.eightstudio.x_project.dataclasses.Employee;
import com.xproject.eightstudio.x_project.dataclasses.Project;
import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.util.ArrayList;

public class Storage {
    private static final Storage ourInstance = new Storage();
    ArrayList<Company> companies;

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
        companies = new ArrayList<>();
        companies.add(new Company("My Company", new Director("Some", "One", "CEO")));

        companies.get(0).employees.add(
                new Employee("Nikita", "Yakovlev", "CEO", 10000)
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
                "Make adapters",
                "Just do it",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters",
                "Just do it",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters",
                "Just do it",
                c.employees.get(0))
        );
        p.getTask(1).setComplete(true);
    }
}
