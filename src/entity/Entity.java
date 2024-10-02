package entity;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;

public class Entity {
    public int worldX, worldY, screenX, screenY, speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteNum = 0;
    public int spriteCounter = 0;
    
    public Rectangle rect;
    public boolean collisionOn = false;
    BufferedImage source;
    BufferedImage img;
    public Vector<Vector<BufferedImage>> animations = new Vector<>();
    public int aniCount = 0;
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

}
