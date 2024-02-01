package WSM.trust.consensus;

import core.utils.Globals;

public class CertVerify {

    public CertVerify(int id) {
        if (id == -1) {
            this.id = Globals.DAGRA_VERIFY_NEXT_ID++;
        } else {
            this.id = id;
        }
    }

    private int id;
    private int time;
    private CertContract verifier;
    private CertContract verified;
    private boolean result;

    //============================//============================//============================
    public CertVerify clone() {
        CertVerify obj = new CertVerify(this.id);
        obj.setResult(result);
        obj.setVerified(verified);
        obj.setVerifier(verifier);
        obj.setTime(time);
        return obj;
    }
    //============================//============================//============================

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public CertContract getVerifier() {
        return verifier;
    }

    public void setVerifier(CertContract verifier) {
        this.verifier = verifier;
    }

    public CertContract getVerified() {
        return verified;
    }

    public void setVerified(CertContract verified) {
        this.verified = verified;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
