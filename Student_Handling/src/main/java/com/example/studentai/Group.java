package com.example.studentai;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public abstract class Group implements attendance {
    SingletonStudents students;
    @Override
    public void setAttendance(LocalDate date, ArrayList<Student> students) {
        this.students.setAttendance(date, students);
    }
    public void setAttendance(LocalDate date, Student student){
        this.students.setAttendance(date, student);
    }
    public void setAttendance(LocalDate date, int idx){
        students.setAttendance(date, idx);
    }
    public boolean[] getAttendance(LocalDate date){
        return students.getAttendance(date);
    }
    public Student getStudent(String id){
        return students.getStudent(id);
    }
    public void addStudent(Student student){
        students.addStudent(student);
    }
    public void sort(){
        students.SortStudents();
    }
}


