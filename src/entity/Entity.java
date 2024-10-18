package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Entity {
    public boolean isItem = false;
    // BIẾN LƯU
    public int worldX, worldY;
    public int screenX, screenY;
    public float alpha = 1;
    public int speed;
    public String direction;
    public String objName;

    public Rectangle solidArea; // Rect
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;

    public GamePanel gp;
    public KeyHandler keyH;

    // Dành cho collision
    public boolean collisionOn;
    public boolean collision;
    public boolean isTrigger;
    public boolean isTriggerOn = false;

    // Dành cho importImage
    protected int aniCount = 0;
    protected int spriteCounter = 0;
    protected int spriteNum = 0;
    BufferedImage source;
    BufferedImage img;
    public Vector<Vector<BufferedImage>> animations = new Vector<>();

    // CẮT VÀ IMPORT ẢNH
    public void importAndSlice(String path, int count, int x, int y){
        Vector<BufferedImage> a = new Vector<>();
        try(InputStream is = getClass().getResourceAsStream(path)){
            source = ImageIO.read(is);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0;i<count;i++){
            BufferedImage tmp = source.getSubimage(source.getWidth()/count * i, y, source.getWidth()/count, source.getHeight());
            a.add(tmp);
        }
        animations.add(a);
    }

    // IMPORT NHIỀU ẢNH 1 LOẠI CHUYỂN ĐỘNG
    public void importEachImage(String[] path, boolean newAnimation){
        if(newAnimation){
            Vector<BufferedImage> a = new Vector<>();
            for(int i = 0;i<path.length;i++){
                try(InputStream is = getClass().getResourceAsStream(path[i])){
                    img = ImageIO.read(is);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                a.add(img);
            }
            animations.add(a);
        }
        else{
            for(int i = 0;i<path.length;i++){
                try(InputStream is = getClass().getResourceAsStream(path[i])){
                    img = ImageIO.read(is);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                animations.get(animations.size() - 1).add(img);
            }
        }
    }

    // IMPORT 1 ẢNH THÔI
    public void importAnImage(String path, boolean newAnimation) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            img = ImageIO.read(is);
            if (newAnimation) {
                Vector<BufferedImage> a = new Vector<>();
                a.add(img);
                animations.add(a);
            } else {
                animations.get(animations.size() - 1).add(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VẼ KHUNG RECT CỦA OBJECT
    public void rectDraw(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

    // TẠO KHUNG RECT CỦA OBJECT
    public void rectGet(int a, int b, int w, int h) {
        solidArea = new Rectangle();
        solidArea.x = a;
        solidArea.y = b;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = w;
        solidArea.height = h;
    }    

    // SET GIÁ TRỊ MẶC ĐỊNH
    public void setDefaultValue(int x, int y, int speed, String direction) {
        this.worldX = x;
        this.worldY = y;
        this.speed = speed;
        this.direction = direction;
    }

    // PHÁT HIỆN CHUYỂN ĐỘNG VÀ VẼ TƯƠNG ỨNG
    public void detectMoveAndDraw(Graphics2D g2) {
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
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        BufferedImage image = animations.get(aniCount).get(spriteNum); // Import ảnh từ Animations
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
    
    public void drawObjImage(Graphics2D g2, GamePanel gp) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;
    
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
        && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
        && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
        && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void draw(Graphics2D g2d, GamePanel gamePanel) {
    }
    public void update() {
    }
    public void effect(){
    }
    public void onTriggerEnter(Entity entity){
    }
}
