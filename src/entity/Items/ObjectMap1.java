package entity.Items;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ObjectMap1 extends Entity {

    public ArrayList<Rectangle> rectList;
    public GamePanel gp;
    String path;
    public ObjectMap1(String name, String path, GamePanel gp) {
        objName = name;
        collision = true;
        this.rectList = new ArrayList<>();
        this.path = path;
        this.isTrigger = false;
        getObjImage(path);
        this.gp = gp;

    }

    public void getObjImage(String path) {
        // IMPORT NPC
        importAnImage(path, true);

    }

    public int getWidth() {
        imgWidth = animations.get(aniCount).get(0).getWidth();
        return imgWidth;
    }

    public int getHeight() {
        imgHeight = animations.get(aniCount).get(0).getHeight();
        return imgHeight;
    }
    // Phương thức để khởi tạo các rect dựa trên tọa độ
    public void mapRectGet(int a, int b, int w, int h) {
        Rectangle solidArea = new Rectangle();
        solidArea.x = (int)(a);
        solidArea.y = (int)(b);
        solidArea.width = (int)(w);
        solidArea.height = (int)(h);
        this.rectList.add(solidArea);
    }

    // Phương thức để vẽ tất cả các rect đã khởi tạo
    public void mapRectDraw(Graphics2D g2) {
        g2.setColor(Color.red);
        for (Rectangle rect : rectList) {
            g2.drawRect(screenX + rect.x, screenY + rect.y, rect.width, rect.height);
        }
    }

        // Phương thức đọc từ file và thêm vào danh sách rect
    public void loadRectsFromFile(String rectFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(rectFilePath))) {
            String line = br.readLine();
            if (line == null) {
                BufferedImage image = animations.get(aniCount).get(spriteNum);
                mapRectGet(0, 0, image.getWidth(), image.getHeight());
                return;
            }
            do {
                String[] values = line.split(",");
                int a = Integer.parseInt(values[0].trim());
                int b = Integer.parseInt(values[1].trim());
                int w = Integer.parseInt(values[2].trim());
                int h = Integer.parseInt(values[3].trim());
                mapRectGet(a, b, w, h);
            } while ((line = br.readLine()) != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Override
    // public void update() {
    //     spriteCounter++; // Đếm số lần cập nhật
    //     if(spriteCounter > 5) { //
    //         spriteNum++; // Tăng lên để ch
    //         if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
    //         spriteCounter = 0;
    //     }
    // }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjMapImage(g2, gp);
        mapRectDraw(g2);
    }

    public void drawObjMapImage(Graphics2D g2, GamePanel gp) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;
    
        if(worldX + 50*gp.tileSize > gp.player.worldX - gp.player.screenX
        && worldX - 50*gp.tileSize < gp.player.worldX + gp.player.screenX
        && worldY + 50*gp.tileSize > gp.player.worldY - gp.player.screenY
        && worldY - 50*gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            imgWidth = image.getWidth();
            imgHeight = image.getHeight();
            int width = (int)(imgWidth);
            int height = (int)(imgHeight);
            g2.drawImage(image, screenX, screenY, width, height, null);
        }
    }
    // Kiểm tra xem vị trí có nằm trong vùng collision của bất kỳ rect nào trong rectList không
    public boolean isCollisionArea(Point position) {
        for (Rectangle rect : rectList) {
            int rectLeft = worldX + rect.x;
            int rectRight = rectLeft + rect.width;
            int rectTop = worldY + rect.y;
            int rectBottom = rectTop + rect.height;

            if (position.x >= rectLeft && position.x < rectRight &&
                    position.y >= rectTop && position.y < rectBottom) {
                return true;
            }
        }
        return false;
    }

    // @Override
    // public void effect(){
    //     CommonItem commonItem = new CommonItem(this.objName, this.path, gp);
    //     commonItem.worldX = gp.player.worldX;
    //     commonItem.worldY = gp.player.worldY;
    //     gp.obj.add(commonItem);
    // }
}

