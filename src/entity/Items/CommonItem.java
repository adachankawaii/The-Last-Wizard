package entity.Items;

import entity.Entity;
import entity.effect.Effect;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class CommonItem extends Entity {
    GamePanel gp;
    String path;
    int count = 0;
    public CommonItem(String name, GamePanel gp) {
        objName = name;
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 32, 32);
        getNPCImage(path);
        this.gp = gp;
        isItem = true;
        layer = 0;
    }

    public void getNPCImage(String path) {
        switch (objName) {
            case "Key" -> path = "/item/key.png";
            case "Box" -> path = "/item/Micro Chests/micro chest 01 BRONZE1.png";
            case "Feather" -> path = "/item/Feather.png";
            case "Artichoke" -> path = "/item/artichoke.png";
            case "CrystalFragment1" -> path = "/item/mảnh vỡ 1.png";
            case "CrystalFragment2" -> path = "/item/mảnh vỡ 2.png";
            case "CrystalFragment3" -> path = "/item/mảnh vỡ 3.png";
            case "CrystalFragment" -> path = "/item/mất 1 còn 2.png";
            case "AetherCrystal" -> path = "/item/viên lành lặn.png";
            default -> {
            }
        }
        // IMPORT NPC
        importAnImage(path, true);
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) { //
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        if(Objects.equals(objName, "CrystalFragment1")){
            for(int i= 0;i<gp.player.items.size();i++){
                if(gp.player.items.get(i).objName.equals("CrystalFragment1") || gp.player.items.get(i).objName.equals("CrystalFragment") || gp.player.items.get(i).objName.equals("AetherCrystal")){
                    gp.obj.remove(this);
                }
            }
            int bulletCount = 2; // Số đạn từ 1 đến 5

            for (int i = 0; i < bulletCount; i++) {
                // Góc ngẫu nhiên từ 0 đến 360 độ
                double angle = Math.toRadians(Math.random() * 360);

                // Tính toán tọa độ mục tiêu cho từng viên đạn
                int targetBulletX = (int) (worldX + 8 * Math.cos(angle) * gp.tileSize);
                int targetBulletY = (int) (worldY + 8 * Math.sin(angle) * gp.tileSize);

                Effect a = new Effect(null, 16, 16, worldX, worldY, 20, gp, 3, 2, 2, targetBulletX, targetBulletY);
                gp.obj.add(a);
            }
        }if(Objects.equals(objName, "CrystalFragment2")){
            for(int i= 0;i<gp.player.items.size();i++){
                if(gp.player.items.get(i).objName.equals("CrystalFragment") || gp.player.items.get(i).objName.equals("AetherCrystal")){
                    gp.obj.remove(this);
                }

            }
            int bulletCount = 2; // Số đạn từ 1 đến 5

            for (int i = 0; i < bulletCount; i++) {
                // Góc ngẫu nhiên từ 0 đến 360 độ
                double angle = Math.toRadians(Math.random() * 360);

                // Tính toán tọa độ mục tiêu cho từng viên đạn
                int targetBulletX = (int) (worldX + 8 * Math.cos(angle) * gp.tileSize);
                int targetBulletY = (int) (worldY + 8 * Math.sin(angle) * gp.tileSize);

                Effect a = new Effect(null, 16, 16, worldX, worldY, 20, gp, 3, 2, 2, targetBulletX, targetBulletY);
                gp.obj.add(a);
            }
        }if(Objects.equals(objName, "CrystalFragment3")){
            for(int i= 0;i<gp.player.items.size();i++){
                if(gp.player.items.get(i).objName.equals("AetherCrystal")){
                    gp.obj.remove(this);
                }
            }
            int bulletCount = 2; // Số đạn từ 1 đến 5

            for (int i = 0; i < bulletCount; i++) {
                // Góc ngẫu nhiên từ 0 đến 360 độ
                double angle = Math.toRadians(Math.random() * 360);

                // Tính toán tọa độ mục tiêu cho từng viên đạn
                int targetBulletX = (int) (worldX + 8 * Math.cos(angle) * gp.tileSize);
                int targetBulletY = (int) (worldY + 8 * Math.sin(angle) * gp.tileSize);

                Effect a = new Effect(null, 16, 16, worldX, worldY, 20, gp, 3, 2, 2, targetBulletX, targetBulletY);
                gp.obj.add(a);
            }
        }
        if (Objects.equals(objName, "CrystalFragment")){
            int bulletCount = 2; // Số đạn từ 1 đến 5

            for (int i = 0; i < bulletCount; i++) {
                // Góc ngẫu nhiên từ 0 đến 360 độ
                double angle = Math.toRadians(Math.random() * 360);

                // Tính toán tọa độ mục tiêu cho từng viên đạn
                int targetBulletX = (int) (worldX + 8 * Math.cos(angle) * gp.tileSize);
                int targetBulletY = (int) (worldY + 8 * Math.sin(angle) * gp.tileSize);

                Effect a = new Effect(null, 16, 16, worldX, worldY, 20, gp, 3, 2, 2, targetBulletX, targetBulletY);
                gp.obj.add(a);
            }
        }
        if(Objects.equals(objName, "AetherCrystal")){
            if(gp.done) {
                int bulletCount = 2; // Số đạn từ 1 đến 5

                for (int i = 0; i < bulletCount; i++) {
                    // Góc ngẫu nhiên từ 0 đến 360 độ
                    double angle = Math.toRadians(Math.random() * 360);

                    // Tính toán tọa độ mục tiêu cho từng viên đạn
                    int targetBulletX = (int) (worldX + 8 * Math.cos(angle) * gp.tileSize);
                    int targetBulletY = (int) (worldY + 8 * Math.sin(angle) * gp.tileSize);

                    Effect a = new Effect(null, 16, 16, worldX, worldY, 80, gp, 3, 2, 2, targetBulletX, targetBulletY);
                    gp.obj.add(a);
                }
                if (count <= 100) {
                    this.worldY -= 1;
                    count++;
                }
                gp.player.combat = false;
                gp.endgame = true;
            }
            else {
                int bulletCount = 2; // Số đạn từ 1 đến 5

                for (int i = 0; i < bulletCount; i++) {
                    // Góc ngẫu nhiên từ 0 đến 360 độ
                    double angle = Math.toRadians(Math.random() * 360);

                    // Tính toán tọa độ mục tiêu cho từng viên đạn
                    int targetBulletX = (int) (worldX + 8 * Math.cos(angle) * gp.tileSize);
                    int targetBulletY = (int) (worldY + 8 * Math.sin(angle) * gp.tileSize);

                    Effect a = new Effect(null, 16, 16, worldX, worldY, 20, gp, 3, 2, 2, targetBulletX, targetBulletY);
                    gp.obj.add(a);
                }
            }
        }

    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjImage(g2, gp);
        rectDraw(g2);
    }
    @Override
    public void effect(){
        CommonItem commonItem = new CommonItem(this.objName, gp);
        commonItem.worldX = gp.player.worldX;
        commonItem.worldY = gp.player.worldY;
        gp.obj.add(commonItem);
    }
}

