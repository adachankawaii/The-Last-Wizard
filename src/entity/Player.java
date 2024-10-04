package entity;

import main.KeyHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import main.GamePanel;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyHandler;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;

        screenX = gp.screenWidth / 2 - gp.titleSize/2;
        screenY = gp.screenHeight / 2 - gp.titleSize/2;

        rect = new Rectangle();
        rect.x = 8;
        rect.y = 16;
        solidAreaDefaultX = rect.x;
        solidAreaDefaultY = rect.y;
        rect.width = 32;
        rect.height = 32;
        setDefautValue();
        getPlayerImage();
    }

    public void setDefautValue() {
        worldX = gp.titleSize * 23;
        worldY = gp.titleSize * 21;
        speed = 5;
        direction = "down";
    }

    public void getPlayerImage() {
        importEachImage(new String[]{"/player/boy_up_1.png","/player/boy_up_2.png"}, true);
        importEachImage(new String[]{"/player/boy_down_1.png","/player/boy_down_2.png"}, true);
        importEachImage(new String[]{"/player/boy_left_1.png","/player/boy_left_2.png"}, true);
        importEachImage(new String[]{"/player/boy_right_1.png","/player/boy_right_2.png"}, true);
    }

    public void update() {
        if(keyHandler.up || keyHandler.down || keyHandler.left || keyHandler.right) {
            if (keyHandler.up && keyHandler.left) {
                direction = "up-left";
            } else if (keyHandler.up && keyHandler.right) {
                direction = "up-right";
            } else if (keyHandler.down && keyHandler.left) {
                direction = "down-left";
            } else if (keyHandler.down && keyHandler.right) {
                direction = "down-right";
            } else if (keyHandler.up) {
                direction = "up";
            } else if (keyHandler.down) {
                direction = "down";
            } else if (keyHandler.left) {
                direction = "left";
            } else if (keyHandler.right) {
                direction = "right";
            }

            spriteCounter++;
            if(spriteCounter > 5) {
                spriteNum++;
                if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
                spriteCounter = 0;
            }

            collisionOn = false;
            gp.collision.checkTile(this);
            int objIndex = gp.collision.checkObject(this, true);
            pickUpObj(objIndex);

            if(!collisionOn){
                switch (direction){
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
                        worldY -= (speed * 0.75); // 0.75 là căn bậc hai của 2 chia cho 2, để giữ tốc độ chéo nhất quán
                        worldX -= (speed * 0.75);
                        break;
                    case "up-right":
                        worldY -= speed * 0.75;
                        worldX += speed * 0.75;
                        break;
                    case "down-left":
                        worldY += speed * 0.75;
                        worldX -= speed * 0.75;
                        break;
                    case "down-right":
                        worldY += speed * 0.75;
                        worldX += speed * 0.75;
                        break;
                }
            }
        }
    }

    int hasKey = 0;
    public void pickUpObj(int i) {
        if(i != 999) {
            String objName = gp.obj.get(i).name;
            switch (objName) {
                case "Slime":
                    hasKey++;
                    gp.obj.remove(gp.obj.get(i));
                    System.out.println(objName + " " + hasKey);
                    break;
            
                default:
                    break;
            }
        }
    }
    public void draw(Graphics2D g2d) {

        switch(direction) {
            case "up":
                aniCount = 0;
                break;
            case "down":
                aniCount = 1;
                break;
            case "left":
                aniCount = 2;
                break;
            case "right":
                aniCount = 3;
                break;  
            case "up-left":
                aniCount = 0; // Có thể dùng hoạt ảnh cho di chuyển lên
                break;
            case "up-right":
                aniCount = 0; // Có thể dùng hoạt ảnh cho di chuyển lên
                break;
            case "down-left":
                aniCount = 1; // Có thể dùng hoạt ảnh cho di chuyển xuống
                break;
            case "down-right":
                aniCount = 1; // Có thể dùng hoạt ảnh cho di chuyển xuống
                break;         
        }
        BufferedImage image = animations.get(aniCount).get(spriteNum);
        g2d.drawImage(image, screenX, screenY, gp.titleSize, gp.titleSize, null);
        g2d.setColor(Color.red);
        g2d.drawRect(screenX + rect.x, screenY + rect.y, rect.width, rect.height);
    }
}