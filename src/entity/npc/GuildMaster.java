package entity.npc;

import entity.Entity;
import entity.player.Quest;
import main.FontLoader;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class GuildMaster extends Entity {
    Vector<Vector<String>> words = new Vector<>();
    int index = 0;
    int dialogueIndex = 0;
    int timer = 0;
    GamePanel gp;
    Vector<Quest> questsLine = new Vector<>();
    Font smallFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 15);
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);
    public GuildMaster(GamePanel gp) {
        layer = 0;
        objName = "GuildMaster";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        questsLine.add(new Quest("Get the Key","Get the Key from the ShopKeeper", "Items", 1,"Key","Coin", 15, gp));
        questsLine.add(new Quest("Get the HPBottle","Get the HPBottle", "Items", 1,"HPBottle","Coin", 15, gp));
        questsLine.add(new Quest("Defeat the enemies","Clear this area, cover it with blood","kills",4,null,"ThrowingBottle", 13, gp));
        questsLine.add(new Quest("Talk to NPC","Talk to Te Quiero","interact",1,"Te Quiero","Coin", 28,gp));
        addWords(new String[]{"Look like you have completed the quest","This is your reward","end"});
        addWords(new String[]{"Nothing here for you !", "end"});
        String[] tmp = new String[questsLine.size()+2];
        for (int i = 0; i < tmp.length;i++){
            if(i == 0){
                tmp[i] = "Complete these, and you can encounter the boss";
            }
            else if(i == tmp.length -1){
                tmp[i] = "end";
            }
            else{
                tmp[i] = questsLine.get(i-1).description;
            }
        }
        addWords(tmp);
        gp.keyH.SpacePressed = false;
    }

    public void getNPCImage() {
        importAndSlice("/npc/wizard idle.png", 4, 0 ,0 );
    }
    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 8) {
            spriteNum++;
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        timer--;
        if(questsLine.isEmpty() && gp.player.quests.isEmpty()){
            gp.player.completed = true;
        }
        else {
            gp.player.completed = false;
        }
    }

    public void addWords(String[] inputWords) {
        words.add(new Vector<>());
        words.get(words.size() - 1).addAll(List.of(inputWords));
    }

    // Thêm biến để theo dõi việc đã hiển thị đoạn dialogueChoice chưa
    // Biến để theo dõi trạng thái lựa chọn
    // Biến để theo dõi trạng thái lựa chọn

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            g2.drawImage(image, screenX-16  , screenY-16, 48*2, 48*2, null);
        }
        rectDraw(g2);
    }
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){
        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));

        // Nếu khoảng cách <= 1.5 tile, hiển thị hội thoại hoặc nhắc nhở
        if (distance <= 1.5 * gp.tileSize) {
            g2.setFont(smallFont);
            int textWidth = g2.getFontMetrics(smallFont).stringWidth(objName);
            screenX = worldX - gp.player.worldX + gp.player.screenX + this.solidArea.width / 2 - textWidth / 2;
            screenY = worldY - gp.player.worldY + gp.player.screenY - 5;
            g2.setColor(new Color(100,100,100,120));
            g2.fillRect(screenX - 2,screenY - 12,textWidth + 5,15);
            g2.setColor(Color.WHITE);
            g2.drawString(objName, screenX, screenY);
        }
        Font font = bigFont;
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


                if ((gp.keyH.SpacePressed || gp.mouseH.isClicked) && dialogueIndex < words.get(index).size() - 1 && timer <= 0) {
                    dialogueIndex++; // Chuyển sang đoạn tiếp theo
                    gp.keyH.SpacePressed = false;
                    timer = 20;
                }
                if (dialogueIndex >= words.get(index).size() - 1) {
                    if (index == 0) {
                        Iterator<Quest> iterator = gp.player.quests.iterator();
                        while (iterator.hasNext()) {
                            Quest quest = iterator.next();
                            if (quest.isComplete) {
                                quest.getReward();
                                iterator.remove(); // Xóa phần tử an toàn bằng Iterator
                            }
                        }
                    }
                    else if(index == 2){
                        Iterator<Quest> iterator = questsLine.iterator();
                        while (iterator.hasNext()) {
                            Quest quest = iterator.next();
                            gp.player.addQuest(quest);
                            iterator.remove();
                        }
                    }
                    gp.player.combat = true;
                    gp.keyH.SpacePressed = false;
                    boolean flag = false;
                    for(Quest quest : gp.player.quests){
                        if (quest.isComplete) {
                            flag = true;
                            index = 0;
                            break;
                        }
                    }
                    if(!flag) index = 1;
                    if(!questsLine.isEmpty() && !flag) index = 2;
                    dialogueIndex = 0; // Đặt lại để sẵn sàng cho lần sau
                }

                g2.setFont(font);
                g2.setColor(Color.WHITE);
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

                if ((gp.keyH.SpacePressed) && timer <= 0) {
                    gp.player.combat = false;
                    gp.keyH.SpacePressed = false;
                    timer = 20;
                }
            }
        } else {
            // Reset các biến khi ra khỏi phạm vi
            boolean flag = false;
            for(Quest quest : gp.player.quests){
                if (quest.isComplete) {
                    flag = true;
                    index = 0;
                    break;
                }
            }
            if(!flag) index = 1;
            if(!questsLine.isEmpty() && !flag) index = 2;
            dialogueIndex = 0;
        }

    }
    // Bấm Space để tương tác, lựa chọn va để chạy hội thoại
    // Các phím 1, 2, 3,... đế trỏ vào lựa chọn muốn chọn, rồi bấm space để chọn
}



