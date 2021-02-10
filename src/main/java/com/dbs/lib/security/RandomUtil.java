package com.dbs.lib.security;


import org.apache.commons.lang3.RandomStringUtils;
import java.security.SecureRandom;

/**
 * @author dbs at 18 Sep 2019 10:39:41
 * @since 1.0.10
 * @version 1.0
 */
@lombok.experimental.UtilityClass
public class RandomUtil {

  private static final int DEF_COUNT = 20;

  private static SecureRandom SECURE_RANDOM;

  static {
      SECURE_RANDOM = new SecureRandom();
      SECURE_RANDOM.nextBytes(new byte[64]);
  }

  public static SecureRandom getSecureRandom() {
    return SECURE_RANDOM;
  }
  
  public static void setSecureRandom(SecureRandom secr) {
    SECURE_RANDOM = secr;
  }

  private static String generateRandomAlphanumericString() {
      return RandomStringUtils.random(DEF_COUNT, 0, 0, true, true, null, SECURE_RANDOM);
  }

  /**
   * Generate a password.
   *
   * @return the generated password.
   */
  public static String generatePassword() {
      return generateRandomAlphanumericString();
  }

  /**
   * Generate an activation key.
   *
   * @return the generated activation key.
   */
  public static String generateActivationKey() {
      return generateRandomAlphanumericString();
  }

  /**
   * Generate a reset key.
   *
   * @return the generated reset key.
   */
  public static String generateResetKey() {
      return generateRandomAlphanumericString();
  }
}
