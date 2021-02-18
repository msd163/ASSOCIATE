package system;

import java.util.Date;

public class ServiceMetaInfo {

    public ServiceMetaInfo(Service service, Agent publisher, float coefficient) {
        this.service = service;
        this.publisher = publisher;   // watcher of the service
        this.coefficient = coefficient;
        this.time = new Date().getTime();
    }

    private Service service;
    private Agent publisher;
    private float coefficient;
    private long time;

    //============================//============================//============================
    private float getForgottenValue() {
        return (float) time /new Date().getTime();
    }

    //============================//============================//============================
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Agent getPublisher() {
        return publisher;
    }

    public void setPublisher(Agent publisher) {
        this.publisher = publisher;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
