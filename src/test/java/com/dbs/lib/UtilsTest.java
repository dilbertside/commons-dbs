/**
 * UtilsTest
 */
package com.dbs.lib;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @author dbs on Aug 24, 2016 3:33:58 PM
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class UtilsTest {

	/**
	 * Test method for {@link com.sti.lib.Utils#isValidIndoNumber(java.lang.String)}.
	 */
	@Test
	public void testIsValidIndoNumber() {
		String ph1 = "082817082634";
		assertTrue(Utils.isValidIndoNumber(ph1), "must be valid");
		
		String ph2 = "6282817082634";
		assertTrue(Utils.isValidIndoNumber(ph2), "must be valid");
		
		String ph3 = "+6282817082634";
		assertTrue(Utils.isValidIndoNumber(ph3), "must be valid");
	}
	
	@Test
	public void idLocaleTest() {
		Locale[] a = Locale.getAvailableLocales();
		for (Locale locale : a) {
			System.out.println(locale.toString());
		}
		
		Locale locale = Utils.getLocale("id");
		assertNotNull(locale);
		assertTrue(locale.toString().equals("in"), "must be in");
	}
	
	@Test
  public void toStringFormatted() {
	  //String fmt = Utils.toStringFormatted(obj);
    String obj = "com.sti.bss.idv.dto.CustomerDto@7a36c1f8[\n  contactNumber=<null>\n  createdDate=2018-01-10T09:14:44.347+07:00[Asia/Jakarta]\n  dukcapilRaw=<null>\n  subscriberType=none\n  email=<null>\n  firstName=john\n  idNumber=8123456789123456\n  typeId=KTP\n  kkNumber=8203202304081784\n  lastName=Doe\n  locations=[]\n  motherMaidenName=<null>\n  poc=<null>\n  preferedLanguage=<null>\n  bizServices=[]\n  device=<null>\n  status=ONE\n  serviceStatus=selfRegistration\n  token=[id=<null>,smsCode=AAAAA,emailCode=BBBBB,numberOfRetryEmail=0,nubmerOfRetrySms=0]\n  country=[id=102,name=Indonesia,code=ID]\n  customerId=1801012908\n  additionalProperties={}\n]";
    String str = StringUtils.substringAfter(obj, "[");
    str = StringUtils.removeEnd(str, "]").trim();
    str = RegExUtils.replaceAll(str, "=", ": ") ;
    assertNotNull(str);
    assertFalse(str.contains("com.sti.bss.idv.dto.CustomerDto@7a36c1f8["), "must be not in");
  }

	/**
   * Test method for {@link com.sti.lib.Utils#computeTax(java.math.BigDecimal, Float)}.
   */
	@Test
  public void computeTaxTest() {
	  BigDecimal price = new BigDecimal(100);
	  BigDecimal priceVat = Utils.computeTax(price, 10f);
	  assertNotNull(priceVat);
	  System.out.println(priceVat.toPlainString());
    assertTrue(priceVat.compareTo(new BigDecimal(110)) == 0, "must be 110");
  }
	
	 @Test
	  public void computeTax2Test() {
	   BigDecimal price = new BigDecimal(180909);
	   BigDecimal priceVat = Utils.computeTax(price, 10f);
	   System.out.println(priceVat.toPlainString());
	   
	   BigDecimal price1 = new BigDecimal(2170909);
     BigDecimal priceVat1 = Utils.computeTax(price1, 10f);
     System.out.println(priceVat1.toPlainString());
	   
     BigDecimal price2 = new BigDecimal(2351818);
     BigDecimal priceVat2 = Utils.computeTax(price2, 10f);
     assertNotNull(priceVat2);
     System.out.println(priceVat2.toPlainString());
     assertTrue(priceVat2.compareTo(new BigDecimal(2587000)) == 0, "must be 2587000");
     
     
     
     BigDecimal total = BigDecimal.ZERO;
     total = total.add(priceVat).add(priceVat1).add(priceVat2);
     System.out.println(total.toPlainString());
     
	 }

  @Test
  public void computeTaxTest100M() {
    BigDecimal price = new BigDecimal(153000000);
    BigDecimal priceVat = Utils.computeTax(price, 10f);
    assertNotNull(priceVat);
    System.out.println(priceVat.toPlainString());
    assertTrue(priceVat.compareTo(new BigDecimal(168300000)) == 0, "must be 110");
  }
  
  @Test
  public void humanReadableByteCount() {
    long[] bytes = new long[] {0, 27, 999, 1000, 1023, 1024, 1728, 110592, 7077888, 452984832, 28991029248l, 1855425871872l , Long.MAX_VALUE};
    System.out.println(String.format("%20s: %10s %10s", "bytes", "SI", "BINARY"));
    for (long l : bytes) {
      System.out.println(String.format("%20d: %10s %10s", l, Utils.humanReadableByteCount(l, true), Utils.humanReadableByteCount(l, false)));
    }
  }

}
