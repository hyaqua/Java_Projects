package com.example.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static DataCollecter dataCollecter = new DataCollecter();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private String GroupChat;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = bufferedReader.readLine();
            this.GroupChat = "";
            dataCollecter.addUser(username);
            clients.add(this);
            bufferedWriter.write("SERVER: Welcome " + username + "\n");
            SendInformation();
            broadcastMessage("SERVER: " + username + " joined the chat");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void JoinGroup(String GroupChat){
        this.GroupChat = GroupChat;
        dataCollecter.addGroup(GroupChat);
        try {
            bufferedWriter.write("SERVER: You joined the group " + GroupChat);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        broadcastMessage("SERVER: " + username + " joined the group " + GroupChat);
    }
    public void leaveGroup(){
        broadcastMessage("SERVER: " + username + " left the group " + GroupChat);
        try {
            bufferedWriter.write("SERVER: You left the group " + GroupChat);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.GroupChat = "";
    }
    public void SendInformation(){
        StringBuilder info = new StringBuilder();
        info.append("SERVER: ********************** Information ********************** \n");
        info.append("SERVER: To send a private message type: /pm <username> <message> \n");
        info.append("SERVER: To see this information again type: /help \n");
        info.append("SERVER: To join a group type: /join <groupname> \n");
        info.append("SERVER: To leave a group type: /leave\n");
        info.append("SERVER: To exit type: /exit \n");
        try {
            bufferedWriter.write(info.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                dataCollecter.addMessage(GroupChat, username, messageFromClient);
                System.out.println(messageFromClient);
                if(messageFromClient == null){
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                if(messageFromClient.equals("/exit") ){
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                if(messageFromClient.startsWith("/pm")){
                    String[] message = messageFromClient.split(" ", 3);
                    for (ClientHandler client : clients){
                        if(client.username.equals(message[1])){
                            client.bufferedWriter.write("USER: Private message from" + username + ": " + message[2]);
                            client.bufferedWriter.newLine();
                            client.bufferedWriter.flush();
                        }
                    }
                } else if(messageFromClient.startsWith("/join")){
                    String[] msg = messageFromClient.split(" ", 2);
                    JoinGroup(msg[1]);
                } else if(messageFromClient.equals("/leave")){
                    leaveGroup();
                } else if (messageFromClient.equals("/help")){
                    SendInformation();
                } else {
                    broadcastMessage("USER: " + username + ": " + messageFromClient);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String message){
        for (ClientHandler client : clients){
            try {
                if(!client.username.equals(this.username) && client.GroupChat.equals(this.GroupChat)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler(){
        clients.remove(this);
        broadcastMessage("SERVER: " + username + " left the chat");
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
