package entity.npc;

import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

import entity.Entity;
import main.GamePanel;

public class NPC extends Entity {
    Vector<String> words = new Vector<>();
    int dialogueIndex = 0;
    int timer = 0;
    GamePanel gp;
    public NPC(GamePanel gp) {
        objName = "Te Quiero";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        setWords(" ,default text 1,default text 2,default text 3,default text 4");
    }

    public void getNPCImage() {

        // IMPORT NPC
        importAnImage("/npc/merchant.png", true);
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) { // 
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        timer--;
    }

    public void setWords(String inputWords) {
        // Tách chuỗi inputWords thành các từ dựa trên khoảng trắng hoặc dấu phẩy
        String[] splitWords = inputWords.split(",");
        words.addAll(Arrays.asList(splitWords));
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        int textWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 15)).stringWidth(objName);
        screenX = worldX - gp.player.worldX + gp.player.screenX + this.solidArea.width / 2 - textWidth / 2;
        screenY = worldY - gp.player.worldY + gp.player.screenY - 5;

        g2.setColor(Color.BLACK);
        g2.drawString(objName, screenX, screenY);

        Font font = new Font("Arial", Font.PLAIN, 20);
        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));

        // Nếu khoảng cách <= 1.5 tile, hiển thị hội thoại hoặc nhắc nhở
        if (distance <= 1.5 * gp.tileSize) {
            int dialogueBoxHeight = gp.tileSize * 2;
            int dialogueBoxY = gp.screenHeight - dialogueBoxHeight - 10;
            int dialogueBoxX = 20;
            int dialogueBoxWidth = gp.screenWidth - 40;

            int textX = dialogueBoxX + 20;
            int textY = dialogueBoxY + 40;

            if (!gp.player.combat && !words.isEmpty()) {
                // Nếu đang trong chế độ hội thoại, hiển thị hội thoại
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);

                g2.setColor(Color.WHITE);
                g2.drawRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);

                String currentDialogue = words.get(dialogueIndex);
                g2.setFont(font);
                g2.setColor(Color.WHITE);
                g2.drawString(objName + ": " + currentDialogue, textX, textY);

                if (gp.keyH.SpacePressed && timer <= 0) {
                    if (dialogueIndex < words.size() - 1) {
                        dialogueIndex++;
                        timer = 20;
                        gp.keyH.SpacePressed = false;
                    } else {
                        gp.keyH.SpacePressed = false;
                        gp.player.combat = true;
                        dialogueIndex = 0;
                        timer = 50;
                    }
                }
            } else if (gp.player.combat && timer <= 0) {
                // Nếu không trong hội thoại, hiển thị khung thông báo "Press Space to interact"
                String prompt = "Press Space to interact";

                int promptWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 20)).stringWidth(prompt);
                int promptX = (gp.screenWidth - promptWidth) / 2;
                int promptY = gp.screenHeight - gp.tileSize - 40;

                // Vẽ khung nền cho thông báo
                int boxWidth = promptWidth + 20;
                int boxHeight = 40;
                int boxX = promptX - 10;
                int boxY = promptY - 30;

                g2.setColor(new Color(0, 0, 0, 180));  // Nền màu đen trong suốt
                g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

                g2.setColor(Color.WHITE);  // Viền màu trắng
                g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

                // Vẽ chữ "Press Space to interact"
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.setColor(Color.WHITE);
                g2.drawString(prompt, promptX, promptY);

                // Khi nhấn Space, bắt đầu hội thoại
                if (gp.keyH.SpacePressed && timer <= 0) {
                    gp.player.combat = false;
                    timer = 0;
                }
            }
        } else {
            gp.keyH.SpacePressed = false;
        }

        drawObjImage(g2, gp);  // Vẽ hình NPC
        rectDraw(g2);  // Vẽ ô nhận diện collision nếu cần
    }



}
