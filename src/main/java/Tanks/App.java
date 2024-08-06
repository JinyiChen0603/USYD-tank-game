package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    //create the need varible
    private HashMap<String,String> playColors = new HashMap<String,String>();
    private Level[] levels;
    private char[][] layout;
    private PImage tree;
    private int[] foregroundColour;
    private PImage background;
    private PImage windimage;
    private int windsize;
    public boolean tankalive =true;

    private int count;
    private int currentplayer = 0;
    private boolean isLeft = false;
    private boolean isRight = false;
    private boolean isUp = false;
    private boolean isDown = false;
    private boolean powerUp = false;//w
    private boolean powerDown = false;//s
    private boolean isfired = false;//" "
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private int x;


    private PImage fuel;

    public static float[] window = new float[896];

    public ArrayList<Tank> tanks = new ArrayList<Tank>();;

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {


        frameRate(FPS);
        readconfig();
        loadlevel(levels[0]);
        
        for(int i=0; i<20; i++){
            for(int j=0; j<28; j++){
                if(layout[i][j] == 'X'){
                    for(int k = 0; k<32; k++){
                        window[j*32+k] =i*32;
                    }
                }
            }
        }

        for(int i=0; i<864 ;i++){
            //i max = 863
            window[i] = cal_num(i,window,32);
        }
        for(int i=0; i<864 ;i++){
            //i max = 863
            window[i] = cal_num(i,window,32);
        }
        

        
		//See PApplet javadoc:
		//loadJSONObject(configPath)
		//loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    }

    private void readconfig() {
        try{
            FileReader fr = new FileReader(configPath);
            JSONObject jo1 = new JSONObject(fr);

            //get levels
            JSONArray ja = jo1.getJSONArray("levels");
            //System.out.println("what in jo2" + ja);
            levels = new Level[ja.size()];
            for(int i=0; i< ja.size(); i++){

                JSONObject jo2 = ja.getJSONObject(i);

                if(jo2.hasKey("trees")){
                    //get level and background
                    levels[i] = new Level(jo2.getString("layout"),jo2.getString("background"),
                                            jo2.getString("foreground-colour"),jo2.getString("trees"),this);
                }else{
                    levels[i] = new Level(jo2.getString("layout"),jo2.getString("background"),
                                            jo2.getString("foreground-colour"),null,this);
                }
            }

            //get player colors
            JSONObject jo3 = jo1.getJSONObject("player_colours");
            //System.out.println("player_colours for jo3" +jo3);
            for(Object s : jo3.keys()){

                String k = (String) s;
                String v = jo3.getString(k);
                playColors.put(k,v);
                //System.out.println("player_colours" +playColors);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadlevel(Level level) {
        this.background=level.background;
        this.tree=level.tree;
        this.layout=level.layout;
        this.foregroundColour=level.foregroundColour;
        for(int j=0; j<28; j++) {
            for(int i=0; i< 20; i++){
                if(layout[i][j] == 'A' || layout[i][j] == 'B' || layout[i][j] =='C' || layout[i][j] == 'D'){
                    Tank t = new Tank(j, Character.toString(layout[i][j]), playColors);
                    tanks.add(t);
                }
            }
        }
        tanks.get(0).selected = true;


    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if(event.getKeyCode() == 37){
            System.out.println(" left way go works");
            isLeft = true;
        }
        if(event.getKeyCode() == 39){
            System.out.println(" right way go works");
            isRight = true;
        }
        if(event.getKeyCode() == 38){
            isUp = true;
        }
        if(event.getKeyCode() == 40){
            isDown = true;
        }
        if(event.getKeyCode() == 87){
            powerUp = true;
        }
        if(event.getKeyCode() == 83){
            powerDown = true;
        }
        if(event.getKeyCode() == 32){
            if (event.getKeyCode() == 32) {
                isfired = false;
                if (tanks == null) {
                    System.out.println("tanks list is null");
                    return;
                }
                Tank t = tanks.get(currentplayer);
                if (t == null) {
                    System.out.println("Tank for current player is null");
                    return;
                }
                float v = 1 + t.power / 100 * 8;
                Bullet b = new Bullet(v, t.pixelx, t.colors, t.radian);
                if (bullets == null) {
                    System.out.println("bullets list is null");
                    return;
                }
                bullets.add(b);
            }
        }
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(KeyEvent event){
        if(event.getKeyCode() == 37){
            isLeft = false;
        }
        if(event.getKeyCode() == 39){
            isRight = false;
        }
        if(event.getKeyCode() == 38){
            isUp = false;
        }
        if(event.getKeyCode() == 40){
            isDown = false;
        }
        if(event.getKeyCode() == 87){
            powerUp = false;
        }
        if(event.getKeyCode() == 83){
            powerDown = false;
        }
        if(event.getKeyCode() == 32){
            isfired = true;
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        this.image(background,0,0);

        //set upthe window

        // for(int i=0; i<20; i++){
        //     for(int j=0; j<28; j++){
        //         if(layout[i][j] == 'X'){
        //             for(int k = 0; k<32; k++){
        //                 window[j*32+k] =i*32;
        //             }
        //         }
        //     }
        // }

        // for(int i=0; i<864 ;i++){
        //     //i max = 863
        //     window[i] = cal_num(i,window,32);
        // }
        // for(int i=0; i<864 ;i++){
        //     //i max = 863
        //     window[i] = cal_num(i,window,32);
        // }

        //draw the ground
        for(int i=0; i<20; i++){
            for(int j=0; j<28; j++){
                if(layout[i][j] == 'X'){
                    //i for y 竖着的, j for x 横着的
                    //draw the ground with different color in different levels
                    noStroke();
                    for(int k=0; k<32; k++){
                        float height = window[j*32+k];
                        fill(255,255,255,255);
                        rect(j*32+k,height,1,640-height);
                        
                    }
                }
            }
        }

        //draw the tree
        for(int i=0; i<20; i++){
            for(int j=0; j<28; j++){
                if(layout[i][j] == 'T'){
                    //i for y 竖着的, j for x 横着的
                    //draw the tree
                    noStroke();
                    height = (int) window[j*32];
                    this.image(tree, j*32-13,height-32,32,32);
                }
            }
        }

        //draw the tank
        for(int i = 0; i<tanks.size(); i++){
            tanks.get(i).draw(this);
        }
        // draw the bullet
        for(int i = 0; i<bullets.size(); i++){
            bullets.get(i).draw(this);
            bullets.get(i).collide(window,tanks);
        }

        Tank t = tanks.get(currentplayer);
        count += 1;
        fill(0);//black
        textSize(20);//
        if(currentplayer ==0){
            text("Player A's turn",10,30);
            stroke(0);
            fill(255,255,255);
            this.rect(500,10,150,27);//生命值条
            fill(0,0,255);
            this.rect(500,10,(t.health/100)*150,27);

        }
        if(currentplayer ==1){
            text("Player B's turn",10,30);
            stroke(0);
            fill(255,255,255);
            this.rect(500,10,150,27);//生命值条
            fill(255,0,0);
            this.rect(500,10,(t.health/100)*150,27);
        }
        if(currentplayer ==2){
            text("Player C's turn",10,30);
            stroke(0);
            fill(255,255,255);
            this.rect(500,10,150,27);//生命值条
            fill(0,255,255);
            this.rect(500,10,(t.health/100)*150,27);
        }
        if(currentplayer ==3){
            text("Player D's turn",10,30);
            stroke(0);
            fill(255,255,255);
            this.rect(500,10,150,27);//生命值条
            fill(255,255,0);
            this.rect(500,10,(t.health/100)*150,27);
        }

        if (count <= 60){
            if(isfired){
                count=0;
                taketurn();
                x+=1;
                isfired = false;
            }
        }
        if(count == 60){
            count=0;
            if(isfired){
                taketurn();
                x+=1;
                isfired = false;
            }
        }

        
        if(isLeft){
            if(t.fuel > 0 ){
                //System.out.println("fuel: " + t.fuel);
                t.pixelx -= 2;
                t.fuel -= 2;
            }
        }
        if(isRight){
            if(t.fuel > 0){
                //System.out.println("fuel: " + t.fuel);
                t.pixelx += 2;
                t.fuel -= 2;
            }
        }
        if(isUp){
            if(-1.0000001< t.radian){
                t.radian -=0.1;
                System.out.println("angle: " + t.radian);
            }
        }
        if (isDown) {
            if(t.radian<= 1.0000001){
                t.radian += 0.1;
                System.out.println("angle: " + t.radian);
            }
        }
        if(powerUp){
            if(t.power < 50.0 ){
                System.out.println("power: " + t.power);
                t.power +=1.0;
            }
        }
        if(powerDown){
            if( t.power > 0){
                System.out.println("power: " + t.power);
                t.power -=1.0;
            }
        }

        

        fuel = loadImage("src//main//resources//Tanks//fuel.png");
        image(fuel,150,3,32,32);
        fill(0);//black
        textSize(24);//
        text(t.fuel,185,30);//text the fuel 打出油量

        fill(0);//black
        textSize(24);//
        text("Health:",415,30);//text the "health" 打出生命值

        // stroke(0);
        // fill(255,255,255);
        // this.rect(500,10,150,27);//生命值条
        // fill(0,0,0);
        // this.rect(500,10,(t.health/100)*150,27);

        fill(0);//black
        textSize(24);//
        text(String.format("%.0f", t.health),650,32);//text the int health 打出生命值数字

        fill(0);//black
        textSize(24);//
        text("power: "+t.power,500,60);//text the power 打出power

        int min = -25;
        int max = 25;
        //windsize = random.nextInt(max - min + 1)+min;
        
        if(x == 4){
            windsize = random.nextInt(max - min + 1)+min;
            x = 0;
        }
        if(windsize <0){
            windimage = loadImage("src//main//resources//Tanks//wind-1.png");
            image(windimage,790,3,32,32);
            fill(0);//black
            textSize(20);//
            text(windsize,790,50);
            
        }
        if(windsize >0){
            windimage = loadImage("src//main//resources//Tanks//wind.png");
            image(windimage,790,3,32,32);
            fill(0);//black
            textSize(20);//
            text(windsize,790,50);
        }
        if(windsize == 0){
            fill(0);//black
            textSize(20);//
            text("NO WIND",750,50);
        }



        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
    }


    private void taketurn() {
        tanks.get(currentplayer).selected = false;
        currentplayer =currentplayer+1;
        if (currentplayer <=3){
            tanks.get(currentplayer).selected = true;
            tanks.get(currentplayer).fuel = 250;
        }else{
            currentplayer = 0;
            tanks.get(currentplayer).selected = true;
            tanks.get(currentplayer).fuel = 250;
        }
    }

    private float cal_num(int start, float[] window, int size) {
        float result =0;
        for(int i=0; i<size; i++){
            result += window[start+i];
        }
        result = result/32;
        return result;
    }

    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}
