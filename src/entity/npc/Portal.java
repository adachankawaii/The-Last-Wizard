package entity.npc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

import entity.Entity;
import main.FontLoader;
import main.GamePanel;

public class Portal extends Entity {
    Vector<Vector<String>> words = new Vector<>();
    int index = 0;
    int dialogueIndex = 0;
    int timer = 0;
    GamePanel gp;
    String[] choices = {"Yes", "No"}; // Các lựa chọn

    int selectedChoice = -1; // Chỉ số lựa chọn hiện tại (-1: chưa chọn)
    Font smallFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 15);
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);

    public Portal(GamePanel gp) {
        layer = 0;
        objName = "Portal";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(-12, -12, 48*2, 48*2);
        getNPCImage();
        setWords("Bạn có chắc chắn muốn sang khu vực khác?, end");
        setWords("Bạn cần Crystal để đi tiếp,end");
        setWords("..., end");
        setWords("end");
        gp.keyH.SpacePressed = false;
    }

    public void getNPCImage() {
        importAndSliceVertical("/npc/portal.png", 6, 0,0);
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
    boolean isChoosing = false; // Đảm bảo rằng chỉ bật khi vào trạng thái lựa chọn
    boolean choiceMade = false; // Để kiểm tra xem đã thực hiện lựa chọn chưa

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            g2.drawImage(image, screenX - 12, screenY - 12, 48*2, 48*2, null);
        }
        rectDraw(g2);
    }
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){


        Font font = bigFont;
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
                gp.player.interact = objName;
                g2.setColor(new Color(255, 255, 255, 150)); // Màu mờ
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight); // Làm mờ toàn bộ màn hình
                // Vẽ khung hội thoại
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);
                int shopKeeperWidth = gp.tileSize * 3; // Chiều rộng hình ShopKeeper
                int shopKeeperHeight = gp.tileSize * 3; // Chiều cao hình ShopKeeper
                int shopKeeperX = dialogueBoxX + 10; // Vị trí x của ShopKeeper
                int shopKeeperY = dialogueBoxY - shopKeeperHeight - 10; // Vị trí y của ShopKeeper

                // Vẽ hình ShopKeeper (ảnh đã được load vào trong biến 'image' thông qua getNPCImage)
                g2.drawImage(animations.get(0).get(0), shopKeeperX, shopKeeperY, shopKeeperWidth, shopKeeperHeight, null);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);
                String currentDialogue = words.get(index).get(dialogueIndex);

                // Khi đến đoạn hội thoại yêu cầu lựa chọn
                if (dialogueIndex == 0 && !choiceMade && index == 0) {

                    int choiceBoxWidth = (int)(dialogueBoxWidth * 2 / 3);
                    int choiceBoxHeight = (int)(dialogueBoxHeight * 3 / 4);
                    int choiceBoxY = dialogueBoxY - dialogueBoxHeight - choiceBoxHeight; // Di chuyển xuống dưới khung hội thoại
                    int choiceBoxX = dialogueBoxX + dialogueBoxWidth - (dialogueBoxWidth * 2 / 3) - 20; // Đặt bên phải khung hội thoại
                    int screenMouseX = gp.mouseX - (gp.player.worldX - gp.player.screenX);
                    int screenMouseY = gp.mouseY - (gp.player.worldY - gp.player.screenY);
                    isChoosing = true;
                    // Hiển thị các lựa chọn
                    for (int i = 0; i < choices.length; i++) {
                        Rectangle r = new Rectangle(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40);
                        g2.setColor(new Color(0, 0, 0, 180));
                        g2.fillRoundRect(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40, 15, 15);
                        g2.setColor(Color.WHITE);
                        g2.setFont(smallFont);
                        g2.drawRoundRect(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40, 15, 15);
                        g2.drawString("Key " + (i + 1) + ": " + choices[i], choiceBoxX + 10, choiceBoxY + i * choiceBoxHeight + 20);
                        if (r.contains(screenMouseX, screenMouseY)) {
                            selectedChoice = i;  // Gán lựa chọn dựa trên vị trí của chuột
                        }
                        // Đánh dấu lựa chọn hiện tại
                        if (i == selectedChoice) {
                            g2.setColor(Color.WHITE);
                            g2.drawRoundRect(choiceBoxX - 5, choiceBoxY + i * choiceBoxHeight , choiceBoxWidth + 10, 40, 15, 15);
                            g2.setColor(new Color(100, 100, 100, 100));
                            g2.fillRoundRect(choiceBoxX - 5, choiceBoxY + i * choiceBoxHeight , choiceBoxWidth + 10, 40, 15, 15);
                        }
                    }

                    // Xử lý khi người chơi nhấn 'Space' để chọn
                    if ((gp.keyH.SpacePressed || gp.mouseH.isClicked) && selectedChoice != -1 && timer <= 0) {
                        dialogueIndex = 0; // Chuyển sang đoạn hội thoại tương ứng
                        if(selectedChoice == 0){
                            int pos = -1; // Vị trí mặc định nếu không tìm thấy
                            for (int i = 0; i < gp.player.items.size(); i++) {
                                if (gp.player.items.get(i).objName.contains("Crystal")) {
                                    pos = i; // Lưu vị trí của item có chứa "Crystal"
                                    break;   // Dừng vòng lặp sau khi tìm thấy
                                }
                            }
                            if(pos == -1) index = 1;
                            else{
                                gp.fadeAlpha = 0;
                                gp.fadingIn = true;
                                index = 3;
                                gp.map++;
                                gp.dialogueIndex = 0;
                                gp.nextMap();
                            }
                        }
                        else{
                            index = 2;
                        }
                        gp.keyH.SpacePressed = false;
                        timer = 20;
                        choiceMade = true; // Đã chọn
                        isChoosing = false; // Hoàn thành quá trình lựa chọn
                    }
                }
                // Sau khi hoàn thành lựa chọn, tiếp tục hiển thị đoạn hội thoại
                else if (!isChoosing) {
                    // Sau khi chọn, nếu có đoạn hội thoại tiếp theo
                    if ((gp.keyH.SpacePressed || gp.mouseH.isClicked) && dialogueIndex < words.get(index).size() - 1 && timer <= 0) {
                        dialogueIndex++; // Chuyển sang đoạn tiếp theo
                        gp.keyH.SpacePressed = false;
                        timer = 20;
                    }
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
                if(index >= 1 && dialogueIndex == (words.get(index).size() - 2)) g2.setColor(Color.PINK);
                else g2.setColor(Color.WHITE);
                g2.drawString(objName + ": " + currentDialogue, textX, textY);
            }
            else if (gp.player.combat && timer <= 0) {
                String prompt = "Press Space to interact";
                int promptWidth = g2.getFontMetrics(bigFont).stringWidth(prompt);
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
                g2.setFont(bigFont);
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
            index = 0;
            dialogueIndex = 0;
            isChoosing = false; // Reset trạng thái lựa chọn
            choiceMade = false;
        }

    }
    // Bấm Space để tương tác, lựa chọn va để chạy hội thoại
    // Các phím 1, 2, 3,... đế trỏ vào lựa chọn muốn chọn, rồi bấm space để chọn
}



