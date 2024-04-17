package com.example.lab_7;

import processing.core.*;

import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Processing extends PApplet implements Runnable {

    String mazeFileName = "sample.maze";

    int xblocks;
    int yblocks;

    int xExit;
    int yExit;
    int xStart;
    int yStart;

    int blockSize = 40;

    int mapWidth;
    int mapHeight;

    boolean gameOver = false;

    public class Position {
        int   x,  y;
        float xR, yR;
        boolean isBarrier;
        Position north, east, south, west;

        public Position( int X, int Y, boolean IsBarrier ) {
            x = X;
            y = Y;
            xR = X*blockSize;
            yR = Y*blockSize;
            isBarrier = IsBarrier;
            north     = null;
            east      = null;
            south     = null;
            west      = null;
        }

        void draw() {
            noStroke();
            if ( isBarrier ) {
                fill(0, 0, 255);
            } else {
                fill(255,255,255);
            }

            rect(xR, yR, blockSize, blockSize);
        }
    }




    public class Player {

        int x, y;
        Position pos;
        public Player( int X, int Y ) {
            x = X;
            y = Y;
            pos = maze[y][x];
        }

        void north() {
            if(pos.north != null && !pos.north.isBarrier) {
                pos = pos.north;
                y--;
            }
        }
        void east() {
            if(pos.east != null && !pos.east.isBarrier) {
                pos=pos.east;
                x++;
            }
        }
        void south() {
            if(pos.south != null && !pos.south.isBarrier) {
                pos=pos.south;
                y++;
            }
        }
        void west() {
            if(pos.west != null && !pos.west.isBarrier) {
                pos=pos.west;
                x--;
            }
        }

        void draw() {

            stroke(1.0f);
            strokeWeight(1.0f);
            fill(255,255,0);
            ellipse((0.5f+x)*blockSize, (0.5f+y)*blockSize, blockSize, blockSize);
            fill(0,0,0);
            ellipse((x+0.3f)*blockSize, (y+0.35f)*blockSize, 0.15f*blockSize, 0.15f*blockSize);
            ellipse((x+0.7f)*blockSize, (y+0.35f)*blockSize, 0.15f*blockSize, 0.15f*blockSize);
            noFill();
            strokeWeight(3.0f);
            arc((x+0.5f)*blockSize, (y+0.4f)*blockSize, 0.8f*blockSize, 0.8f*blockSize, 0.5f, PI-0.5f);
            if(x==xExit && y==yExit) {
                gameOver=true;
            }
        }

    }


    Position[][] maze;
    Player player;


    // How we move around in the maze
    public void keyPressed() {
        if (key == CODED)
        {
            switch (keyCode) {
                case LEFT -> player.west();
                case UP -> player.north();
                case RIGHT -> player.east();
                case DOWN -> player.south();
            }
        }
    }
    void  runCode(String[] lines) throws InterruptedException {
        if(!gameOver){
            player.x = xStart;
            player.y = yStart;
            player.pos = maze[yStart][xStart];
            sleep(20);
            for(String line : lines){
                handleCommands(line);
            }
        }
    }

    private boolean handleCommands(String line) throws InterruptedException {
        String[] words = line.split(" ");
        switch (words[0]) {
            case "north", "south", "east", "west" -> {
                if (words.length > 2) {
                    handleMove(new String[]{words[0], words[1]});
                    if(words[2].equals("&&")){
                        System.out.println("hello");
                        handleCommands(line.split("&& ", 2)[1]);
                    }
                } else {
                    handleMove(words);
                }
            }
            case "for" -> handleFor(words);
            case "while" -> handleWhile(words);
            case "loop" -> handleLoop(words);
            default -> {
                System.out.println("Invalid command");
                return false;
            }
        }
        return true;
    }

    private void handleLoop(String[] words) throws InterruptedException {
        int i = Integer.parseInt(words[1]);
        String command = String.join(" ", words);
        words = command.split("[{] ", 2);
        command = words[1].split("[}]", 2)[0];
        for(int j = 0; j < i; j++){
            handleCommands(command);
        }
        if(words[1].split("[}]", 2)[1].startsWith(" && ")){
            handleCommands(words[1].split("[}]", 2)[1].split("&& ", 2)[1]);
        }
    }

    private void handleWhile(String[] words) throws InterruptedException {
        String command = String.join(" ", words).split("[{] ", 2)[1].split("[}]", 2)[0];
        System.out.println(command);
        switch (words[1]) {
            case "collision_north" ->{
                while (!player.pos.north.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "!collision_north" ->{
                while (player.pos.north.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "collision_south" ->{
                while (!player.pos.south.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "!collision_south" ->{
                while (player.pos.south.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "collision_east" ->{
                while (!player.pos.east.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "!collision_east" ->{
                while (player.pos.east.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "collision_west" ->{
                while (!player.pos.west.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
            case "!collision_west" ->{
                while (player.pos.west.isBarrier) {
                    if(!handleCommands(command)){
                        break;
                    }
                }
            }
        }
        command = String.join(" ", words);
        words = command.split("[{] ", 2);
        if(words[1].split("[}]", 2).length > 1){
            if(words[1].split("[}]", 2)[1].startsWith(" && ")){
                handleCommands(words[1].split("[}]", 2)[1].split("&& ", 2)[1]);
            }
        }
    }

    private void handleFor(String[] words) throws InterruptedException {
        String command = String.join(" ", words);
        command = command.replace("for ", "");
        words = command.split("[{] ", 2);
        String[] for_words = words[0].split(";");
        int i = Integer.parseInt(for_words[0].split(" = ", 2)[1]);
        int j = Integer.parseInt(for_words[1].split(" ", 4)[3]);
        for(int k = i; k < j;){
            handleCommands(words[1].split("[}]", 2)[0]);
            if(for_words[2].contains("++")){
                k++;
            }else{
                k--;
            }
        }
        if(words[1].split("[}]", 2)[1].startsWith(" && ")){
            handleCommands(words[1].split("[}]", 2)[1].split("&& ", 2)[1]);
        }
    }

    private void handleMove(String[] words) throws InterruptedException {
        switch (words[0]) {
            case "north" -> {
                if(words.length > 1){
                    for(int i = 0; i < Integer.parseInt(words[1]); i++){
                        player.north();
                        sleep(10);
                    }
                } else{
                    player.north();
                    sleep(10);
                }
            }
            case "south" -> {
                if(words.length > 1){
                    for(int i = 0; i < Integer.parseInt(words[1]); i++){
                        player.south();
                        sleep(10);
                    }
                } else{
                    player.south();
                    sleep(10);
                }
            }
            case "east" -> {
                if(words.length > 1){
                    for(int i = 0; i < Integer.parseInt(words[1]); i++){
                        player.east();
                        sleep(10);
                    }
                } else{
                    player.east();
                    sleep(10);
                }
            }
            case "west" -> {
                if(words.length > 1){
                    for(int i = 0; i < Integer.parseInt(words[1]); i++){
                        player.west();
                        sleep(10);
                    }
                } else{
                    player.west();
                    sleep(10);
                }
            }
        }
    }

    public void setup() {

        // Load the maze text file
        String[] text = loadStrings(mazeFileName);

        xblocks = text[0].length();
        yblocks = text.length;

        mapWidth  = xblocks*blockSize;
        mapHeight = yblocks*blockSize;

        maze = new Position[yblocks][];

        // Determine maze size and locations of start and exit
        for ( int row=0; row<yblocks; ++row ) {

            maze[row] = new Position[xblocks];

            for (int col=0; col<xblocks; ++col) {

                char c = text[row].charAt(col);

                boolean isBarrier = c == 'X';
                boolean isExit    = c == 'E';
                boolean isStart   = c == 'O';

                maze[row][col] = new Position(col, row, isBarrier);

                if(isExit) {
                    xExit = col;
                    yExit = row;
                }
                if(isStart) {
                    xStart = col;
                    yStart = row;
                }
            }
        }

        // Do connectivity

        for( int row=1; row<yblocks-1; ++row ) {
            for( int col=1; col<xblocks-1; ++col ) {
                maze[row][col].north = maze[row-1][col];
                maze[row][col].east  = maze[row][col+1];
                maze[row][col].south = maze[row+1][col];
                maze[row][col].west  = maze[row][col-1];
            }
        }

        player = new Player(xStart,yStart);

        int X = player.pos.x;
        int Y = player.pos.y;

        windowResize(mapWidth,mapHeight);

    }
    public void settings(){
        size(1200, 450);
    }

    public void draw() {
        if (!gameOver) {
            background(255,255,255);
            for ( int row=0; row<yblocks; ++row ) {
                for ( int col=0; col<xblocks; ++col ) {
                    maze[row][col].draw();
                }
            }
            player.draw();
        } else {
            background(0,0,0);
            textSize(2*blockSize);
            fill(255,255,255);
            textAlign(CENTER);
            text("You Win!", 0.5f*mapWidth, 0.5f*mapHeight);
        }
    }
    @Override
    public void run() {
        String[] processingArgs = {"Processing"};
        PApplet.runSketch(processingArgs, this);
    }
}
