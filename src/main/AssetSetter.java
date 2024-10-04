package main;

import object.OBJ_Slime;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0] = new OBJ_Slime();
        gp.obj[0].worldX = 50 * gp.titleSize;
        gp.obj[0].worldY = 50 * gp.titleSize;      
        
        gp.obj[1] = new OBJ_Slime();
        gp.obj[1].worldX = 50 * gp.titleSize;
        gp.obj[1].worldY = 46 * gp.titleSize;    

        gp.obj[2] = new OBJ_Slime();
        gp.obj[2].worldX = 48 * gp.titleSize;
        gp.obj[2].worldY = 48 * gp.titleSize;

        gp.obj[3] = new OBJ_Slime();
        gp.obj[3].worldX = 52 * gp.titleSize;
        gp.obj[3].worldY = 48 * gp.titleSize;
    }
}
