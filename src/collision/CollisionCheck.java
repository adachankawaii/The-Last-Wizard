package collision;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    
    public int checkObject(Entity entity, boolean player) {

        int index = 999;
    
        for (int i = 0; i < gp.obj.size(); i++) {
            if(gp.obj.get(i) != null && gp.obj.get(i).collision) {
    
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
            
                // Get the object's solid area position
                gp.obj.get(i).solidArea.x = gp.obj.get(i).worldX + gp.obj.get(i).solidArea.x;
                gp.obj.get(i).solidArea.y = gp.obj.get(i).worldY + gp.obj.get(i).solidArea.y;

                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                    case "up-left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "up-right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down-left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                    case "down-right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                }
    
                if (entity.solidArea.intersects(gp.obj.get(i).solidArea)) {
                    if (gp.obj.get(i).collision) {
                        if(!gp.obj.get(i).isTrigger && !gp.obj.get(i).isEnemy) entity.collisionOn = true;
                        else {
                            if(entity.isTrigger) entity.isTriggerOn = true;
                        }
                    }
                    if (player) index = i;
                }
    
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj.get(i).solidArea.x = gp.obj.get(i).solidAreaDefaultX;
                gp.obj.get(i).solidArea.y = gp.obj.get(i).solidAreaDefaultY;
            }            
        }
        return index;
    }
    public void checkMapObject(Entity entity) {

        for (int i = 0; i < gp.objMap1.size(); i++) {
            if (gp.objMap1.get(i) != null && gp.objMap1.get(i).collision) {

                // Duyệt qua tất cả các Rectangle trong rectList của objMap1
                ArrayList<Rectangle> rectList = gp.objMap1.get(i).rectList;
                for (Rectangle rect : rectList) {

                    // Tạo các biến tạm thời để lưu vị trí solid area của entity
                    int entitySolidAreaX = entity.worldX + entity.solidArea.x;
                    int entitySolidAreaY = entity.worldY + entity.solidArea.y;

                    // Cập nhật vị trí solid area của entity dựa trên hướng di chuyển
                    switch (entity.direction) {
                        case "up":
                            entitySolidAreaY -= entity.speed;
                            break;
                        case "down":
                            entitySolidAreaY += entity.speed;
                            break;
                        case "left":
                            entitySolidAreaX -= entity.speed;
                            break;
                        case "right":
                            entitySolidAreaX += entity.speed;
                            break;
                        case "up-left":
                            entitySolidAreaX -= entity.speed;
                            entitySolidAreaY -= entity.speed;
                            break;
                        case "up-right":
                            entitySolidAreaX += entity.speed;
                            entitySolidAreaY -= entity.speed;
                            break;
                        case "down-left":
                            entitySolidAreaX -= entity.speed;
                            entitySolidAreaY += entity.speed;
                            break;
                        case "down-right":
                            entitySolidAreaX += entity.speed;
                            entitySolidAreaY += entity.speed;
                            break;
                    }

                    // Cập nhật vị trí của rect
                    int rectX = gp.objMap1.get(i).worldX + rect.x;
                    int rectY = gp.objMap1.get(i).worldY + rect.y;

                    // Tạo một Rectangle tạm để kiểm tra va chạm
                    Rectangle tempEntitySolidArea = new Rectangle(entitySolidAreaX, entitySolidAreaY, entity.solidArea.width, entity.solidArea.height);
                    Rectangle tempRect = new Rectangle(rectX, rectY, rect.width, rect.height);

                    // Kiểm tra va chạm giữa solid area của entity và rect
                    if (tempEntitySolidArea.intersects(tempRect)) {
                        entity.collisionOn = true;
                        break;  // Thoát khỏi vòng lặp nếu đã có va chạm với một rect
                    }
                }
            }
        }
    }



    public Vector<Integer> checkObjectForObj(Entity entity) {

        Vector<Integer> result = new Vector<>();
    
        for (int i = 0; i < gp.obj.size(); i++) {
            if(gp.obj.get(i) != null && gp.obj.get(i) != entity  && gp.obj.get(i).collision) {
    
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
    
                // Get the object's solid area position
                gp.obj.get(i).solidArea.x = gp.obj.get(i).worldX + gp.obj.get(i).solidArea.x;
                gp.obj.get(i).solidArea.y = gp.obj.get(i).worldY + gp.obj.get(i).solidArea.y;
    
                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                    case "up-left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "up-right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down-left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                    case "down-right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                }
    
                if (entity.solidArea.intersects(gp.obj.get(i).solidArea)) {
                    if (gp.obj.get(i).collision) {
                        if(!entity.isTrigger && !gp.obj.get(i).isTrigger) entity.collisionOn = true;
                        else {
                            if(entity.isTrigger) entity.isTriggerOn = true;
                        }
                    }
                    result.add(i);
                }
    
                // Reset positions to default
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj.get(i).solidArea.x = gp.obj.get(i).solidAreaDefaultX;
                gp.obj.get(i).solidArea.y = gp.obj.get(i).solidAreaDefaultY;
            }
        }
    
        return result;
    }
    
}