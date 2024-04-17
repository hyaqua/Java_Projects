package com.example.studentai;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Student implements StudentAttendance {
    protected String name;
    protected String id;
    List<LocalDate> attendance;

    List<String> groups;

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
        attendance = new ArrayList<>();
        groups = new ArrayList<>();
    }
    public Student(String name, String id, ArrayList<LocalDate> attendance) {
        this.name = name;
        this.id = id;
        this.attendance = attendance;
        groups = new ArrayList<>();
    }
    public void addGroup(String name){
        groups.add(name);
    }
    public String ListGroups(){
        return Arrays.toString(groups.toArray());
    }
    public String ListDates() {
        String ret = "";
        for(LocalDate date : attendance){
            ret += date.toString() + " ";
        }
        return ret;
    }
    public boolean hasGroup(String name){
        return groups.contains(name);
    }
    @Override
    public void setAttendance(LocalDate date) {
        attendance.add(date);
    }
    public boolean getAttendance(LocalDate date) {
        return attendance.contains(date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + ' ' + id;
    }
}
