package main;

import object.OBJ_Slime;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0] = new OBJ_Slime();
        gp.obj[0].worldX = 23 * gp.titleSize;
        gp.obj[0].worldY = 23 * gp.titleSize;      
        
        gp.obj[1] = new OBJ_Slime();
        gp.obj[1].worldX = 23 * gp.titleSize;
        gp.obj[1].worldY = 19 * gp.titleSize;    

        gp.obj[2] = new OBJ_Slime();
        gp.obj[2].worldX = 21 * gp.titleSize;
        gp.obj[2].worldY = 21 * gp.titleSize;

        gp.obj[3] = new OBJ_Slime();
        gp.obj[3].worldX = 25 * gp.titleSize;
        gp.obj[3].worldY = 21 * gp.titleSize;
    }
}
