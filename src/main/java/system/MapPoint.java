package system;

import utils.Config;

public class MapPoint {

    public MapPoint() {
    }

    public MapPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int x;
    private int y;

    //============================//============================//============================

    public void modify() {
        int xMode = x % Config.MAP_TILE_SIZE;
        int yMode = y % Config.MAP_TILE_SIZE;

        x -= xMode;
        if (xMode > Config.MAP_TILE_SIZE / 2) {
            x += Config.MAP_TILE_SIZE;
        }

        y -= yMode;
        if (yMode > Config.MAP_TILE_SIZE / 2) {
            y += Config.MAP_TILE_SIZE;
        }
    }

    public boolean isEquals(MapPoint point) {
        return point != null && point.x == this.x && point.y == this.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void minusY() {
        y -= Config.MAP_TILE_SIZE;
    }

    public void addY() {
        y += Config.MAP_TILE_SIZE;
    }

    public void minusX() {
        x -= Config.MAP_TILE_SIZE;
    }

    public void addX() {
        x += Config.MAP_TILE_SIZE;
    }

    public void changeX(int vX) {
        x += vX;
    }

    public void changeY(int vY) {
        y += vY;
    }

    public void print() {
        print("Point: ");
    }

    public void print(String title) {
        System.out.println(title + " [ " + x + " , " + y + " ]");
    }
}
