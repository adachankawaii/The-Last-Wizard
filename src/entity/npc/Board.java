package entity.npc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
// import java.util.Arrays;
// import java.util.Vector;

import entity.Entity;
import main.FontLoader;
import main.GamePanel;

import javax.imageio.ImageIO;

public class Board extends Entity {
    int timer = 0;
    GamePanel gp;

    Font smallFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 15);
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);
    BufferedImage imgs = null;

    public Board(GamePanel gp, String name, String path, String imgPath) {
        layer = 0;
        objName = name;
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        importAnImage(path, true);
        try (InputStream is = getClass().getResourceAsStream(imgPath)) {
            imgs = ImageIO.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gp.keyH.SpacePressed = false;
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) {
            spriteNum++;
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        timer--;
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {

        drawObjImage(g2, gp);
        rectDraw(g2);
    }
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp) {
        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));

        // Nếu khoảng cách <= 1.5 tile, hiển thị hội thoại hoặc nhắc nhở
        if (distance <= 2 * gp.tileSize) {
            g2.setFont(smallFont);
            int textWidth = g2.getFontMetrics(smallFont).stringWidth(objName);
            screenX = worldX - gp.player.worldX + gp.player.screenX + this.solidArea.width / 2 - textWidth / 2;
            screenY = worldY - gp.player.worldY + gp.player.screenY - 5;
            g2.setColor(new Color(100, 100, 100, 120));
            g2.fillRect(screenX - 2, screenY - 12, textWidth + 5, 15);
            g2.setColor(Color.WHITE);
            g2.drawString(objName, screenX, screenY);
        }

        Font font = bigFont;


        // Nếu khoảng cách <= 1.5 tile, hiển thị hội thoại hoặc nhắc nhở
        if (distance <= 2 * gp.tileSize) {
            if (!gp.player.combat) {
                gp.player.interact = objName;
                g2.setColor(new Color(255, 255, 255, 150)); // Màu mờ
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight); // Làm mờ toàn bộ màn hình
                if (img != null) {
                    int imgWidth = img.getWidth()/4;
                    int imgHeight = img.getHeight()/4;

                    int imgX = (gp.screenWidth - imgWidth) / 2;
                    int imgY = (gp.screenHeight - imgHeight) / 2;

                    g2.drawImage(img, imgX, imgY,imgWidth,imgHeight, null);
                }
                if ((gp.keyH.SpacePressed || gp.mouseH.isClicked) && timer <= 0) {
                    gp.player.combat = true;
                    gp.keyH.SpacePressed = false;
                    timer = 20;
                }
            }
            else if (timer <= 0) {
                g2.setFont(font);
                String prompt = "Press Space to interact";
                int promptWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 20)).stringWidth(prompt);
                int promptX = (gp.screenWidth - promptWidth) / 2;
                int promptY = gp.screenHeight - gp.tileSize - 40;

                int boxWidth = promptWidth + 20;
                int boxHeight = 40;
                int boxX = promptX - 10;
                int boxY = promptY - 30;

                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.setColor(Color.WHITE);
                g2.drawString(prompt, promptX, promptY);

                if (gp.keyH.SpacePressed && timer <= 0) {
                    gp.player.combat = false;
                    gp.keyH.SpacePressed = false;
                    timer = 20;
                }
            }
        }
    }

    // Bấm Space để tương tác, lựa chọn va để chạy hội thoại
    // Các phím 1, 2, 3,... đế trỏ vào lựa chọn muốn chọn, rồi bấm space để chọn
}



