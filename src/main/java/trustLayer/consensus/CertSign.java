package trustLayer.consensus;

import org.apache.commons.codec.binary.Base64;
import systemLayer.Agent;
import utils.Cryptor;

public class CertSign {

    private int time;
    private Agent signer;
    private Agent signed;
    private float trustValue;
    private String sign;

    //============================//============================//============================
    public String doSign() {
        byte[] bytes = Cryptor.sign_RSA(signer, signed, trustValue, time);

        sign = Base64.encodeBase64String(bytes);
        return sign;
    }


    public boolean isValid() {
        return Cryptor.verifySign_RSA(signer, signed, trustValue, time, sign);
    }

    //============================//============================//============================


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Agent getSigner() {
        return signer;
    }

    public void setSigner(Agent signer) {
        this.signer = signer;
    }

    public Agent getSigned() {
        return signed;
    }

    public void setSigned(Agent signed) {
        this.signed = signed;
    }

    public float getTrustValue() {
        return trustValue;
    }

    public void setTrustValue(float trustValue) {
        this.trustValue = trustValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
