package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int[][] mapTile;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTile = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("res/maps/world01.txt");
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("res/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("res/tiles/wall.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("res/tiles/water.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("res/tiles/earth.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new File("res/tiles/tree.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(new File("res/tiles/sand.png"));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void draw(Graphics2D gd) {

        int worldRow = 0, worldCol = 0;
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int num = mapTile[worldCol][worldRow];

            int worldX = worldCol * gp.titleSize;
            int worldY = worldRow * gp.titleSize; 
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
            if(worldX + gp.titleSize > gp.player.worldX - gp.player.screenX
            && worldX - gp.titleSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.titleSize > gp.player.worldY - gp.player.screenY
            && worldY - gp.titleSize < gp.player.worldY + gp.player.screenY) {
                gd.drawImage(tile[num].image, screenX, screenY, gp.titleSize, gp.titleSize, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
    public Tile getTileAt(int col, int row) {
        // Trả về tile tại cột và hàng tương ứng
        int i = mapTile[col][row];
        return tile[i];
    }
}
