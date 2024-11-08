package entity.player;

import entity.Entity;
import entity.Items.Coin;
import entity.Items.CommonItem;
import entity.Items.HPBottle;
import entity.Items.ThrowingBottle;
import main.GamePanel;

import java.io.Serializable;
import java.util.Random;

public class Quest{
    public String name;
    public String description;
    public boolean isComplete;
    public int targetAmount;  // Mục tiêu cần đạt, ví dụ: giết 10 quái
    public int currentAmount; // Số lượng đã hoàn thành
    String targetType; // Loại mục tiêu (vd: quái, vật phẩm)
    String rewardType;
    int rewardCount = 0;
    GamePanel gp;
    public String targetObject;

    public Quest(String name, String description, String targetType, int targetAmount,String targetObject, String rewardType, int rewardCount, GamePanel gp) {
        this.name = name;
        this.description = description;
        this.targetType = targetType;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
        this.isComplete = false;
        this.rewardType = rewardType;
        this.rewardCount = rewardCount;
        this.targetObject = targetObject;
        this.gp = gp;
    }

    // Kiểm tra xem nhiệm vụ có hoàn thành hay chưa
    public void checkCompletion() {
        if (currentAmount >= targetAmount) {
            isComplete = true;
        }
    }
    public void getReward(){
        for(int i = 0;i<rewardCount;i++){
            Entity entity1 = createObject(rewardType);
            gp.obj.add(entity1);
        }
    }
    public Entity createObject(String objectType) {
        switch (objectType) {
            case "ThrowingBottle" -> {
                Entity e = new ThrowingBottle(gp);
                e.worldX = gp.player.worldX;
                e.worldY = gp.player.worldY;
                return e;
            }
            case "HPBottle" -> {
                Entity a = new HPBottle("HPBottle", gp);
                a.worldX = gp.player.worldX;
                a.worldY = gp.player.worldY;
                return a;
            }
            case "InvisiblePotion" -> {
                Entity bottle = new HPBottle("InvisiblePotion", gp);
                bottle.worldX = gp.player.worldX;
                bottle.worldY = gp.player.worldY;
                return bottle;
            }
            case "Key" -> {
                Entity n = new CommonItem("Key", gp);
                n.worldX = gp.player.worldX;
                n.worldY = gp.player.worldY;
                return n;
            }
            case "Coin" -> {
                return new Coin(gp.player.worldX + new Random().nextInt(gp.tileSize * 2), gp.player.worldY + new Random().nextInt(gp.tileSize * 2), gp);
            }
            default -> {
                System.out.println("Unknown object type: " + objectType);
                return null;
            }
        }
    }

}
