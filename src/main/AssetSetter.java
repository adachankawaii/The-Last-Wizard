package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entity.Entity;
import entity.Items.CommonItem;
import entity.Items.HPBottle;
import entity.Items.ThrowingBottle;
import entity.enemy.*;
import entity.npc.*;

public class AssetSetter {
    GamePanel gp;

    // CONSTRUCTOR
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    // ĐỌC TỪ FILE TXT TÊN OBJ, SỐ LƯỢNG, VỊ TRÍ TỌA ĐỘ CỦA CÁC OBJ
    public void setObject(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
    
            // Đọc từng dòng
            while ((line = br.readLine()) != null) {
                // Tách dòng đầu tiên thành các phần: tên đối tượng và số lượng
                String[] parts = line.split(" ");
                String objectType = parts[0];  // Lấy tên đối tượng (Slime, Tree, Rock,...)
                int quantity = Integer.parseInt(parts[1]);  // Lấy số lượng đối tượng
    
                // Đọc dòng tiếp theo để lấy các tọa độ
                line = br.readLine();
                if (line != null) {
                    String[] coordsList = line.split(" ");  // Mỗi cặp tọa độ cách nhau bởi dấu cách
    
                    // Xử lý các tọa độ theo số lượng đã chỉ định
                    for (int i = 0; i < quantity; i++) {
                        String[] coords = coordsList[i].split(",");
                        int x = Integer.parseInt(coords[0].trim());
                        int y = Integer.parseInt(coords[1].trim());
    
                        // Tạo đối tượng dựa trên loại objectType và đặt vị trí
                        Entity newObj = createObject(objectType);
                        if (newObj != null) {
                            newObj.worldX = x * gp.tileSize;
                            newObj.worldY = y * gp.tileSize;
                            newObj.gp = gp;
                            gp.obj.add(newObj);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gp.map == 1) {
            Board b = new Board(gp, "Wanted", "/UI/Stuffs/wanted.png", "/UI/Stuffs/wanted.png");
            b.worldX = gp.tileSize* 47;
            b.worldY = gp.tileSize*49;
            b.layer = 10;
            gp.obj.add(b);
            // Tạo tường có kích thước width = 8, height = 1 ở vị trí (60,70) và (60,80)
            int[][] horizontalWalls = {{60, 70}, {60, 80}};
            for (int[] pos : horizontalWalls) {
                CombatWall c = new CombatWall(gp, 10, 1); // Width = 8, Height = 1
                c.worldX = pos[0] * gp.tileSize;
                c.worldY = pos[1] * gp.tileSize;
                gp.obj.add(c); // Thêm tường vào danh sách đối tượng
            }

            // Tạo tường có kích thước width = 1, height = 8 ở vị trí (70,80)
            int[][] verticalWalls = {{40,70},{70, 70},{39,42},{39,11}};
            for (int[] pos : verticalWalls) {
                CombatWall c = new CombatWall(gp, 1, 10); // Width = 1, Height = 8
                c.worldX = pos[0] * gp.tileSize;
                c.worldY = pos[1] * gp.tileSize;
                gp.obj.add(c); // Thêm tường vào danh sách đối tượng
            }
        }

    }
    
    // TẠO RA CÁC OBJ TƯƠNG ỨNG
    public Entity createObject(String objectType) {
        switch (objectType) {
            case "Slime":
                return new Slime(gp);
            case "NPC":
                return new NPC(gp);
            case "Portal":
                return new Portal(gp);
            case "ThrowingBottle":
                return new ThrowingBottle(gp);
            case "HPBottle":
                return new HPBottle("HPBottle",gp);
            case "InvisiblePotion":
                return new HPBottle("InvisiblePotion",gp);
            case "Key":
                return new CommonItem("Key", gp);
            case "ShopKeeper":
                return new ShopKeeper(gp);
            case "Soldier":
                return new Soldier(gp);
            case "Knight":
                return new Knight(gp);
            case "MageSeeker":
                return new Mage(gp);
            case "Golem":
                return new Golem(gp);
            case "Executioner":
                return new Executioner(gp);
            case "Tower":
                return new Tower(gp);
            case "Ghost":
                return new Ghost(gp);
            case "GuildMaster":
                return new GuildMaster(gp);
            case "FinalBoss":
                return new FinalBoss(gp);
            default:
                System.out.println("Unknown object type: " + objectType);
                return null;
        }
    }
}
