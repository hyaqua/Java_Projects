package com.example.studentai;

import java.util.ArrayList;

public class Grupes extends Group{
    private String name;
    private ArrayList<Student> list;

    public Grupes(String name) {
        this.name = name;
        this.students = SingletonStudents.getInstance();
    }
    @Override
    public void addStudent(Student student) {
        students.addGroup(student, name);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return this.getName();
    }
}
