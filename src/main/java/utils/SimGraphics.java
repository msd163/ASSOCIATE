package utils;

import java.awt.*;

public class SimGraphics {

    public static void draw(Graphics2D g, int x, int y, Color color, int index) {
        draw(g, x, y, color, index, 4);
    }

    public static void draw(Graphics2D g, int x, int y, Color color, int index, int size) {
        switch (index) {
            case 0:
                g.setColor(color);
                g.fillOval(x, y, size, size);
                break;
            case 1:
                g.setColor(color);
                g.drawLine(x - (size / 2), y, x + (size / 2), y);
                g.drawLine(x, y - (size / 2), x, y + (size / 2));
                break;
            case 2:
                g.setColor(color);
                g.fillRect(x, y, size, size);
                break;
            case 3:
                g.setColor(color);
                g.drawLine(x - (size / 2), y - (size / 2), x + (size / 2), y + (size / 2));
                g.drawLine(x - (size / 2), y + (size / 2), x + (size / 2), y - (size / 2));
                break;
            case 4:
                g.setColor(color);
                g.drawRect(x, y, size, size);
                break;
            case 5:
                g.setColor(color);
                g.drawOval(x, y, size, size);
                break;
            case 6:
                g.setColor(color);
                g.drawLine(x, y, x + size, y);
                break;
            default:
                g.setColor(color);
                g.drawLine(x, y, x, y + size * 3);

        }
    }
}
