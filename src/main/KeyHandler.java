package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener{

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean EPressed;
    public boolean RPressed;
    public boolean SpacePressed = false;
    public int i = -1;

    @Override
    public void keyPressed(KeyEvent e) { // Khi nhấn phím
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_R){
            RPressed = true;
        }
        if(code == KeyEvent.VK_E){
            EPressed = true;
        }
        switch (code) {
            case KeyEvent.VK_1 -> i = 0;
            case KeyEvent.VK_2 -> i = 1;
            case KeyEvent.VK_3 -> i = 2;
            case KeyEvent.VK_4 -> i = 3;
            case KeyEvent.VK_5 -> i = 4;
            case KeyEvent.VK_6 -> i = 5;
            case KeyEvent.VK_7 -> i = 6;
            case KeyEvent.VK_8 -> i = 7;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) { // Khi nhả phím
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_E){
            EPressed = false;
        }
        if(code == KeyEvent.VK_R){
            RPressed = false;
        }
        if(code == KeyEvent.VK_ALPHANUMERIC) i = -1;
        if(code == KeyEvent.VK_SPACE) SpacePressed = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }  
}
