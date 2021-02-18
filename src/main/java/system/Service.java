package system;

public class Service {

    public Service() {
        id = getNextId();
        result = 0;
    }

    private static int NEXT_ID = 0;

    private int id;

    private ServiceType serviceType;

    private Agent requester;

    private Agent doer;

    private float result;


    //============================//============================//============================


    public static int getNextId() {
        return ++NEXT_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Agent getRequester() {
        return requester;
    }

    public void setRequester(Agent requester) {
        this.requester = requester;
    }

    public Agent getDoer() {
        return doer;
    }

    public void setDoer(Agent doer) {
        this.doer = doer;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }
}
