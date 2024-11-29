package entity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Entity{
    public boolean isItem = false;
    public boolean awake = false;
    public boolean on = false;
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
    public int layer = 0;
    public int imgWidth, imgHeight;

    public GamePanel gp;
    public KeyHandler keyH;
    public boolean done = false;
    // Dành cho collision
    public boolean collisionOn;
    public boolean collision;
    public boolean isTrigger;
    public boolean isTriggerOn = false;
    public boolean isEnemy = false;
    protected boolean isHurt = false;
    // Dành cho importImage
    protected int aniCount = 0;
    protected int spriteCounter = 0;
    protected int spriteNum = 0;
    public boolean isBoss = false;
    BufferedImage source;
    protected BufferedImage img;
    public Vector<Vector<BufferedImage>> animations = new Vector<>();
    public boolean flip = false;

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
    
    public void importAndSliceVertical(String path, int count, int x, int y) {
        Vector<BufferedImage> a = new Vector<>();
        try (InputStream is = getClass().getResourceAsStream(path)) {
            source = ImageIO.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < count; i++) {
            BufferedImage tmp = source.getSubimage(x, source.getHeight() / count * i, source.getWidth(), source.getHeight() / count);
            a.add(tmp);
        }
        animations.add(a);
    }


    // VẼ KHUNG RECT CỦA OBJECT
    public boolean isBullet = false;
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
    public BufferedImage makeSpriteWhite(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage whiteSprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgba = original.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xff;  // Lấy độ trong suốt

                // Chỉ thay đổi màu sắc nếu pixel không trong suốt
                if (alpha != 0) {
                    // Đặt pixel thành màu trắng nhưng giữ độ trong suốt ban đầu
                    whiteSprite.setRGB(x, y, (alpha << 24) | 0x00FFFFFF);
                } else {
                    // Giữ nguyên pixel trong suốt
                    whiteSprite.setRGB(x, y, rgba);
                }
            }
        }

        return whiteSprite;
    }
    public BufferedImage makeSpriteRed(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        // Tạo một hình ảnh mới để lưu sprite đã chuyển đổi
        BufferedImage redOutlineSprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgba = original.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xff;  // Lấy độ trong suốt của pixel

                // Giữ nguyên pixel ban đầu
                redOutlineSprite.setRGB(x, y, rgba);

                // Chỉ xử lý pixel không trong suốt
                if (alpha != 0) {
                    // Kiểm tra nếu pixel này nằm cạnh pixel trong suốt
                    boolean isEdge = false;

                    // Các tọa độ lân cận (trên, dưới, trái, phải)
                    int[][] neighbors = {
                            {x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}
                    };

                    for (int[] neighbor : neighbors) {
                        int nx = neighbor[0];
                        int ny = neighbor[1];

                        // Bỏ qua nếu ngoài phạm vi ảnh
                        if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                            continue;
                        }

                        int neighborRgba = original.getRGB(nx, ny);
                        int neighborAlpha = (neighborRgba >> 24) & 0xff;

                        // Nếu pixel lân cận trong suốt, đây là pixel viền
                        if (neighborAlpha == 0) {
                            isEdge = true;
                            break;
                        }
                    }

                    // Nếu pixel này là viền, đổi màu thành đỏ (giữ độ trong suốt)
                    if (isEdge) {
                        redOutlineSprite.setRGB(x, y, (alpha << 24) | 0x00FF0000);
                    }
                }
            }
        }

        return redOutlineSprite;
    }
    // PHÁT HIỆN CHUYỂN ĐỘNG VÀ VẼ TƯƠNG ỨNG
    public void detectMoveAndDraw(Graphics2D g2) {

        switch (direction) {
            case "up":
                break;
            case "down":
                break;
            case "left", "up-left", "down-left":
                flip = true;
                break;
            case "right", "up-right", "down-right":
                flip = false;
                break;
        }

        // Lấy hình ảnh từ animations
        BufferedImage image = spriteNum < animations.get(aniCount).size() ? animations.get(aniCount).get(spriteNum) : animations.get(aniCount).get(0);
        AffineTransform old = g2.getTransform(); // Lưu trạng thái ban đầu của Graphics2D
        Composite originalComposite = g2.getComposite();
        // Tính toán vị trí trung tâm màn hình
        int centerX = gp.screenWidth / 2;
        int centerY = gp.screenHeight / 2;

        // Dịch hệ tọa độ đến vị trí trung tâm màn hình
        g2.translate(centerX, centerY);

        // Lật hình ảnh theo trục Ox nếu cần
        if (flip) g2.scale(-1, 1);
        if (isHurt) { // Giả sử đây là trạng thái bị thương
            image = makeSpriteWhite(image); // Làm trắng hình ảnh nếu bị thương
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        // Vẽ hình ảnh đã lật, căn chỉnh để hình ảnh được hiển thị ở giữa
        g2.drawImage(image, -gp.tileSize, -gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, null);
        g2.setComposite(originalComposite);
        // Khôi phục lại trạng thái ban đầu của Graphics2D
        g2.setTransform(old);
    }

    public void drawObjImage(Graphics2D g2, GamePanel gp) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;
    
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
        && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
        && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
        && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            if(isItem) g2.drawImage(image, screenX, screenY, 32, 32, null);
            else g2.drawImage(image, screenX, screenY, 48, 48, null);
        }
    }
    

    public void draw(Graphics2D g2d, GamePanel gamePanel) {
    }
    public void drawUI(Graphics2D g2d, GamePanel gamePanel){
    }
    public void update() {
    }
    public void effect(){
    }
    public void onTriggerEnter(Entity entity){
    }
}
