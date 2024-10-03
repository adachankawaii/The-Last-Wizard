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

        int tileSize = gp.titleSize;
        int entityLeftCol = entityLeftX / tileSize;
        int entityRightCol = entityRightX / tileSize;
        int entityTopRow = entityTopY / tileSize;
        int entityBottomRow = entityBottomY / tileSize;
        int tileNum1, tileNum2;
        switch (entity.direction) {
            case "up":
                // Kiểm tra va chạm khi thực thể di chuyển lên
                entityTopRow = (entityTopY - entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileMng.mapTile[entityRightCol][entityTopRow];
                if (gp.tileMng.tile[tileNum1].collision || gp.tileMng.tile[tileNum2].collision) {
                    entity.collisionOn = true; // Nếu có va chạm với tile
                }
                break;

            case "down":
                // Kiểm tra va chạm khi thực thể di chuyển xuống
                entityBottomRow = (entityBottomY + entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileMng.mapTile[entityRightCol][entityBottomRow];
                if (gp.tileMng.tile[tileNum1].collision || gp.tileMng.tile[tileNum2].collision) {
                    entity.collisionOn = true; // Nếu có va chạm với tile
                }
                break;

            case "left":
                // Kiểm tra va chạm khi thực thể di chuyển sang trái
                entityLeftCol = (entityLeftX - entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileMng.mapTile[entityLeftCol][entityBottomRow];
                if (gp.tileMng.tile[tileNum1].collision || gp.tileMng.tile[tileNum2].collision) {
                    entity.collisionOn = true; // Nếu có va chạm với tile
                }
                break;

            case "right":
                // Kiểm tra va chạm khi thực thể di chuyển sang phải
                entityRightCol = (entityRightX + entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityRightCol][entityTopRow];
                tileNum2 = gp.tileMng.mapTile[entityRightCol][entityBottomRow];
                if (gp.tileMng.tile[tileNum1].collision || gp.tileMng.tile[tileNum2].collision) {
                    entity.collisionOn = true; // Nếu có va chạm với tile
                }
                break;
        }

    }
}
