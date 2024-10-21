package entity.bullet;

import main.GamePanel;
import entity.effect.Effect;

import java.util.Random;

public class NormalBullet extends Bullet{
    int timer = 0;
    public NormalBullet(String path, String name,int solidAreaX, int solidAreaY, int rectX, int rectY, int worldX, int worldY,
    int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY, int targetX, int targetY) {
        super(path, name,solidAreaX,solidAreaY, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY, targetX, targetY);
        animationDelay = 0;
    }
    
    @Override
    public void specialMethod(){
        gp.soundManager.play("wand");
        // Tạo hiệu ứng nổ tại vị trí hiện tại của viên đạn
        Effect impactEffect = new Effect(null, 16, 16, this.worldX, this.worldY, 10, gp, 7, 2, 2, targetX, targetY);
        gp.obj.add(impactEffect);

        // Tăng dần timer để tạo hiệu ứng nhấp nháy laser
        timer++;

        // Tạo hiệu ứng laser từ vị trí viên đạn đến mục tiêu
        if(timer >= 1) {
            // Vẽ laser theo đường từ vị trí hiện tại của viên đạn đến mục tiêu
            int startX = this.worldX;
            int startY = this.worldY;

            // Tạo các điểm ngẫu nhiên xung quanh đường di chuyển để mô phỏng ánh sáng laser
            int laserEndX = targetX + new Random().nextInt(20) - 10;  // Thêm hiệu ứng rung nhỏ
            int laserEndY = targetY + new Random().nextInt(20) - 10;

            // Tạo nhiều đoạn laser nhỏ theo đường di chuyển
            for (int i = 0; i < 5; i++) {
                int segmentX = startX + (laserEndX - startX) * i / 5;  // Tính toán vị trí đoạn
                int segmentY = startY + (laserEndY - startY) * i / 5;

                // Tạo hiệu ứng ánh sáng cho mỗi đoạn
                Effect laserSegment = new Effect(null, 8, 8, segmentX, segmentY, 10, gp, 2, 1, 1, laserEndX, laserEndY);
                gp.obj.add(laserSegment);
            }

            // Reset timer để tiếp tục tạo hiệu ứng
            if(timer >= 3){
                Effect glowEffect = new Effect(null, 16, 16, this.worldX - 10 + new Random().nextInt(20),
                        this.worldY - 10 + new Random().nextInt(20),
                        10, gp, 0, 1.5, 1.5, targetX, targetY);
                gp.obj.add(glowEffect);
                timer = 0;
            }
        }
    }

}
