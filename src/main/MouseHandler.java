package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
    GamePanel gp;
    public boolean isClicked = false;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Không cần xử lý gì ở đây
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseInfo = 0;
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseInfo = 1;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            mouseInfo = 2;
        }
        isClicked = true;
        gp.mouseX = e.getX() + gp.player.worldX - gp.player.screenX;
        gp.mouseY = e.getY() + gp.player.worldY - gp.player.screenY;
        gp.onClick(mouseInfo);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Không cần xử lý gì ở đây
        isClicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Không cần xử lý gì ở đây
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Không cần xử lý gì ở đây
    }

    public void mouseMoved(MouseEvent e) {
        // Cập nhật vị trí chuột ngay khi nó di chuyển
        gp.mouseX = e.getX() + gp.player.worldX - gp.player.screenX;
        gp.mouseY = e.getY() + gp.player.worldY - gp.player.screenY;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Cập nhật vị trí chuột khi nó được kéo
        gp.mouseX = e.getX() + gp.player.worldX - gp.player.screenX;
        gp.mouseY = e.getY() + gp.player.worldY - gp.player.screenY;
    }
}
