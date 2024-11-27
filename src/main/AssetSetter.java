package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entity.Entity;
import entity.Items.*;
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
        else if(gp.map == 2){
            int []tmp = {3,1,4,2};
            for(int i = 0;i<4;i++){
                Pole p = new Pole(gp,"Pole",tmp[i],63 + 5*i, 67);
                gp.obj.add(p);
            }
            int[][] horizontalWalls = {{23,41},{67,42},{67,55}};
            for (int[] pos : horizontalWalls) {
                CombatWall c = new CombatWall(gp, 10, 1); // Width = 8, Height = 1
                c.worldX = pos[0] * gp.tileSize;
                c.worldY = pos[1] * gp.tileSize;
                gp.obj.add(c); // Thêm tường vào danh sách đối tượng
            }

            // Tạo tường có kích thước width = 1, height = 8 ở vị trí (70,80)
            int[][] verticalWalls = {{43,24},{55,24},{54,47}};
            for (int[] pos : verticalWalls) {
                CombatWall c = new CombatWall(gp, 1, 10); // Width = 1, Height = 8
                c.worldX = pos[0] * gp.tileSize;
                c.worldY = pos[1] * gp.tileSize;
                gp.obj.add(c); // Thêm tường vào danh sách đối tượng
            }
            int startX = 63 * gp.tileSize; // Góc trái trên
            int startY = 18 * gp.tileSize; // Góc trái trên
            int edgeLength = 17 * gp.tileSize; // Chiều dài cạnh hình vuông
            int interval = edgeLength / 3; // Khoảng cách giữa các hình
            int count = 1;

// Đặt hình ở 4 góc
            int[][] cornerPositions = {
                    {startX, startY}, // Góc trái trên
                    {startX + edgeLength, startY}, // Góc phải trên
                    {startX, startY + edgeLength}, // Góc trái dưới
                    {startX + edgeLength, startY + edgeLength} // Góc phải dưới
            };

// Đặt hình ở 4 góc
            for (int[] pos : cornerPositions) {
                int x = pos[0];
                int y = pos[1];
                Placer p = new Placer(gp, "Placer", count, x, y);
                gp.obj.add(p);
                count++;
            }

// Đặt hình ở các cạnh (trừ góc)
            for (int i = 1; i < 3; i++) {
                // Cạnh trên (trái sang phải, trừ góc)
                int xTop = startX + i * interval;
                int yTop = startY;
                Placer pTop = new Placer(gp, "Placer", count, xTop, yTop);
                gp.obj.add(pTop);
                count++;

                // Cạnh dưới (trái sang phải, trừ góc)
                int xBottom = startX + i * interval;
                int yBottom = startY + edgeLength;
                Placer pBottom = new Placer(gp, "Placer", count, xBottom, yBottom);
                gp.obj.add(pBottom);
                count++;

                // Cạnh trái (trên xuống dưới, trừ góc)
                int xLeft = startX;
                int yLeft = startY + i * interval;
                Placer pLeft = new Placer(gp, "Placer", count, xLeft, yLeft);
                gp.obj.add(pLeft);
                count++;

                // Cạnh phải (trên xuống dưới, trừ góc)
                int xRight = startX + edgeLength;
                int yRight = startY + i * interval;
                Placer pRight = new Placer(gp, "Placer", count, xRight, yRight);
                gp.obj.add(pRight);
                count++;
            }
            Placer pRight = new Placer(gp, "Placer", count, 87*gp.tileSize, 28*gp.tileSize);
            gp.obj.add(pRight);

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
            case "Box":
                return new CommonItem("Box", gp);
            case "FinalBoss":
                return new FinalBoss(gp);
            default:
                System.out.println("Unknown object type: " + objectType);
                return null;
        }
    }
}
