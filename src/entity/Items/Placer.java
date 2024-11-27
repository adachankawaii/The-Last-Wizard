package entity.Items;

import entity.Entity;
import main.FontLoader;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;
import java.util.Vector;

public class Placer extends Entity {
    GamePanel gp;
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);
    int target = -1;
    boolean placed = false;
    public Placer(GamePanel gp, String objname, int target, int worldX, int worldY) {
        layer = -1;
        objName = objname;
        direction = "down";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(8,0,gp.tileSize,gp.tileSize);
        this.worldX = worldX;
        this.worldY = worldY;
        importAnImage("/UI/Border.png", true);
        this.target = target;
        if(target < 11){
            done = true;
        }
        else{
            done = false;
        }
    }
    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) { //
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        placed = false;
        Vector<Integer> vector = gp.cCheck.checkObjectForObj(this);
        for(int i : vector){
            if(gp.obj.get(i) != null){
                if(Objects.equals(gp.obj.get(i).objName, "Box")){
                    placed = true;
                    done = true;
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
    public void drawUI(Graphics2D g2, GamePanel gp){
        if(placed) {
            // Vẽ giá trị của target ở giữa ô
            g2.setFont(bigFont); // Sử dụng font đã tải sẵn
            g2.setColor(Color.PINK); // Màu chữ
            String targetStr = String.valueOf(target); // Chuyển target thành chuỗi
            FontMetrics metrics = g2.getFontMetrics(bigFont);

            // Tính toán vị trí chính giữa ô
            int textX = (worldX - gp.player.worldX + gp.player.screenX) + (gp.tileSize / 2) - (metrics.stringWidth(targetStr) / 2) + 10;
            int textY = (worldY - gp.player.worldY + gp.player.screenY) + (gp.tileSize / 2) + (metrics.getAscent() / 2) - (metrics.getDescent() / 2) - 24;

            // Vẽ chuỗi
            g2.drawString(targetStr, textX, textY);
        }
    }
}
