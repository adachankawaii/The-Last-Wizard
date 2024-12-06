package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        // TẠO CỬA SỔ GAME
        JFrame window = new JFrame();
        window.setTitle("The Last Wizard");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sẽ kết thúc game khi đóng
        window.setResizable(true); // Có thể thay đổi độ lớn cửa sổ hay k

        // TẠO GAME PANEL
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); // Giới hạn size của các obj
        window.setResizable(true);
        // CÀI ĐẶT HIỂN THỊ
        window.setLocationRelativeTo(null); // Màn hình spawn ở center
        window.setVisible(true); // Hiển thị màn hình

        // ĐẶT ĐỒ LÊN MAP
        // CHẠY THREAD
        gp.startGameThread();
    }
}
