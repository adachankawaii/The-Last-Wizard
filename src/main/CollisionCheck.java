package main;

import entity.Entity;

public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLX = entity.worldX + entity.rect.x;
        int entityRX = entity.worldX + entity.rect.x + entity.rect.width;
        int entityUY = entity.worldY + entity.rect.y;
        int entityDY = entity.worldY + entity.rect.y + entity.rect.height;
    }
}
