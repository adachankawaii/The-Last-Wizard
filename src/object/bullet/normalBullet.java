package object.bullet;

import main.GamePanel;
import object.effect.effect;

import java.util.Random;

public class normalBullet extends bullet{
    public normalBullet(String path, String name, int rectX, int rectY, int worldX, int worldY, int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY) {
        super(path, name, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY);
        animationDelay = 5;
    }
    @Override
    public void specialMethod(){
        effect a = new effect(null, 16,16,this.worldX  -5 + new Random().nextInt(10),this.worldY-5 + new Random().nextInt(10),10, gp, 2, 2,2);
        gp.obj.add(a);
    }
}
