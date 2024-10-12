package entity.bullet;

import main.GamePanel;
import entity.effect.Effect;

import java.util.Random;

public class NormalBullet extends Bullet{
    public NormalBullet(String path, String name,int solidAreaX, int solidAreaY, int rectX, int rectY, int worldX, int worldY,
    int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY, int targetX, int targetY) {
        super(path, name,solidAreaX,solidAreaY, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY, targetX, targetY);
        animationDelay = 0;
    }
    @Override
    public void specialMethod(){
        Effect a = new Effect(null, 16,16,this.worldX  -5 + new Random().nextInt(10),this.worldY-5 + new Random().nextInt(10),10, gp, 2, 2,2, gp.mouseX, gp.mouseY);
        gp.obj.add(a);
    }
}
