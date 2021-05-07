package utils;

public class RectangleX {

    public int x;
    public int y;
    public int with;
    public int height;

    public RectangleX(int x, int y, int with, int height) {
        this.x = x;
        this.y = y;
        this.with = with;
        this.height = height;
    }

    public boolean isOverlapping(RectangleX other) {
        if (this.topRight().getY() < other.bottomLeft().getY()
                || this.bottomLeft().getY() > other.topRight().getY()) {
            return false;
        }
        if (this.topRight().getX() < other.bottomLeft().getX()
                || this.bottomLeft().getX() > other.topRight().getX()) {
            return false;
        }
        return true;
    }

    public Point topRight() {
        return new Point(x + with, y);
    }

    public Point topLeft() {
        return new Point(x, y);
    }

    public Point bottomRight() {
        return new Point(x + with, y + height);
    }

    public Point bottomLeft() {
        return new Point(x, y + height);
    }
}
