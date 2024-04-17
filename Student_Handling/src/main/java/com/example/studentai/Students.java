package com.example.studentai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Students extends Group {
    List<Grupes> grupes;

    Students(){
        grupes = new ArrayList<Grupes>();
        this.students = SingletonStudents.getInstance();
    }

    public Grupes getGrupe(String grupe) {
        for (Grupes value : grupes) {
            if (value.getName().equals(grupe)) {
                return value;
            }
        }
        return null;
    }
    public void addToGroup(Grupes value, Student student) {
        if(grupes.contains(value)){
            value.addStudent(student);
        }
    }
    public ObservableList<Student> getObservableStudentList(){
        ObservableList<Student> list = FXCollections.observableArrayList();
        list.addAll(students.getStudents());
        return list;
    }
    public ObservableList<Grupes> getObservableGrupesList(){
        ObservableList<Grupes> list = FXCollections.observableArrayList();
        list.addAll(grupes);
        return list;
    }
    public List<Student> getStudentList(){
        return students.getStudents();
    }
    public void addGrupes(Grupes grupe) {
        grupes.add(grupe);
    }
    public List<Grupes> getGrupesList(){
        return grupes;
    }
}
