package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
    GamePanel gp;
    public MouseHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        gp.mouseX = e.getX() + gp.player.worldX - gp.player.screenX;
        gp.mouseY = e.getY() + gp.player.worldY - gp.player.screenY;
        gp.onClick();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
