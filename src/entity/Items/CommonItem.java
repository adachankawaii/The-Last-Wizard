package entity.Items;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class CommonItem extends Entity {
    GamePanel gp;
    String path;
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
        }if(Objects.equals(objName, "CrystalFragment2")){
            for(int i= 0;i<gp.player.items.size();i++){
                if(gp.player.items.get(i).objName.equals("CrystalFragment") || gp.player.items.get(i).objName.equals("AetherCrystal")){
                    gp.obj.remove(this);
                }
            }
        }if(Objects.equals(objName, "CrystalFragment3")){
            for(int i= 0;i<gp.player.items.size();i++){
                if(gp.player.items.get(i).objName.equals("AetherCrystal")){
                    gp.obj.remove(this);
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

