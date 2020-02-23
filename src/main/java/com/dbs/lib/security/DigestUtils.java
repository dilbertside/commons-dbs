/**
 * DigestUtils
 */
package com.dbs.lib.security;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.dbs.lib.Utils;

/**
 * add dependency org.bouncycastle:bcpkix-jdk15on:1.62, minimum version 1.58
 * 
 * 
 * @author dbs at 23 Feb 2020 11:08:41
 * @since 1.0.11
 * @version 1.0
 */
@lombok.experimental.UtilityClass
@lombok.extern.slf4j.Slf4j
public class DigestUtils {

  private static MessageDigest sha1;
  private static MessageDigest md5;
  private static MessageDigest sha256;
  private static MessageDigest sha384;
  private static MessageDigest sha512;
  private static Cipher cipherAes128gcm;
  private static Cipher cipherAes128ofb;
  private static Cipher cipherAes128cfb;
  private static Cipher cipherAes128ecb;
  private static Cipher cipherAes128cbc;
  @SuppressWarnings("unused")
  private static Cipher cipherAes128cbcP;
  private static Cipher cipherAes128ctr;
  private static Cipher cipherAesElitecore;
  private static MessageDigest blake2b160;
  private static MessageDigest blake2b256;
  private static MessageDigest blake2b384;
  private static MessageDigest blake2b512;

  static {
    Security.addProvider(new BouncyCastleProvider());
    try {
      sha1 = MessageDigest.getInstance("SHA1");
      md5 = MessageDigest.getInstance("MD5");
      sha256 = MessageDigest.getInstance("SHA-256");
      sha384 = MessageDigest.getInstance("SHA-384");
      sha512 = MessageDigest.getInstance("SHA-512");
      cipherAesElitecore = Cipher.getInstance("AES");
      cipherAes128gcm = Cipher.getInstance("AES_128/GCM/NoPadding");
      cipherAes128ofb = Cipher.getInstance("AES_128/OFB/NoPadding");
      cipherAes128cfb = Cipher.getInstance("AES_128/CFB/NoPadding");
      cipherAes128ecb = Cipher.getInstance("AES_128/ECB/NoPadding");
      cipherAes128cbc = Cipher.getInstance("AES_128/CBC/NoPadding");//Cipher Block Chaining
      //secure
      cipherAes128cbcP = Cipher.getInstance("AES/CBC/PKCS5Padding");//Cipher Block Chaining
      cipherAes128ctr = Cipher.getInstance("AES/CTR/NoPadding");//Counter
      blake2b160 = MessageDigest.getInstance("Blake2b-160");
      blake2b256 = MessageDigest.getInstance("Blake2b-256");
      blake2b384 = MessageDigest.getInstance("Blake2b-384");
      blake2b512 = MessageDigest.getInstance("Blake2b-512");
    } catch (Exception e) {
      log.error( "DigestUtils failed to initialize with error [{}]", Utils.errorMessage(e));
      log.debug("DigestUtils Exception", e);
    }
  }

  /**
   * 
   * @return list of all algo providers
   */
  public static String[] getAllProviders() {
    Provider[] prv = Security.getProviders();
    String[] list = new String[prv.length];
    int i = 0;
    for (Provider provider : prv) {
      list[i++] = provider.getName();
    }
    return list;
  }

