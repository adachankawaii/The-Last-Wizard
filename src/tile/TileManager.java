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

        tile = new Tile[200]; // Mảng lưu trữ các tile cần dùng
        mapTile = new int[gp.maxWorldCol][gp.maxWorldRow]; // Tạo mảng map
        getTileImage(); // Lấy tile từ res
        loadMap("res/maps/world0" + gp.map +".txt"); // Match với ma trận ở file txt
    }

    // LẤY MẪU TILE
    public void getTileImage() {
        try { // Lấy các tile
            if(gp.map == 1) {
                for (int i = 1; i <= 8; i++) {
                    tile[i] = new Tile();
                    tile[i].image = ImageIO.read(new File("res/tiles/map1/" + i + ".png"));
                }
            } else if (gp.map == 2) {
                for (int i = 1; i <= 33; i++) {
                    tile[i] = new Tile();
                    tile[i].image = ImageIO.read(new File("res/tiles/map2/" + i + ".png"));
                }
            } else if (gp.map == 3) {
                for (int i = 1; i <= 2; i++) {
                    tile[i] = new Tile();
                    tile[i].image = ImageIO.read(new File("res/tiles/map3/" + i + ".png"));
                }
            }else if (gp.map == 4) {
                for (int i = 1; i <= 34; i++) {
                    tile[i] = new Tile();
                    tile[i].image = ImageIO.read(new File("res/tiles/map4/" + i + ".png"));
                }
            }
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
                if (worldX + 1*gp.tileSize > gp.player.worldX - gp.player.screenX
                        && worldX - 1*gp.tileSize < gp.player.worldX + gp.player.screenX
                        && worldY + 1*gp.tileSize > gp.player.worldY - gp.player.screenY
                        && worldY - 1*gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    if (worldCol < 0 || worldRow < 0 || worldCol >= gp.maxWorldCol || worldRow >= gp.maxWorldRow) {
                        gd.drawImage(tile[1].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    } else {
                        int num = mapTile[worldCol][worldRow];
                        gd.drawImage(tile[num].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
        }
    }


    // TRẢ VỀ TILE THEO MA TRẬN

}