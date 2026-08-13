package com.games.common.utils;



import cn.hutool.core.codec.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * AES + RSA Hybrid Encryption Utility
 * Supports encryption & decryption
 */
public class AESTool {

    // AES GCM 模式参数
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 128;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;

    // RSA 算法参数
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final int RSA_KEY_SIZE = 2048;

    /**
     * 生成RSA公钥私钥对
     */
    public static Map<String, String> generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(RSA_KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();

        Map<String, String> keys = new HashMap<>();
        keys.put("publicKey", Base64.encode(keyPair.getPublic().getEncoded()));
        keys.put("privateKey", Base64.encode(keyPair.getPrivate().getEncoded()));
        return keys;
    }

    /**
     * 生成随机AES密钥
     */
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    /**
     * 使用RSA公钥加密AES密钥
     */
    public static String encryptAESKey(SecretKey aesKey, String rsaPublicKeyBase64) throws Exception {
        byte[] publicKeyBytes = Base64.decode(rsaPublicKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(spec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(aesKey.getEncoded());
        return Base64.encode(encrypted);
    }

    /**
     * 使用RSA私钥解密AES密钥
     */
    public static SecretKey decryptAESKey(String encryptedAESKeyBase64, String rsaPrivateKeyBase64) throws Exception {
        byte[] privateKeyBytes = Base64.decode(rsaPrivateKeyBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(spec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedKey = cipher.doFinal(Base64.decode(encryptedAESKeyBase64));
        return new SecretKeySpec(decodedKey, "AES");
    }

    /**
     * AES 加密（返回Base64）
     */
    public static String encryptData(String plainText, SecretKey aesKey) throws Exception {
        byte[] iv = SecureRandom.getInstanceStrong().generateSeed(GCM_IV_LENGTH);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // 拼接 IV + Ciphertext
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        return Base64.encode(combined);
    }

    /**
     * AES 解密（输入Base64）
     */
    public static String decryptData(String encryptedBase64, SecretKey aesKey) throws Exception {
        byte[] decoded = Base64.decode(encryptedBase64);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] ciphertext = new byte[decoded.length - GCM_IV_LENGTH];
        System.arraycopy(decoded, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(decoded, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, spec);
        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 测试示例
     */
    public static void main(String[] args) throws Exception {
        System.out.println("---- Generate RSA Keys ----");
        Map<String, String> rsaKeys = generateRSAKeyPair();
        String publicKey = rsaKeys.get("publicKey");
        String privateKey = rsaKeys.get("privateKey");

        System.out.println("Public Key: " + publicKey);
        System.out.println("Private Key: " + privateKey);

        // 生成AES密钥
        SecretKey aesKey = generateAESKey();
        String encryptedAESKey = encryptAESKey(aesKey, publicKey);

        System.out.println("\nEncrypted AES Key: " + encryptedAESKey);

        // 加密数据
        String data = "Sensitive credit card info: 4111-1111-1111-1111";
        String encryptedData = encryptData(data, aesKey);
        System.out.println("\nEncrypted Data: " + encryptedData);

        // 解密AES密钥
        SecretKey decryptedAESKey = decryptAESKey(encryptedAESKey, privateKey);

        // 解密数据
        String decryptedData = decryptData(encryptedData, decryptedAESKey);
        System.out.println("\nDecrypted Data: " + decryptedData);
    }
}
