package com.java.gui;

import java.awt.*;
import java.io.File;

public class CustomFonts {
    public static Font load(String path, float size, int style) {
        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return f.deriveFont(style, size);
        } catch (Exception e) {
            return new Font("Arial", style, (int)size);
        }
    }
}
