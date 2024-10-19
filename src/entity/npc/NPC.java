package entity.npc;

import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

import entity.Entity;
import main.GamePanel;

public class NPC extends Entity {
    Vector<Vector<String>> words = new Vector<>();
    int index = 0;
    int dialogueIndex = 0;
    int timer = 0;
    GamePanel gp;
    String[] choices = {"Choice 1", "Choice 2", "Choice 3"}; // Các lựa chọn
    int selectedChoice = -1; // Chỉ số lựa chọn hiện tại (-1: chưa chọn)

    public NPC(GamePanel gp) {
        objName = "Te Quiero";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        setWords("What can I help you ?,default text 1,default text 2,default text 3,default text 4");
        setWords("Choice 1, great choice, end");
        setWords("Choice 2,stupid, end");
        setWords("Choice 3,aaaaaaaaaa, end");

    }

    public void getNPCImage() {
        importAnImage("/npc/merchant.png", true);
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

    public void setWords(String inputWords) {
        words.add(new Vector<>());
        String[] splitWords = inputWords.split(",");
        words.get(words.size() - 1).addAll(Arrays.asList(splitWords));
    }

    // Thêm biến để theo dõi việc đã hiển thị đoạn dialogueChoice chưa
    // Biến để theo dõi trạng thái lựa chọn
    // Biến để theo dõi trạng thái lựa chọn
    private boolean isChoosing = false; // Đảm bảo rằng chỉ bật khi vào trạng thái lựa chọn
    private boolean choiceMade = false; // Để kiểm tra xem đã thực hiện lựa chọn chưa

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
                // Vẽ khung hội thoại
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);

                g2.setColor(Color.WHITE);
                g2.drawRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);
                String currentDialogue = words.get(index).get(dialogueIndex);

                // Khi đến đoạn hội thoại yêu cầu lựa chọn
                if (dialogueIndex == 2 && !choiceMade && index == 0) {
                    int choiceBoxY = dialogueBoxY - 50;
                    int choiceBoxX = dialogueBoxX + 20;
                    isChoosing = true;
                    // Hiển thị các lựa chọn
                    for (int i = 0; i < choices.length; i++) {
                        g2.setColor(new Color(0, 0, 0, 180));
                        g2.fillRoundRect(choiceBoxX, choiceBoxY - i * 40, 200, 30, 15, 15);
                        g2.setColor(Color.WHITE);
                        g2.drawRoundRect(choiceBoxX, choiceBoxY - i * 40, 200, 30, 15, 15);
                        g2.drawString(choices[i], choiceBoxX + 10, choiceBoxY - i * 40 + 20);

                        // Đánh dấu lựa chọn hiện tại
                        if (i == selectedChoice) {
                            g2.setColor(Color.WHITE);
                            g2.drawRoundRect(choiceBoxX - 5, choiceBoxY - i * 40 - 5, 210, 40, 15, 15);
                            g2.setColor(new Color(100, 100, 100, 100));
                            g2.fillRoundRect(choiceBoxX - 5, choiceBoxY - i * 40 - 5, 210, 40, 15, 15);
                        }
                        g2.drawString(choices[i], choiceBoxX + 10, choiceBoxY - i * 40 + 20);
                    }

                    // Xử lý khi người chơi nhấn 'Space' để chọn
                    if (gp.keyH.SpacePressed && selectedChoice != -1 && timer <= 0) {
                        dialogueIndex = 0; // Chuyển sang đoạn hội thoại tương ứng
                        index = selectedChoice + 1;
                        gp.keyH.SpacePressed = false;
                        timer = 20;
                        choiceMade = true; // Đã chọn
                        isChoosing = false; // Hoàn thành quá trình lựa chọn
                    }
                }
                // Sau khi hoàn thành lựa chọn, tiếp tục hiển thị đoạn hội thoại
                else if (!isChoosing) {
                    // Sau khi chọn, nếu có đoạn hội thoại tiếp theo
                    if (gp.keyH.SpacePressed && dialogueIndex < words.get(index).size() - 1 && timer <= 0) {
                        dialogueIndex++; // Chuyển sang đoạn tiếp theo
                        gp.keyH.SpacePressed = false;
                        timer = 20;
                    }
                }

                // Cập nhật lựa chọn
                if (isChoosing) {
                    selectedChoice = (gp.keyH.i + choices.length) % choices.length; // Đảm bảo chỉ chọn trong khoảng từ 0 đến số lượng lựa chọn
                }

                // Khi kết thúc hội thoại
                if (dialogueIndex >= words.get(index).size() - 1) {
                    gp.player.combat = true;
                    gp.keyH.SpacePressed = false;
                    index = 0;
                    dialogueIndex = 0; // Đặt lại để sẵn sàng cho lần sau
                    choiceMade = false; // Cho phép lựa chọn lại lần sau
                }

                g2.setFont(font);
                g2.setColor(Color.WHITE);
                g2.drawString(objName + ": " + currentDialogue, textX, textY);
            }
            else if (gp.player.combat && timer <= 0) {
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
        } else {
            // Reset các biến khi ra khỏi phạm vi
            gp.keyH.SpacePressed = false;
            index = 0;
            dialogueIndex = 0;
            isChoosing = false; // Reset trạng thái lựa chọn
            choiceMade = false;
        }

        drawObjImage(g2, gp);
        rectDraw(g2);
    }

}



