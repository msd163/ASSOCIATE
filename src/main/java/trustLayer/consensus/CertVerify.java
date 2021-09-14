package trustLayer.consensus;

public class CertVerify {

    private int time;
    private CertContract verifier;
    private CertContract verified;
    private boolean result;

    //============================//============================//============================

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
}
