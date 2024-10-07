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
        int mouseInfo = 0;
        if(e.getButton() == MouseEvent.BUTTON1){
            mouseInfo = 1;
        }
        else if(e.getButton() == MouseEvent.BUTTON3){
            mouseInfo = 2;
        }
        gp.mouseX = e.getX() + gp.player.worldX - gp.player.screenX;
        gp.mouseY = e.getY() + gp.player.worldY - gp.player.screenY;
        gp.onClick(mouseInfo);
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
