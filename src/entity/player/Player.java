package entity.player;

import main.KeyHandler;

import java.awt.*;
import java.util.ArrayList;


import entity.Entity;
import main.GamePanel;

public class Player extends Entity{
    public int HP = 10;
    public int Energy = 200;
    int timer = 0;
    public ArrayList<Entity> items = new ArrayList<>(8);
    public ArrayList<Integer> itemsCount = new ArrayList<>(8);
    public int pointer = 0;
    public boolean combat = true;
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // Khởi tạo vị trí nhân vật
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

        // Khởi tạo giá trị ban đầu, gọi các hàm khởi tạo trong Entity
        setDefaultValue(
        gp.tileSize * 23,
        gp.tileSize * 21,
        5,
        "down");

        rectGet(8, 16, 32, 32);
        getPlayerImage(); // Lấy hình ảnh Player

    }

    public void getPlayerImage() {

        // Lấy từng chuyển động của nhân vật. Mỗi chuyển động lên/xuống/trái/phải tương ứng với 4 ảnh
        // Quy ước ở đây: [0]: lên - [1]: xuống - [2]: trái - [3]: phải
        importEachImage(new String[]{"/player/move/up_0.png","/player/move/up_1.png","/player/move/up_2.png","/player/move/up_3.png"}, true);
        importEachImage(new String[]{"/player/move/down_0.png","/player/move/down_1.png","/player/move/down_2.png","/player/move/down_3.png"}, true);
        importEachImage(new String[]{"/player/move/left_0.png","/player/move/left_1.png","/player/move/left_2.png","/player/move/left_3.png"}, true);
        importEachImage(new String[]{"/player/move/right_0.png","/player/move/right_1.png","/player/move/right_2.png","/player/move/right_3.png"}, true);
    }
    public void drawItems(Graphics2D g2) {
        int itemX = 10; // Xác định vị trí X (cạnh trái màn hình)
        int itemY = 50; // Xác định vị trí Y ban đầu
        int itemSize = 32; // Kích thước của mỗi item (hoặc bạn có thể lấy kích thước thực của hình ảnh)

        // Lặp qua các item trong danh sách
        for (int i = 0; i < items.size(); i++) {
            Entity item = items.get(i); // Lấy item hiện tại

            // Nếu item này là item mà pointer đang chỉ đến, vẽ khung đỏ
            if(i == pointer) {
                g2.setColor(Color.RED);
                g2.drawRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4); // Vẽ khung viền màu đỏ lớn hơn hình item một chút
            }

            // Vẽ hình ảnh của item (giả sử item có phương thức drawItem để vẽ hình ảnh của nó)
            g2.drawImage(item.animations.get(0).get(0), itemX, itemY, itemSize, itemSize, null);

            // Vẽ số lượng item
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.black);
            g2.drawString("x" + itemsCount.get(i), itemX + itemSize + 5, itemY + itemSize / 2);

            // Di chuyển Y xuống cho item tiếp theo
            itemY += itemSize + 10; // Thêm khoảng cách giữa các item
        }
    }


    public void update() {
        if(combat) {
            // Xử lí di chuyển khi bấm phím
            int objIndex = gp.cCheck.checkObject(this, true);
            if (isTriggerOn && (gp.obj.get(objIndex).objName.equals("Slime") || gp.obj.get(objIndex).objName.equals("enemyBullet")) && timer <= 0) {
                alpha = 1;
                keyH.EPressed = false;
                timer = 20;
                HP--;
            }
            if (HP >= 10) HP = 10;
            if (Energy >= 200) Energy = 200;
            else if (Energy <= 0) Energy = 0;
            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
                // Di chuyển chéo
                if (keyH.upPressed && keyH.leftPressed) {
                    direction = "up-left";
                } else if (keyH.upPressed && keyH.rightPressed) {
                    direction = "up-right";
                } else if (keyH.downPressed && keyH.leftPressed) {
                    direction = "down-left";
                } else if (keyH.downPressed && keyH.rightPressed) {
                    direction = "down-right";
                } else { // Di chuyển đơn
                    if (keyH.upPressed) {
                        direction = "up";
                    } else if (keyH.downPressed) {
                        direction = "down";
                    } else if (keyH.leftPressed) {
                        direction = "left";
                    } else if (keyH.rightPressed) {
                        direction = "right";
                    }
                }
                gp.cCheck.checkTile(this);
                if (!collisionOn) {
                    switch (direction) {
                        case "up":
                            worldY -= speed;
                            break;
                        case "down":
                            worldY += speed;
                            break;
                        case "left":
                            worldX -= speed;
                            break;
                        case "right":
                            worldX += speed;
                            break;
                        case "up-left":
                            worldY -= speed / Math.sqrt(2);
                            worldX -= speed / Math.sqrt(2); // Chia căn 2 để giảm tốc về bình thường
                            break;
                        case "up-right":
                            worldY -= speed / Math.sqrt(2);
                            worldX += speed / Math.sqrt(2);
                            break;
                        case "down-left":
                            worldY += speed / Math.sqrt(2);
                            worldX -= speed / Math.sqrt(2);
                            break;
                        case "down-right":
                            worldY += speed / Math.sqrt(2);
                            worldX += speed / Math.sqrt(2);
                            break;
                    }
                }

                // Đếm số lượng sprite đã chạy
                spriteCounter++; // Đếm số lần cập nhật
                if (spriteCounter > 8) { // Chuyển sang khung hình tiếp theo
                    spriteNum++; // Tăng lên để chuyển
                    if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0; // Lặp lại hoạt hình
                    spriteCounter = 0; // Reset cho khung hình tiếp theo
                    Energy++;
                }
            } else spriteNum = 0; // Nếu không bấm gì thì nhân vật trở về trạng thái cơ bản
            if (keyH.EPressed) alpha = 0.5f;
            else alpha = 1;
            // COLLISION
            if (alpha < 0.9) {
                Energy--;
            }
            if (Energy <= 0) {
                alpha = 1;
                keyH.EPressed = false;
            }
            collisionOn = false;
            isTriggerOn = false;
            if (objIndex != 999) {
                pickUpObj(objIndex);
            }
            if (gp.keyH.i != -1 && !items.isEmpty() && (gp.keyH.i < items.size())) {
                // Chỉ xóa item tại vị trí gp.keyH.i, không ảnh hưởng đến các phần tử khác
                pointer = gp.keyH.i;
            }
            timer--;
        }
    }
    int hasKey = 0;

    public void pickUpObj(int i) {
        if(i != 999) {
            String objName = gp.obj.get(i).objName;
            if(objName != null){
                switch (objName) {
                    case "Slime":
                        hasKey++;
                        System.out.println(objName + " " + hasKey);
                        break;
                    case "NPC_1":
                        hasKey--;
                        gp.obj.remove(gp.obj.get(i));
                        System.out.println(objName + " " + hasKey);
                        break;
                    case "ThrowingBottle", "HPBottle":
                        if(items.size() < 8){
                            boolean flag = false;
                            for(int j = 0; j < items.size(); j++){
                                if(items.get(j).objName.equals(gp.obj.get(i).objName)){
                                    flag = true;
                                    // Sửa từ add thành set để cập nhật đúng số lượng item
                                    itemsCount.set(j, itemsCount.get(j) + 1);
                                    break; // Thêm break để dừng vòng lặp sau khi tìm thấy item
                                }
                            }
                            if(!flag) {
                                items.add(gp.obj.get(i));
                                itemsCount.add(1); // Thêm mới số lượng item là 1
                            }
                        }
                        gp.obj.remove(gp.obj.get(i));
                        break;
                    default:
                        break;
                }
            }

        }
    }
    public void draw(Graphics2D g2) {
        detectMoveAndDraw(g2); // Nhận diện chuyển động lên/xuống/trái/phải để vẽ hình tương ứng
        drawItems(g2); // Vẽ danh sách các item

        // Hiển thị tên của item đang được chọn (pointer chỉ đến)
        if (!items.isEmpty() && pointer < items.size()) {
            String itemName = items.get(pointer).objName;
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.setColor(Color.WHITE);

            // Tính toán vị trí giữa màn hình để hiển thị tên item
            int textX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(itemName) / 2;
            int textY = gp.screenHeight - 30; // Hiển thị gần đáy màn hình

            g2.drawString(itemName, textX, textY); // Vẽ tên item
        }

        rectDraw(g2); // Vẽ ô rect nhận diện collision
    }

}
