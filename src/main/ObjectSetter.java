package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entity.Items.ObjectMap1;

public class ObjectSetter {
    GamePanel gp;

    // CONSTRUCTOR
    public ObjectSetter(GamePanel gp) {
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
                String objName = parts[0];  // Lấy tên đối tượng (Slime, Tree, Rock,...)
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
    
                        // Tạo đường dẫn tới file tọa độ rect cho obj này
                        String rectFilePath = "res/collisionmap/" + objName + ".txt";

                        // Khởi tạo Map1Item với các thông tin và đường dẫn file tọa độ
                        ObjectMap1 newObj = new ObjectMap1(objName, "/map1/" + objName + ".png", gp);
                        // Đọc các rect từ file và thêm vào item
                        newObj.loadRectsFromFile(rectFilePath);

                        if (newObj != null) {
                            newObj.worldX = x * gp.tileSize;
                            newObj.worldY = y * gp.tileSize;
                            newObj.gp = gp;
                            gp.objMap1.add(newObj);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
