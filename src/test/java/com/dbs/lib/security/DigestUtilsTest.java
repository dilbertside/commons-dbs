/**
 * DigestUtilsTest
 */
package com.dbs.lib.security;

import static org.junit.jupiter.api.Assertions.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import javax.crypto.Cipher;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


/**
 * 
 * @author dbs at 23 Feb 2020 10:49:03
 * @since 1.0.11
 * @version 1.0
 */
@Tag("Digest-MD5-SHAx-BLAK2b_X")
public class DigestUtilsTest {

  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] { { "6282881390158|510278280822315|free" }, { "6282881390150|510278280822316|gold" } });
  }

  /**
   * Test method for {@link com.sti.lib.utils.DigestUtils#getAllProviders()}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @Test
  public void testGetAllProvidersMessageDigest() throws NoSuchAlgorithmException {
    System.out.println("+++++++++++++++++++++++++++++ MessageDigest ++++++++++++++++++++++++++++++++++++");
    for (String iter : DigestUtils.getAllProviders()) {
      System.out.println(iter);
    }
    Provider[] providers = Security.getProviders();
    for (Provider provider : providers) {
      Set<String> outputs = DigestUtils.showHashAlgorithms(provider, MessageDigest.class);
      for (String output : outputs) {
        System.out.println(output);
      }
    }
    System.out.println("----------------------------- MessageDigest ------------------------------------");
  }

  /**
   * Test method for {@link com.sti.lib.utils.DigestUtils#getAllProviders()}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @Test
  public void testGetAllProvidersCipher() throws NoSuchAlgorithmException {
    System.out.println("+++++++++++++++++++++++++++++ Cipher ++++++++++++++++++++++++++++++++++++");
    Provider[] providers = Security.getProviders();
    for (Provider provider : providers) {
      Set<String> outputs = DigestUtils.showHashAlgorithms(provider, Cipher.class);
      for (String output : outputs) {
        System.out.println(output);
      }
    }
    System.out.println("----------------------------- Cipher ------------------------------------");
  }

  /**
   * Test method for {@link com.sti.lib.utils.DigestUtils#sha1(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   * @throws CloneNotSupportedException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testSha1(String toDigest) throws NoSuchAlgorithmException, CloneNotSupportedException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String apache = org.apache.commons.codec.digest.DigestUtils.sha1Hex(toDigest);
    System.out.println(String.format("testSha1 Duration apache: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
    assertNotNull("must be valid", apache);

    start = System.nanoTime();
    String our = DigestUtils.sha1(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testSha1 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));

    assertEquals(apache, our);
  }

  /**
   * Test method for {@link com.sti.lib.utils.DigestUtils#md5(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testMd5(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String apache = org.apache.commons.codec.digest.DigestUtils.md5Hex(toDigest);
    System.out.println(String.format("testMd5 Duration apache: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
    assertNotNull("must be valid", apache);

    start = System.nanoTime();
    String our = DigestUtils.md5(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testMd5 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));

    assertEquals(apache, our);
  }

  /**
   * Test method for {@link com.sti.lib.utils.DigestUtils#sha256(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testSha256(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String apache = org.apache.commons.codec.digest.DigestUtils.sha256Hex(toDigest);
    System.out.println(String.format("testSha256 Duration apache: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
    assertNotNull("must be valid", apache);

    start = System.nanoTime();
    String our = DigestUtils.sha256(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testSha256 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));

    assertEquals(apache, our);
  }

  /**
   * Test method for {@link com.sti.lib.utils.DigestUtils#sha512(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testSha512(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String apache = org.apache.commons.codec.digest.DigestUtils.sha512Hex(toDigest);
    System.out.println(String.format("testSha512 Duration apache: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
    assertNotNull("must be valid", apache);

    start = System.nanoTime();
    String our = DigestUtils.sha512(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testSha512 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));

    assertEquals(apache, our);
  }

  /**
   * Test method for {@link com.sti.lib.security.DigestUtils#blake2b160(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testBlake2b_160(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String our = DigestUtils.blake2b160(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testBlake2b_160 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
  }

  /**
   * Test method for {@link com.sti.lib.security.DigestUtils#blake2b256(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testBlake2b_256(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String our = DigestUtils.blake2b256(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testBlake2b_256 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
  }

  /**
   * Test method for {@link com.sti.lib.security.DigestUtils#blake2b384(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testBlake2b_384(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String our = DigestUtils.blake2b384(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testBlake2b_384 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
  }

  /**
   * Test method for {@link com.sti.lib.security.DigestUtils#blake2b512(java.lang.String)}.
   * 
   * @throws NoSuchAlgorithmException
   */
  @ParameterizedTest
	@MethodSource("data")
  public void testBlake2b_512(String toDigest) throws NoSuchAlgorithmException {
    System.out.println("\n--------------------\n");
    long start = System.nanoTime();
    String our = DigestUtils.blake2b512(toDigest);
    assertNotNull("must be valid", our);
    System.out.println(String.format("testBlake2b_512 Duration our: %s\n", Duration.ofNanos(System.nanoTime() - start).toString()));
  }

}