  /**
   * make a digest checksum with SHA-1 hash function
   * 
   * @param input to hash
   * @return the hash, or empty string if input is null or empty
   * @throws NoSuchAlgorithmException if sha1 does not exist
   */
  public static String sha1(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = sha1.digest(input.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * digest MD-5
   * @param input to encode
   * @return encoded string
   * @throws NoSuchAlgorithmException if md5 does not exist
   */
  public static String md5(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = md5.digest(input.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * digest SHA-256
   * @param input to encode
   * @return encoded string
   * @throws NoSuchAlgorithmException if sha256 does not exist
   */
  public static String sha256(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = sha256.digest(input.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }
  
  /**
   * digest SHA-512
   * @param input to encode
   * @return encoded string
   * @throws NoSuchAlgorithmException if sha512 does not exist
   */
  public static String sha512(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = sha512.digest(input.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }
  
  /**
   * digest SHA-384
   * @param input to encode
   * @return encoded string
   * @throws NoSuchAlgorithmException if sha512 does not exist
   */
  public static String sha384(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = sha384.digest(input.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  
  /**
   * used cipher AES for Elitecore Authentication user
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aesElitecore(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAesElitecore.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAesElitecore.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }
  
  /**
   * used cipher AES 128 GCM
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aes128gcm(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAes128gcm.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAes128gcm.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }

  /**
   * used cipher AES 128 OFB
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aes128ofb(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAes128ofb.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAes128ofb.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }

  /**
   * used cipher AES 128 CFB
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aes128cfb(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAes128cfb.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAes128cfb.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }

  /**
   * used cipher AES 128 ECB
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aes128ecb(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAes128ecb.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAes128ecb.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }

  /**
   * used cipher AES 128 CBC
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aes128cbc(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAes128cbc.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAes128cbc.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }

  /**
   * used cipher AES 128 CTR
   * @param input to encode
   * @param key to use for cipher
   * @return encoded string
   * @throws GeneralSecurityException if sha512 does not exist
   */
  public static String aes128ctr(String input, String key) throws GeneralSecurityException {
    if (StringUtils.isAnyBlank(key, input)) {
      return "";
    }
    Key skeySpec = generateKeySpec(key, "AES");
    cipherAes128ctr.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipherAes128ctr.doFinal(input.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
  }

  /**
   * use {@link SecretKeySpec} <br>
   * @see also {@link IvParameterSpec}
   * @param key secret key
   * @return {@link Key}
   */
  protected static Key generateKeySpec(String key, String algorithm) {
    return new SecretKeySpec(key.getBytes(), algorithm);
  }
  
  protected static IvParameterSpec generateKey(String key) {
    return new IvParameterSpec(key.getBytes());
  }
  
  /**
   * make a digest checksum with SHA-1 hash function
   * 
   * @param input
   *          to hash
   * @return the hash, or empty string if input is null or empty
   * @throws NoSuchAlgorithmException if algo not found
   */
  public static String blake2b160(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = blake2b160.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * make a digest checksum with SHA-1 hash function
   * 
   * @param input
   *          to hash
   * @return the hash, or empty string if input is null or empty
   * @throws NoSuchAlgorithmException if algo not found
   */
  public static String blake2b256(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = blake2b256.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * make a digest checksum with SHA-1 hash function
   * 
   * @param input
   *          to hash
   * @return the hash, or empty string if input is null or empty
   * @throws NoSuchAlgorithmException if algo not found
   */
  public static String blake2b384(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = blake2b384.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * make a digest checksum with SHA-1 hash function
   * 
   * @param input
   *          to hash
   * @return the hash, or empty string if input is null or empty
   * @throws NoSuchAlgorithmException if algo not found
   */
  public static String blake2b512(String input) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(input)) {
      return "";
    }
    byte[] result = blake2b512.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * Example call <br>
   * Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            showHashAlgorithms(provider, MessageDigest.class);
        }
   * queries the runtime for available algorithms.
   * @param provider {@link Provider}
   * @param typeClass {@link MessageDigest} {@link Cipher}, {@link SecureRandom}, {@link Mac}, 
   *  {@link KeyAgreement}, {@link KeyFactory} or any other type of algorithm.
   * @return list of available algorithms to be printed
   */
  public static final Set<String> showHashAlgorithms(Provider provider, Class<?> typeClass) {
    Set<String> output = new HashSet<>();
    String type = typeClass.getSimpleName();

    List<Service> algos = new ArrayList<>();

    Set<Service> services = provider.getServices();
    for (Service service : services) {
      if (service.getType().equalsIgnoreCase(type)) {
        algos.add(service);
      }
    }

    if (!algos.isEmpty()) {
      output.add(String.format(" --- Provider %s, version %.2f --- %n", provider.getName(), provider.getVersion()));
      for (Service service : algos) {
        String algo = service.getAlgorithm();
        output.add(String.format("Algorithm name: \"%s\"%n", algo));
      }
    }

    // --- find aliases (inefficiently)
    Set<Object> keys = provider.keySet();
    for (Object key : keys) {
      final String prefix = "Alg.Alias." + type + ".";
      if (key.toString().startsWith(prefix)) {
        String value = provider.get(key.toString()).toString();
        output.add(String.format("Alias: \"%s\" -> \"%s\"%n", key.toString().substring(prefix.length()), value));
      }
    }
    return Collections.unmodifiableSet(output);
  }
}
