package collision;

import entity.Entity;
import main.GamePanel;

import java.util.Vector;

public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        // Lấy các vị trí trên bản đồ
        int entityLeftX = entity.worldX + entity.solidArea.x;
        int entityRightX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.worldY + entity.solidArea.y;
        int entityBottomY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int tileSize = gp.tileSize;
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
            case "up-left":
                // Kiểm tra va chạm khi di chuyển lên và sang trái
                entityTopRow = (entityTopY - entity.speed) / tileSize;
                entityLeftCol = (entityLeftX - entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityTopRow];  // Gạch ở góc trên-trái
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;
            
            case "up-right":
                // Kiểm tra va chạm khi di chuyển lên và sang phải
                entityTopRow = (entityTopY - entity.speed) / tileSize;
                entityRightCol = (entityRightX + entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityRightCol][entityTopRow];  // Gạch ở góc trên-phải
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;
            
            case "down-left":
                // Kiểm tra va chạm khi di chuyển xuống và sang trái
                entityBottomRow = (entityBottomY + entity.speed) / tileSize;
                entityLeftCol = (entityLeftX - entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityBottomRow];  // Gạch ở góc dưới-trái
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;
            
            case "down-right":
                // Kiểm tra va chạm khi di chuyển xuống và sang phải
                entityBottomRow = (entityBottomY + entity.speed) / tileSize;
                entityRightCol = (entityRightX + entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityRightCol][entityBottomRow];  // Gạch ở góc dưới-phải
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;    
        }
    }

    public void checkTileForObj(Entity entity) {
        // Lấy các vị trí trên bản đồ
        int entityLeftX = entity.worldX + entity.solidArea.x;
        int entityRightX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.worldY + entity.solidArea.y;
        int entityBottomY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int tileSize = gp.tileSize;
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
            case "up-left":
                // Kiểm tra va chạm khi di chuyển lên và sang trái
                entityTopRow = (entityTopY - entity.speed) / tileSize;
                entityLeftCol = (entityLeftX - entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityTopRow];  // Gạch ở góc trên-trái
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;

            case "up-right":
                // Kiểm tra va chạm khi di chuyển lên và sang phải
                entityTopRow = (entityTopY - entity.speed) / tileSize;
                entityRightCol = (entityRightX + entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityRightCol][entityTopRow];  // Gạch ở góc trên-phải
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;

            case "down-left":
                // Kiểm tra va chạm khi di chuyển xuống và sang trái
                entityBottomRow = (entityBottomY + entity.speed) / tileSize;
                entityLeftCol = (entityLeftX - entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityLeftCol][entityBottomRow];  // Gạch ở góc dưới-trái
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;

            case "down-right":
                // Kiểm tra va chạm khi di chuyển xuống và sang phải
                entityBottomRow = (entityBottomY + entity.speed) / tileSize;
                entityRightCol = (entityRightX + entity.speed) / tileSize;
                tileNum1 = gp.tileMng.mapTile[entityRightCol][entityBottomRow];  // Gạch ở góc dưới-phải
                if (gp.tileMng.tile[tileNum1].collision) {
                    entity.collisionOn = true; // Nếu có va chạm
                }
                break;
        }
    }
    
    public int checkObject(Entity entity, boolean player) {

        int index = 999;
    
        for (int i = 0; i < gp.obj.size(); i++) {
            if(gp.obj.get(i) != null) {
    
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
    
    
    public Vector<Integer> checkObjectForObj(Entity entity) {

        Vector<Integer> result = new Vector<>();
    
        for (int i = 0; i < gp.obj.size(); i++) {
            if(gp.obj.get(i) != null && gp.obj.get(i) != entity) {
    
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