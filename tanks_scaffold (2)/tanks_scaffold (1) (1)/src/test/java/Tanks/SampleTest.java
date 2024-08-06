package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    public void simpleTest() {

        App app = new App();
        PApplet.runSketch(new String[] {"App"},app);

        app.settings();
        //app.setup();
        //app.readconfig();
        //app.loadlevel(Level level);
        app.draw();

        assertNotNull(app.layout);

        app.key = 'r';
        app.keyPressed();
        app.resetGame();

        app.key =' ';
        app.keyPressed();
        //app.isfired == true;
        
    }
}

//gradle test jacocoTestReport