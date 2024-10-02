package main;

import entity.Entity;
import tile.*;
public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        // Lấy các vị trí trên bản đồ
        int entityLeftX = entity.worldX + entity.rect.x;
        int entityRightX = entity.worldX + entity.rect.x + entity.rect.width;
        int entityTopY = entity.worldY + entity.rect.y;
        int entityBottomY = entity.worldY + entity.rect.y + entity.rect.height;


    }
}
