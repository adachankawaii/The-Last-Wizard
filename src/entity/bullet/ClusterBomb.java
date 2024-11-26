package entity.bullet;

import main.GamePanel;

import java.util.Random;

public class ClusterBomb extends ThrowingObj{
    public ClusterBomb(String path, String name, int solidAreaX, int solidAreaY, int rectX, int rectY, int worldX, int worldY, int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY, int TtargetX, int TtargetY) {
        super(path, name, solidAreaX, solidAreaY, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY, TtargetX, TtargetY);
    }
    @Override
    public void Boom(){
        int bulletCount = 8; // Ví dụ: 5 viên đạn
        double angleStep = Math.toRadians(360.0 / (bulletCount - 1)); // Bước góc (-180 đến 0 độ)

        // Bắn từng luồng đạn
        for (int i = 0; i < bulletCount; i++) {
            // Góc hiện tại của từng viên đạn
            double angle = Math.toRadians(180) + i * angleStep;

            // Tính toán tọa độ mục tiêu cho từng viên đạn
            int targetBulletX = (int) (worldX + Math.cos(angle) * 12*gp.tileSize); // Khoảng cách từ tâm là 25
            int targetBulletY = (int) (worldY + Math.sin(angle) * 12*gp.tileSize);

            // Tạo đối tượng đạn
            Bullet b = new Bullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet",
                    0, 0, 8 * 6, 8 * 6,
                    (int)(worldX), (int)(worldY), 18, gp,
                    0, 8, 1, 1,
                    targetBulletX, targetBulletY);
            b.root = this.objName;

            // Thêm đạn vào danh sách đối tượng
            gp.obj.add(b);
    }}
}
