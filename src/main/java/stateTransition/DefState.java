package stateTransition;

public class DefState {
    private int ID;
    private String Name;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }
    private boolean depi = false;
    public boolean depicted() {
        return depi;
    }
    public void setDepicted()
    {
        depi = true;
    }
    public void resetDepicted()
    {
        depi = false;
    }
    private int locX;
    private int locY;
    private boolean hasLoc = false;
    public int getY() {
        return locY;
    }

    public int getX() {
        return locX;
    }

    public void setLocation(int x,int y)
    {
        locX = x;
        locY = y;
        hasLoc = true;
        System.out.println( " [" + ID + "] X " + x + " Y " + y);
    }

    public boolean hasLocation()
    {
        return hasLoc;
    }
    private  int inDeg = 0;
    private  int outDeg = 0;
    public void incOutDegree() {
        outDeg++;
    }

    public void incInDegree() {
        inDeg++;
    }

    public int getInDegree() {
        return inDeg;
    }

    public int getOutDegree() {
        return outDeg;

    }
}
