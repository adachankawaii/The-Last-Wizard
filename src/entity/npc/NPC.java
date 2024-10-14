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
        objName = "NPC";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        setWords(" ,default text 1,default text 2,default text 3,we are x and 1/x");
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
        Font font = new Font("Arial", Font.PLAIN, 20);
        FontMetrics metrics = g2.getFontMetrics(font);

        // Tính toán vị trí để căn giữa chữ hoặc hộp thoại
        int textWidth = metrics.stringWidth("Press Space to interact");
        screenX = worldX - gp.player.worldX + gp.player.screenX + this.solidArea.width / 2 - textWidth / 2;
        screenY = worldY - gp.player.worldY + gp.player.screenY - 5;

        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));

        // Nếu khoảng cách <= 1 tile (gp.tileSize), hiển thị chữ hoặc hội thoại trên đầu NPC
        if (distance <= 1.5 * gp.tileSize) {
            g2.setFont(font);
            g2.setColor(Color.BLACK);  // Màu chữ

            // Nếu người chơi đang tương tác (combat = false) thì hiển thị hội thoại
            if (!gp.player.combat && !words.isEmpty()) {
                // Sử dụng dialogueIndex để theo dõi câu thoại hiện tại
                String currentDialogue = words.get(dialogueIndex);  // Lấy câu thoại hiện tại
                textWidth = metrics.stringWidth(currentDialogue);
                screenX = worldX - gp.player.worldX + gp.player.screenX + this.solidArea.width / 2 - textWidth / 2;

                //g2.setColor(Color.white);
                //g2.fillRect(screenX, screenY - metrics.getHeight() + 2, textWidth, metrics.getHeight());

                // Hiển thị câu thoại hiện tại
                g2.setColor(Color.BLACK);
                g2.drawString(currentDialogue, screenX, screenY);

                // Khi người chơi nhấn Space, và timer <= 0 để tránh xử lý quá nhanh
                if (gp.keyH.SpacePressed && timer <= 0) {
                    // Chuyển sang câu thoại tiếp theo nếu có
                    if (dialogueIndex < words.size() - 1) {
                        dialogueIndex++;
                        timer = 30;  // Đặt timer về một giá trị lớn để tránh xử lý quá nhanh (30 frames)
                        gp.keyH.SpacePressed = false;  // Đặt lại trạng thái của phím Space
                    } else {
                        // Khi đến câu thoại cuối cùng, đặt combat về true để kết thúc hội thoại
                        gp.keyH.SpacePressed = false;
                        gp.player.combat = true;
                        dialogueIndex = 0;  // Reset lại để có thể lặp lại cuộc hội thoại
                        timer = 30;  // Thời gian chờ trước khi người chơi có thể tương tác lại
                    }
                }

            } else if (gp.player.combat && timer <= 0) {
                // Nếu người chơi chưa bắt đầu tương tác, hiển thị "Press Space to interact"
               // g2.setColor(Color.white);
               // g2.fillRect(screenX, screenY - metrics.getHeight() + 2, textWidth, metrics.getHeight());
                g2.setColor(Color.BLACK);
                g2.drawString("Press Space to interact", screenX, screenY);

                // Khi người chơi nhấn Space và combat đang true (chưa bắt đầu hội thoại)
                if (gp.keyH.SpacePressed && timer <= 0) {
                    gp.player.combat = false;  // Bắt đầu hội thoại
                    timer = 0;  // Đặt timer để tránh lặp lại quá nhanh
                }

            }
        }
        else {
            gp.keyH.SpacePressed = false;
        }

        drawObjImage(g2, gp);  // Vẽ NPC
        rectDraw(g2);  // Vẽ ô rect nhận diện collision nếu cần
    }

}
