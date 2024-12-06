package entity.Items;

import entity.Entity;
import entity.bullet.FireBullet;
import entity.effect.Effect;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class Skill extends Entity {
    public Skill(GamePanel gp, String name) {
        objName = name;
        isSkill = true;
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        isItem = true;
        this.gp = gp;
    }

    public void getNPCImage() {
        if(Objects.equals(objName, "Bell")) importAnImage("/item/Bell.png", true);
        else if(Objects.equals(objName, "MagicBook")) importAnImage("/item/book.png", true);
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) { //
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjImage(g2, gp);
        rectDraw(g2);
    }
    @Override
    public void effect(){
        if(!gp.player.call && gp.player.Energy >= 50 && objName.equals("Bell")) {
                Crow crow = new Crow(gp);
                crow.worldX = gp.player.worldX;
                crow.worldY = gp.player.worldY;
                Effect a = new Effect("/effect/effect1.png", 0, 0, crow.worldX, crow.worldY, 10, gp, 0, 2, 2, 0, 0);
                gp.obj.add(a);
                gp.obj.add(crow);
                gp.player.call = true;
                gp.player.Energy -= 50;
        }
        else if(gp.player.Energy >= 25) {
        for (int i = -1; i <= 1; i++) { // i = -1, 0, 1 tương ứng với các góc lệch
            // Tính góc bắn ban đầu (theo radian)
            double angle = Math.atan2(gp.mouseY - gp.player.worldY, gp.mouseX - gp.player.worldX);

            // Thêm góc lệch (-30°, 0°, +30°) - đổi đơn vị sang radian
            double angleOffset = Math.toRadians(i * 30); // -30°, 0°, +30°
            double finalAngle = angle + angleOffset;

            // Tính vị trí mới của mục tiêu (mouseX, mouseY) dựa trên góc lệch
            int newMouseX = gp.player.worldX + (int) (Math.cos(finalAngle) * 1000); // Xác định mục tiêu xa hơn
            int newMouseY = gp.player.worldY + (int) (Math.sin(finalAngle) * 1000); // Y xác định mục tiêu xa hơn

            // Tạo đối tượng viên đạn với mục tiêu mới
            FireBullet b = new FireBullet(
                    "/effect/effect1.png", "Bigbullet", 4, 4, 16, 16,
                    gp.player.worldX, gp.player.worldY, 25, gp,
                    0, 10, 1, 1,
                    newMouseX, newMouseY, "/effect/effect1.png"
            );
            b.death = false;
            gp.soundManager.play("wand");
            gp.player.Energy -= 25;
            // Thêm viên đạn vào danh sách
            gp.obj.add(b);

        }
    }
    }
    }




