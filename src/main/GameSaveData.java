package main;
import java.io.Serializable;
import java.util.ArrayList;

public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    public int HP;
    public int map;
    public ArrayList<String> items;
    public ArrayList<Integer> itemsCount;
    public int money = 0;

    // Constructor
    public GameSaveData(int HP, int map, ArrayList<String> items, ArrayList<Integer> itemsCount, int money) {
        this.HP = HP;
        this.map = map;
        this.items = new ArrayList<>(items); // Copy các danh sách để lưu trạng thái
        this.itemsCount = new ArrayList<>(itemsCount);
        this.money = money;
    }
}
