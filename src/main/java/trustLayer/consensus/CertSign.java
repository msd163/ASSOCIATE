package trustLayer.consensus;

import org.apache.commons.codec.binary.Base64;
import utils.Config;
import utils.Cryptor;
import utils.Globals;

public class CertSign {

    public CertSign(int id) {
        if(id == -1) {
            this.id = Globals.DAGRA_SIGN_NEXT_ID++;
        }else{
            this.id = id;
        }
    }

    private int id;
    private int time;
    private CertContract signer;
    private CertContract signed;
    private float trustValue;
    private String sign;

    //============================//============================//============================
    public String doSign() {
        byte[] bytes = Cryptor.sign_RSA(signer.getRequester(), signed.getRequester(), trustValue, time);

        sign = Base64.encodeBase64String(bytes);
        return sign;
    }


    public boolean isValid() {
        return Cryptor.verifySign_RSA(signer.getRequester(), signed.getRequester(), trustValue, time, sign);
    }

    //============================//============================//============================

    public CertSign clone() {
        CertSign obj = new CertSign(this.id);
        obj.setTrustValue(trustValue);
        obj.setSigned(signed);
        obj.setSigner(signer);
        obj.setTime(time);
        obj.setSign(sign);

        return obj;
    }
    //============================//============================//============================


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public CertContract getSigner() {
        return signer;
    }

    public void setSigner(CertContract signer) {
        this.signer = signer;
    }

    public CertContract getSigned() {
        return signed;
    }

    public void setSigned(CertContract signed) {
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

    public boolean isExpired(int expiredTimeOfCert_daGra) {
        return (Globals.WORLD_TIMER - time) >= expiredTimeOfCert_daGra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
