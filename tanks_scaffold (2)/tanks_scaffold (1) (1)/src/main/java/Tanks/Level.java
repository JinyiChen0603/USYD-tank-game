package Tanks;

import java.io.BufferedReader;
import java.io.FileReader;

import processing.core.PImage;

public class Level {

    //create the varibles in java
    public final PImage background;
    
    public PImage tree;
    public char layout[][];
    public int[] foregroundColour;
    

    public Level(String filename, String backImage, String foreColor, String treeImage, App app){
        this.layout = new char[20][28];
        for (int i = 0; i <20; i++) {
            for (int j = 0; j <28;j++) {
                layout[i][j] = ' ';
            }
        }

        readfile(filename);
        this.background = app.loadImage("src/main/resources/Tanks/" + backImage);

        //get and show the color
        String[] colors = foreColor.split(",");
        this.foregroundColour = new int[3];
        foregroundColour[0] = Integer.parseInt(colors[0]);
        foregroundColour[1] = Integer.parseInt(colors[1]);
        foregroundColour[2] = Integer.parseInt(colors[2]);

        //justify the trees
        if(treeImage == null){
            this.tree = app.loadImage("src/main/resources/Tanks/tree1.png");
        }else{
            this.tree = app.loadImage("src/main/resources/Tanks/" + treeImage);
        }
    }
    private void readfile(String filename) {
        try{
            BufferedReader buffer = new BufferedReader(new FileReader(filename));
            String line = null;
            int lineNum = 0;
            while((line = buffer.readLine()) != null && lineNum < 20){
                for(int i = 0; i < line.length(); i++){
                    layout[lineNum][i]=line.charAt(i);
                }
                lineNum+=1;
            }
            //System.out.println("lineNum: " + lineNum);
            buffer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
