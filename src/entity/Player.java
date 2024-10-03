package entity;

import main.CollisionCheck;
import main.KeyHandler;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
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
        rect.x = 6;
        rect.y = 6;
        rect.width = 32;
        rect.height = 32;
        setDefautValue();
        getPlayerImage();
    }

    public void setDefautValue() {
        worldX = gp.titleSize * 23;
        worldY = gp.titleSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        /*try{
            up1 = ImageIO.read(new File("res/player/boy_up_1.png"));
            up2 = ImageIO.read(new File("res/player/boy_up_2.png"));
            down1 = ImageIO.read(new File("res/player/boy_down_1.png"));
            down2 = ImageIO.read(new File("res/player/boy_down_2.png"));
            left1 = ImageIO.read(new File("res/player/boy_left_1.png"));
            left2 = ImageIO.read(new File("res/player/boy_left_2.png"));
            right1 = ImageIO.read(new File("res/player/boy_right_1.png"));
            right2 = ImageIO.read(new File("res/player/boy_right_2.png"));       
        }catch(Exception e) {
            e.printStackTrace();
        }*/
        importEachImage(new String[]{"/player/boy_up_1.png","/player/boy_up_2.png"}, true);
        importEachImage(new String[]{"/player/boy_down_1.png","/player/boy_down_2.png"}, true);
        importEachImage(new String[]{"/player/boy_left_1.png","/player/boy_left_2.png"}, true);
        importEachImage(new String[]{"/player/boy_right_1.png","/player/boy_right_2.png"}, true);
    }

    public void update() {
        if(keyHandler.up || keyHandler.down || keyHandler.left || keyHandler.right) {
            if(keyHandler.up) {
                direction = "up";
            }
            if(keyHandler.down) {
                direction = "down";
            }
            if(keyHandler.right) {
                direction = "right";
            }
            if(keyHandler.left) {
                direction = "left";
            }

            // Gọi hàm move() để thực hiện di chuyển nếu không có va chạm

            spriteCounter++;
            if(spriteCounter > 5) {
                spriteNum++;
                if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
                spriteCounter = 0;
            }

            collisionOn = false;
            gp.collision.checkTile(this);
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
                }
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
        }
        BufferedImage image = animations.get(aniCount).get(spriteNum);
        g2d.drawImage(image, screenX, screenY, gp.titleSize, gp.titleSize, null);
    }
}
