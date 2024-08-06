package Tanks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class BulletTest {

    @Test
    public void testBulletCreation() {
        float[] window = new float[896]; 
        ArrayList<Tank> tanks = new ArrayList<>(); 

        Bullet bullet = new Bullet(10, 100, new int[]{255, 0, 0}, 0, window);

        // Test bullet attributes after creation
        assertEquals(10, bullet.v);
        assertEquals(10 * Math.sin(0), bullet.vx);
        assertEquals(-10 * Math.cos(0), bullet.vy);
        assertEquals(100, bullet.pixelx);
        assertEquals(window[100], bullet.pixely);
        assertArrayEquals(new int[]{255, 0, 0}, bullet.bulletcolor);
        assertEquals(3, bullet.r);
        assertTrue(bullet.alive);
        assertFalse(bullet.isExplortion);
        assertEquals(0, bullet.count);
    }
}
