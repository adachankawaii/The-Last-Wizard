package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;

    // Khai báo tile và mảng map chứa các tile
    public Tile[] tile;
    public int[][] mapTile;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[100]; // Mảng lưu trữ các tile cần dùng
        mapTile = new int[gp.maxWorldCol][gp.maxWorldRow]; // Tạo mảng map
        getTileImage(); // Lấy tile từ res
        loadMap("res/maps/world01.txt"); // Match với ma trận ở file txt
        //loadMap("res/maps/world02.txt"); // Match với ma trận ở file txt
    }

    // LẤY MẪU TILE
    public void getTileImage() {
        try { // Lấy các tile
         // MAP 1
            tile[1] = new Tile();
             tile[1].image = ImageIO.read(new File("res/tiles/map1/1.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("res/tiles/map1/2.png"));

             tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("res/tiles/map1/3.png"));

           tile[4] = new Tile();
            tile[4].image = ImageIO.read(new File("res/tiles/map1/4.png"));

            tile[5] = new Tile();
             tile[5].image = ImageIO.read(new File("res/tiles/map1/5.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(new File("res/tiles/map1/6.png"));

             tile[7] = new Tile();
             tile[7].image = ImageIO.read(new File("res/tiles/map1/7.png"));

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(new File("res/tiles/map1/8.png"));

        /* MAP 2
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("res/tiles/map2/1.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("res/tiles/map2/2.png"));        
            
        */
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOAD MAP
    public void loadMap(String mapPath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(mapPath));
            int col = 0, row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while(col < gp.maxWorldCol) {
                    String[] number = line.split(" ");
                    int num = Integer.parseInt(number[col]);
                    mapTile[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // VẼ MAP
    public void draw(Graphics2D gd) {

        int worldRow = 0, worldCol = 0;
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int num = mapTile[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize; 
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
            // Chỉ render phần trong khung camera
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
            && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
            && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                gd.drawImage(tile[num].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    // TRẢ VỀ TILE THEO MA TRẬN

}