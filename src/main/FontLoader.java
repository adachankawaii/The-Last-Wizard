package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
    public static Font loadFont(String path, float size) {
        try {
            InputStream is = FontLoader.class.getResourceAsStream(path);
            if (is == null) {
                throw new IOException("Font file not found: " + path);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // Sử dụng font Arial dự phòng
        }
    }

}
