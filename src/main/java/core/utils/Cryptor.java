package core.utils;

import org.apache.commons.codec.binary.Base64;
import WSM.society.agent.Agent;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Cryptor {


    //============================//============================//============================  DSA Digital Signature

    private static KeyPair loadKeyPair_DSA(byte[] encodedPublicKey, byte[] encodedPrivateKey) {

        PublicKey publicKey;
        PrivateKey privateKey;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);

            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return new KeyPair(publicKey, privateKey);
    }

    public static byte[] sign_DSA(byte[] data, byte[] privateKeyByte) {
        PrivateKey privateKey;
        byte[] digitalSignature = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);

            privateKey = keyFactory.generatePrivate(privateKeySpec);

            ////----------
            Signature signature = Signature.getInstance("SHA256WithDSA");
            SecureRandom secureRandom = new SecureRandom();

            signature.initSign(privateKey, secureRandom);

            signature.update(data);

            digitalSignature = signature.sign();

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return digitalSignature;
    }

    public static boolean verifySign_DSA(byte[] data, byte[] sign, byte[] publicKeyByte) {
        PublicKey publicKey;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyByte);
            publicKey = keyFactory.generatePublic(publicKeySpec);

            ////----------
            Signature signature = Signature.getInstance("SHA256WithDSA");

            signature.initVerify(publicKey);
            signature.update(data);

            return signature.verify(sign);

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    //============================//============================//============================ Signature

    public static byte[] sign_RSA(final Agent signer, final Agent signed, float trustValue, int time) {
        return sign_RSA(signer.getId() + "_" + signed.getId() + "_" + trustValue + "_" + time, signer.getPrivateKey());
    }

    public static byte[] sign_RSA(final Agent signer, final Agent signed, float trustValue, int time, final String privateKey) {
        return sign_RSA(signer.getId() + "_" + signed.getId() + "_" + trustValue + "_" + time, privateKey);
    }

    public static byte[] sign_RSA(final String data, final PrivateKey privateKey) {
        try {
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initSign(privateKey);
            instance.update((data).getBytes());
            byte[] signature = instance.sign();

            return signature;

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] sign_RSA(final String data, final String privateKey) {
        return sign_RSA(data, loadPrivateKey_RSA(privateKey));
    }

    public static boolean verifySign_RSA(final Agent signer, final Agent signed, float trustValue, int time, final String signStr) {
        byte[] sign = Base64.decodeBase64(signStr);
      return verifySign_RSA(signer.getId() + "_" + signed.getId() + "_" + trustValue + "_" + time,sign, signer.getPublicKey());

    }

    public static boolean verifySign_RSA(final String message, final byte[] sign, final PublicKey publicKey) {
        try {
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initVerify(publicKey);
            instance.update(message.getBytes());
            return instance.verify(sign);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean verifySign_RSA(final String message, final byte[] sign, final String publicKey) {
        return verifySign_RSA(message, sign, loadPublicKey_RSA(publicKey));

    }

    public static PrivateKey loadPrivateKey_RSA(String privateKey) {

        try {
            byte[] clear = java.util.Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
            KeyFactory fact;
            fact = KeyFactory.getInstance("RSA");
            PrivateKey priv = fact.generatePrivate(keySpec);
            Arrays.fill(clear, (byte) 0);
            return priv;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey loadPublicKey_RSA(String publicKey) {
        try {
            byte[] data = java.util.Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact;
            fact = KeyFactory.getInstance("RSA");
            return fact.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] generateKey() throws GeneralSecurityException {

        String[] keys = new String[2];
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(512);
        KeyPair keyPair = keyPairGen.genKeyPair();

        keys[0] = Cryptor.savePrivateKey(keyPair.getPrivate());
        //System.out.println("Private Key Text");
        //System.out.println(keys[0]);

        keys[1] = Cryptor.savePublicKey(keyPair.getPublic());
        //System.out.println("Public Key Text");
        //System.out.println(keys[1]);

        return keys;
    }


    public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
                PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();

        String key64 = Base64.encodeBase64String(packed);
        Arrays.fill(packed, (byte) 0);
        return key64;
    }


    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
                X509EncodedKeySpec.class);
        return Base64.encodeBase64String(spec.getEncoded());
    }


}