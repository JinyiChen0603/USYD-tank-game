package Tanks;

import java.util.ArrayList;

public class Bullet {
    public double v,a;
    public double vx,vy;

    public float pixelx,pixely;
    public int[] bulletcolor;
    public float r;
    public boolean alive;
    public boolean isExplortion;
    public int count;
//    float[] window =App.window;
    
    public Bullet(float v,float pixelx,int[] bulletcolor,float radian, float[] window) {
        this.v = v;
        this.vx = v*Math.sin(radian);
        this.vy = -v*Math.cos(radian);
        this.pixelx = pixelx;
        this.pixely = window[(int)pixelx];
        this.bulletcolor = bulletcolor;
        this.r = 3;
        this.alive = true;
        this.a =0.12;
    }


    public void draw(App app){
        if(isExplortion){
            app.noStroke();
            app.fill(255,0,0);
            app.ellipse(pixelx,pixely, 60,60);
            app.fill(255,97,0);
            app.ellipse(pixelx,pixely, 30,30);
            app.fill(255,255,0);
            app.ellipse(pixelx,pixely, 12,12);
            count+=1;
            if (count ==6)
            isExplortion = false;
            alive = false;
        }
        if(alive){
            app.noStroke();
            app.fill(bulletcolor[0],bulletcolor[1],bulletcolor[2]);
            app.ellipse(pixelx,pixely, 3*r,3*r);
            app.fill(0);
            app.ellipse(pixelx,pixely, 1,1);
            pixelx +=vx;
            pixely +=vy;
            vy += a;
            
            //System.out.println("pixelx for bullet: "+pixelx);
            //System.out.println("pixely for bullet: "+(int)pixely);
        }
    }

    public void collide( float[]window, ArrayList<Tank> tanks){
        if(alive== false){
            return;
        }
//        System.out.println("window[i]: "+(int)window[(int)pixelx] + "pixel y = " + pixely);
        if((pixelx <0 || pixelx>896 || pixely>900)){
            alive = false;
            return;
        }

        //判定爆炸
//        System.out.println("distance between bullets: "+Math.abs((int)pixely-(int)window[(int)pixelx]));
        if(pixely >= window[(int)pixelx]){
//            System.out.println("distance between bullets: "+Math.abs((int)pixely-(int)window[(int)pixelx]));
            isExplortion = true;
            alive = false;
            //判定破坏地形
            for(int j= 0; j< 864 ; j++){
                double dist = Math.sqrt((j-pixelx)*(j-pixelx)+(window[j]-pixely)*(window[j]-pixely));
//                System.out.println("dist: "+dist);
                if(dist <=30){
                    double r = Math.abs(pixelx - j)*90/30*(Math.PI/180);

                    double newY = pixely + 30*Math.cos(r);
                    System.out.println("oldY: "+ App.window[j]);
//                    App.window[j] = (float)newY;
                    App.window[j] = (float) newY;
                    System.out.println("newY: "+ App.window[j]);
//                    System.out.println("window: "+window[j]);

                }
            }
            //判定减少生命值
            for(Tank t :tanks){
                double distance = Math.sqrt((pixelx-t.pixelx)*(pixelx-t.pixelx)+(pixely-t.pixely)*(pixely-t.pixely));
                System.out.println("distance: "+distance);
                if(distance<= 30){
                    double damage = Math.abs(60 -distance/30*60);
                    t.health -= damage;
                    t.pixely = window[(int)t.pixelx];
                }
            }
        }
    }
}
