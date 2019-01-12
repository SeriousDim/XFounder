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
        Employee e = new Employee("Василий", "Пупкин", 10000);
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
                "Make adapters",
                "Just do it",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 1",
                "Just do it 1",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 2",
                "Just do it 2",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 2",
                "Just do it 2",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 3",
                "Just do it 3",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 4",
                "Just do it 4",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 5",
                "Just do it 5",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 6",
                "Just do it 6",
                c.employees.get(0))
        );
        p.addTask(new Task(
                p,
                "Make adapters 7",
                "Just do it 7",
                c.employees.get(0))
        );
        p.getTask(1).setComplete(true);

        e.addTask(p.tasks.get(0));
        e.addTask(p.tasks.get(1));
        e.addTask(p.tasks.get(2));
        e.addTask(p.tasks.get(3));
        e.addTask(p.tasks.get(4));
        e.addTask(p.tasks.get(5));
        e.addTask(p.tasks.get(6));
        e.addTask(p.tasks.get(7));
    }
}
