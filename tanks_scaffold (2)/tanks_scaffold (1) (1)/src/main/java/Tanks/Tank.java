package Tanks;

import java.util.HashMap;

import processing.core.PImage;

public class Tank {
    private int x;
    public String type;
    private int count = 0;

    public float pixelx;
    public float pixely;
    public int[] colors;
    public boolean selected;
    public float radian;
    public float health;
    public float power;
    public int fuel;
    public float[] window;
    public int score = 0;
    public boolean broken;
    // public boolean havapara = false;
    // public boolean noPara = false;

    public Tank(int x,String type,HashMap<String,String> playerColers, float[] window){
        this.x = x;
        this.pixelx =32*x;
        this.type = type;
        this.window = window;
        colors = new int[3];
        String[] C = playerColers.get(type).split(",");
        colors[0] = Integer.parseInt(C[0]);
        colors[1] = Integer.parseInt(C[1]);
        colors[2] = Integer.parseInt(C[2]);
        this.selected = false;
        this.radian = 0;
        this.health = 100;
        this.power = 50;
        this.fuel= 250;

    }
    /**
     * draw the tank
     */
    public void draw(App app){
        if(broken){
            return;
        }
        app.noStroke();
        /**
         * draw only when the health for the tank is larger than 0
         */
        if(health > 0){
            //for the turret
            app.fill(0,0,0);
            app.pushMatrix();
            app.translate(pixelx, window[(int)pixelx]);
            app.rotate(radian);
            app.rect(-2,-15,4,15);
            app.popMatrix();


            pixely = window[(int)pixelx];

            app.fill(colors[0],colors[1],colors[2]);
            app.rect(pixelx-16,pixely,32,5);
            app.rect(pixelx-11,pixely-5,20,5);

            if(selected == true){
                app.stroke(0);
                app.line(pixelx,pixely-50,pixelx,pixely-130);
                app.line(pixelx,pixely-50,pixelx-16,pixely-80);
                app.line(pixelx,pixely-50,pixelx+16,pixely-80);
                count+=1;
                /**
                 * make the arrow disappear after two seconds
                 */
                if(count == 60){
                    selected = false;
                    count = 0;
                }
            }
        }if(health <= 0){
            app.noStroke();
            app.fill(0,255,0);
            app.ellipse(pixelx,pixely, 60,60);

            broken = true;
        }
    }

}
