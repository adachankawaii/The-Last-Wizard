package entity;

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
        rect.x = 8;
        rect.y = 16;
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
        try{
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
        }
    }

    public void update() {
        if(keyHandler.up || keyHandler.down || keyHandler.left || keyHandler.right) {
            if(keyHandler.up) {
                direction = "up";
                worldY -= speed;
            }
            if(keyHandler.down) {
                direction = "down";
                worldY += speed;
            }
            if(keyHandler.right) {
                direction = "right";
                worldX += speed;           
            }
            if(keyHandler.left) {
                direction = "left";
                worldX -= speed;
            }
            
            spriteCounter++;
            if(spriteCounter > 10) {
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }

            collisionOn = false;
            gp.collision.checkTile(this);
            
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        switch(direction) {
            case "up":
                if(spriteNum == 1) image = up1;
                if (spriteNum == 2) image = up2;
                break;
            case "down":
                if(spriteNum == 1) image = down1;
                if (spriteNum == 2) image = down2;
                break;
            case "left":
                if(spriteNum == 1) image = left1;
                if (spriteNum == 2) image = left2;
                break;
            case "right":
                if(spriteNum == 1) image = right1;
                if (spriteNum == 2) image = right2;
                break;           
        }
        g2d.drawImage(image, screenX, screenY, gp.titleSize, gp.titleSize, null);
    }
}
