package com.example.studentai;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SingletonStudents implements attendance{
    private List<Student> students;
    private static SingletonStudents instance = null;

    private SingletonStudents() {
        students = new ArrayList<>();
    }
    public static synchronized SingletonStudents getInstance(){
        if(instance == null){
            instance = new SingletonStudents();
        }
        return instance;
    }
    public Student getStudent(String id){
        for(Student student : students){
            if(student.getId().equals(id)){
                return student;
            }
        }
        return null;
    }
    public void addGroup(Student student, String name){
        int idx = students.indexOf(student);
        if(idx != -1){
            students.get(idx).addGroup(name);
        }
    }
    @Override
    public void setAttendance(LocalDate date, Student student){
        int idx = students.indexOf(student);
        if(idx != -1){
            students.get(idx).setAttendance(date);
        }
    }
    @Override
    public void setAttendance(LocalDate date, ArrayList<Student> students){
        for (Student student : students) {
            this.setAttendance(date, student);
        }
    }
    @Override
    public void setAttendance(LocalDate date, int idx){
        students.get(idx).setAttendance(date);
    }
    @Override
    public boolean[] getAttendance(LocalDate date){
        boolean[] result = new boolean[students.size()];
        for(int i = 0; i < students.size(); i++){
            result[i] = students.get(i).getAttendance(date);
        }
        return result;
    }
    public List<Student> getStudents(){
        return students;
    }
    public void addStudent(Student student){
        students.add(student);
    }
    public void SortStudents(){
        students.sort(new CustomComparator());
    }
}
class CustomComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        if(isNumeric(o1.getId()) && isNumeric(o2.getId())){
            return Integer.compare(Integer.parseInt(o1.getId()), Integer.parseInt(o2.getId()));
        } else {
            return o1.getId().compareTo(o2.getId());
        }
    }
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}