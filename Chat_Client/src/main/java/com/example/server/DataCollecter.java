package com.example.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataCollecter {
    private ArrayList<String> users;
    private List<String> groups;
    private ArrayList<String> messages;

public DataCollecter() {
        users = new ArrayList<>();
        groups = new ArrayList<>();
        messages = new ArrayList<>();
    }
    public void addUser(String user){
        users.add(user);
    }
    public void addGroup(String group){
        if(!groups.contains(group)){
            groups.add(group);
        }
    }
    public void addMessage(String group, String user, String message){
        messages.add("Group: " + group + ": " + user + ":" + message);
    }
    public void save(){
        File file = new File("data.txt");
        if(file.exists()){
            file.delete();
        }
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Users:\n");
            for(String user : users){
                fw.write(user + " ");
            }
            fw.write("\nGroups:\n");
            for(String group : groups){
                fw.write(group + " ");
            }
            fw.write("\nMessages:\n");
            for(String message : messages){
                fw.write(message + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
