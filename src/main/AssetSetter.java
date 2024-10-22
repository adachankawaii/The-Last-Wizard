package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entity.Entity;
import entity.Items.CommonItem;
import entity.Items.HPBottle;
import entity.Items.ThrowingBottle;
import entity.enemy.Slime;
import entity.npc.NPC;
import entity.npc.ShopKeeper;

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
    }
    
    // TẠO RA CÁC OBJ TƯƠNG ỨNG
    public Entity createObject(String objectType) {
        switch (objectType) {
            case "Slime":
                return new Slime(gp);
            case "NPC":
                return new NPC(gp);
            case "ThrowingBottle":
                return new ThrowingBottle();
            case "HPBottle":
                return new HPBottle();
            case "Key":
                return new CommonItem("Key","/bullet/HP potion.png", gp);
            case "ShopKeeper":
                return new ShopKeeper(gp);
            default:
                System.out.println("Unknown object type: " + objectType);
                return null;
        }
    }
}
