package entity.bullet;

import main.GamePanel;
import entity.effect.Effect;

import java.util.Objects;
import java.util.Random;

public class NormalBullet extends Bullet{
    int timer = 0;
    Random random = new Random();

    public NormalBullet(String path, String name,int solidAreaX, int solidAreaY, int rectX, int rectY, int worldX, int worldY,
    int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY, int targetX, int targetY) {
        super(path, name,solidAreaX,solidAreaY, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY, targetX, targetY);

        animationDelay = 0;
        gp.soundManager.play("pew");
    }

    @Override
    public void specialMethod() {
        timer++;
        if (Objects.equals(this.objName, "enemyBullet")) {
            Effect glowEffect = new Effect(null, 16, 16,
                    this.worldX,
                    this.worldY,
                    12, gp, 2, 1.5, 1.5, targetX, targetY);
            gp.obj.add(glowEffect);
        }
        // Tạo hiệu ứng laser nhấp nháy từ vị trí hiện tại đến mục tiêu

        // Tạo hiệu ứng ánh sáng cho đường đi của viên đạn
        if (timer >= 3) {
            createGlowEffect();
            timer = 0;
        }
    }



    private void createGlowEffect() {
        Effect glowEffect = new Effect(null, 16, 16,
                this.worldX - 8 + random.nextInt(16),
                this.worldY - 8 + random.nextInt(16),
                12, gp, 2, 1.5, 1.5, targetX, targetY);
        gp.obj.add(glowEffect);
    }

}
