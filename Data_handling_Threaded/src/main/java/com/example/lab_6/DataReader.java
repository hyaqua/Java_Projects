package com.example.lab_6;

import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

public class DataReader implements Runnable {
    private Scanner sc;
    private final HelloController controller;
    private int millis;

    @Override
    public void run() {
        while(sc.hasNextLine()){
            String[] data = sc.nextLine().split(",");
            Person p;
            if (data.length == 8){
                 p = new Person(Integer.parseInt(data[0]), data[1],
                        data[2], data[3], data[4],
                        data[5], data[6], LocalDate.parse(data[7]));
            } else {
                p = new Person(Integer.parseInt(data[0]), data[1],
                        data[2], data[3], data[4],
                        data[5] + data[6], data[7], LocalDate.parse(data[8]));
            }

            controller.addPerson(p);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public DataReader(File file, HelloController controller, int millis) {
        try {
            sc = new Scanner(file);
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("File not found");
        }
        this.millis = millis;
        this.controller = controller;
    }
}
