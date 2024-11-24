package entity.bullet;

import entity.effect.Effect;
import entity.enemy.Pike;
import main.GamePanel;

public class FireBullet extends Bullet{
    String effect = null;
    int timer = 0;
    public FireBullet(String path, String name, int solidAreaX, int solidAreaY, int rectX, int rectY, int worldX, int worldY, int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY, int targetX, int targetY, String effect) {
        super(path, name, solidAreaX, solidAreaY, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY, targetX, targetY);
        this.effect = effect;
    }
    @Override
    public void specialMethod(){
        timer++;
        if(timer >= 7){
            if(effect == null){
                Pike p = new Pike(gp);
                p.canDeath = true;
                p.worldX = worldX;
                p.worldY = worldY;
                gp.obj.add(p);
            }
            else{
                Effect a = new Effect(effect, 0, 0, worldX, worldY, 15, gp, 0, 2, 2, 0, 0);
                gp.obj.add(a);
            }
        }
    }
}
