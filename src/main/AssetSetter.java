package main;

import object.OBJ_Slime;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj.add(new OBJ_Slime());
        gp.obj.get(0).worldX = 23 * gp.titleSize;
        gp.obj.get(0).worldY = 23 * gp.titleSize;
        
        gp.obj.add(new OBJ_Slime());
        gp.obj.get(1).worldX = 23 * gp.titleSize;
        gp.obj.get(1).worldY = 19 * gp.titleSize;    

        gp.obj.add(new OBJ_Slime());
        gp.obj.get(2).worldX = 21 * gp.titleSize;
        gp.obj.get(2).worldY = 21 * gp.titleSize;

        gp.obj.add(new OBJ_Slime());
        gp.obj.get(3).worldX = 25 * gp.titleSize;
        gp.obj.get(3).worldY = 21 * gp.titleSize;
    }
}
