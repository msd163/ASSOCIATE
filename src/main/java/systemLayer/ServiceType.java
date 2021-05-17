package systemLayer;

public class ServiceType {

    public ServiceType(int id) {
        this.id = id;
    }

    private int id;

    private String title;
    //============================//============================//============================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
