package com.example.studentai;

import java.time.LocalDate;
import java.util.ArrayList;

public interface StudentAttendance {
    void setAttendance(LocalDate date);
    boolean getAttendance(LocalDate date);
}
interface attendance {
    void setAttendance(LocalDate date, ArrayList<Student> ids);
    void setAttendance(LocalDate date, Student student);
    void setAttendance(LocalDate date, int idx);
    boolean[] getAttendance(LocalDate date);
}
interface att extends attendance {
    void hello();
}
