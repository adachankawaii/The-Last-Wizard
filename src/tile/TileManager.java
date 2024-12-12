package tile;

import java.awt.Graphics2D;
import java.io.File;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;

    // Khai báo tile và mảng map chứa các tile
    public Tile[] tile;
    // public int[][] mapTile;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10]; // Mảng lưu trữ các tile cần dùng
        // mapTile = new int[gp.maxWorldCol][gp.maxWorldRow]; // Tạo mảng map
        getTileImage(); // Lấy tile từ res
        // loadMap("res/maps/world0" + gp.map +".txt"); // Match với ma trận ở file txt
    }

    // LẤY MẪU TILE
    public void getTileImage() {
        try { // Lấy các tile
            if(gp.map == 1) {
                tile[1] = new Tile();
                tile[1].image = ImageIO.read(new File("res/tiles/map1/1.png"));
            } else if (gp.map == 2) {
                tile[1] = new Tile();
                tile[1].image = ImageIO.read(new File("res/tiles/map2/1.png"));
            } else if (gp.map == 3) {
                tile[2] = new Tile();
                tile[2].image = ImageIO.read(new File("res/tiles/map3/2.png"));
            }else if (gp.map == 4) {
                tile[2] = new Tile();
                tile[2].image = ImageIO.read(new File("res/tiles/map4/2.png"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // // LOAD MAP
    // public void loadMap(String mapPath) {
    //     try {
    //         BufferedReader br = new BufferedReader(new FileReader(mapPath));
    //         int col = 0, row = 0;

    //         while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
    //             String line = br.readLine();
    //             while(col < gp.maxWorldCol) {
    //                 String[] number = line.split(" ");
    //                 int num = Integer.parseInt(number[col]);
    //                 mapTile[col][row] = num;
    //                 col++;
    //             }
    //             if(col == gp.maxWorldCol) {
    //                 col = 0;
    //                 row++;
    //             }
    //         }
    //         br.close();
    //     } catch(Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // VẼ MAP
    public void draw(Graphics2D gd) {

        // Vùng giới hạn tính từ player để render map
        int startCol = gp.player.worldX / gp.tileSize - 15;
        int endCol = gp.player.worldX / gp.tileSize + gp.player.screenX / gp.tileSize + 15;
        int startRow = gp.player.worldY / gp.tileSize - 20;
        int endRow = gp.player.worldY / gp.tileSize + gp.player.screenY / gp.tileSize + 20;

        for (int worldCol = startCol; worldCol <= endCol; worldCol++) {
            for (int worldRow = startRow; worldRow <= endRow; worldRow++) {

                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // Chỉ render phần trong khung camera
                if (worldX + 1 * gp.tileSize > gp.player.worldX - gp.player.screenX
                        && worldX - 1 * gp.tileSize < gp.player.worldX + gp.player.screenX
                        && worldY + 1 * gp.tileSize > gp.player.worldY - gp.player.screenY
                        && worldY - 1 * gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    if (worldCol < 0 || worldRow < 0 || worldCol >= (gp.map == 4 ? 60 : gp.maxWorldCol) || worldRow >= (gp.map == 4 ? 60 : gp.maxWorldRow)) {
                        int i = 1;
                        if(gp.map == 4 || gp.map == 3) i = 2;
                        gd.drawImage(tile[i].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
        }
    }

}