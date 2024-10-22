package entity.Items;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends Entity {
    public Coin(int x, int y, GamePanel gamePanel) {
        objName = "Coin";
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 16, 18);
        getNPCImage();
        this.worldX = x;
        this.worldY = y;
        this.gp = gamePanel;
    }

    public void getNPCImage() {

        // IMPORT NPC
        importAndSlice("/effect/GoldCoinSpinning.png", 24, 0, 0);
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if (spriteCounter > 3) {
            spriteNum++; // Tăng lên để chuyển đổi giữa các khung hình
            if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }

        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        // Tính khoảng cách giữa coin và người chơi
        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));

        // Nếu khoảng cách nhỏ hơn hoặc bằng 2 tile, đồng xu sẽ bay về phía người chơi
        if (distance <= 4 * gp.tileSize) {
            // Tính vector hướng từ coin đến người chơi
            double directionX = playerCenterX - npcCenterX;
            double directionY = playerCenterY - npcCenterY;
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY); // Tính độ lớn của vector

            // Chuẩn hóa vector (đưa về vector đơn vị)
            directionX /= magnitude;
            directionY /= magnitude;

            // Tốc độ di chuyển của coin (có thể điều chỉnh)
            double speed = 8.0; // Tốc độ di chuyển

            // Cập nhật vị trí của đồng xu (Coin) theo hướng của người chơi
            worldX += (int)(directionX * speed);
            worldY += (int)(directionY * speed);
        }
    }


    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            g2.drawImage(image, screenX, screenY, (int)(0.3 * gp.tileSize), (int)(0.3 * gp.tileSize) + 4, null);
        }
        rectDraw(g2);
    }
}
