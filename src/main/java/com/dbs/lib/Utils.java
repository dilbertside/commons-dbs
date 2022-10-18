/**
 * Utils
 */
package com.dbs.lib;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;

/**
 * @author dbs on Aug 21, 2016 2:53:05 PM
 * @since 1.0.0
 * @version 1.0
 * @version 1.1 remove reference to Guava as it is in use by commons DTO and @Nullable
 * @version 1.2 add {@link #loadApplicationPropertiesKey(String, String, String)}
 * @version 1.3 add {@link #sequence(List)}
 * @version 1.4 add {@link #removeAllCrLf(String)}
 *
 */
@lombok.experimental.UtilityClass
public class Utils {

  /**
   * The non-negative {@code int} precision setting: : the number of digits to be used for an operation; results are rounded to this precision<br>
   * used with {@link MathContext} 
   */
  public static int mathContextPrecision = 6;
  
  static {
    Locale[] availableLocales = ArrayUtils.addAll(Locale.getAvailableLocales(), Locale.forLanguageTag("id"), Locale.forLanguageTag("id-ID"),
        Locale.forLanguageTag("en-GB"), Locale.forLanguageTag("en-US"));
    locales = Arrays.asList(availableLocales);
  }

  private static Collection<Locale> locales;

  /**
   * @return the locales including indonesian
   */
  public static Collection<Locale> getAvailableLocales() {
    return locales;
  }

  /**
   * @param locales
   *          the locales to set
   */
  public static void setAvailableLocales(Collection<Locale> locales) {
    Utils.locales = locales;
  }

  /**
   * @param link to set
   * @param name to set
   * @return link
   */
  public static String formatLinkAnchor(String link, String name) {
    return String.format("<a target=\"%s\" href=\"%s\">%s</a> ", "_blank", link, name);
  }

  /**
   * min time year 1980
   */
  public static final ZonedDateTime START_OF_TIME = ZonedDateTime.of(1980, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
  /**
   * max time year 2200
   */
  public static final ZonedDateTime END_OF_TIME = ZonedDateTime.of(2200, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

  /**
   * Helper to check if a date is between {@value #END_OF_TIME} and {@value #START_OF_TIME}
   * 
   * @param dt
   *          {@link Date} null proof
   * @return true if data is between 1980 and 2200
   */
  public static boolean isDateInRange(Date dt) {
    if (null == dt) {
      return false;
    }
    return dt.getTime() >= START_OF_TIME.toInstant().toEpochMilli() && dt.getTime() <= END_OF_TIME.toInstant().toEpochMilli();
  }

  /**
   * 
   * @param dates list
   * @return max date
   */
  @Nullable
  public static Date maxDate(Date... dates) {
    if (null == dates) {
      return null;
    }
    HashSet<Date> set = new HashSet<Date>(dates.length);
    Collections.addAll(set, dates);
    return maxDate(set);
  }

  /**
   * 
   * @param dates list
   * @return max date
   */
  public static Date maxDate(Collection<Date> dates) {
    Long time = START_OF_TIME.toInstant().toEpochMilli();
    for (Date date : dates) {
      if (null != date) {
        time = Math.max(time, date.getTime());
      }
    }
    return new Date(time);
  }

  /**
   * 
   * @param dates list
   * @return min date
   */
  @Nullable
  public static Date minDate(Date... dates) {
    if (null == dates) {
      return null;
    }
    HashSet<Date> set = new HashSet<Date>(dates.length);
    Collections.addAll(set, dates);
    return minDate(set);
  }

  /**
   * Helper to get min date<br>
   * it will compare the date to {@value #END_OF_TIME}
   * 
   * @param dates list
   * @return min date
   */
  public static Date minDate(Collection<Date> dates) {
    Long time = END_OF_TIME.toInstant().toEpochMilli();
    for (Date date : dates) {
      if (null != date) {
        time = Math.min(time, date.getTime());
      }
    }
    return new Date(time);
  }

  /**
   * compute a date minus days without weekend included
   * 
   * @param start
   *          {@link ZonedDateTime}
   * @param days
   *          number of days to substract
   * @return {@link ZonedDateTime} the date
   */
  public static ZonedDateTime minusDaysWithoutWeekend(ZonedDateTime start, int days) {
    ZonedDateTime weekday = start.minusDays(days);
    if (start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY) {
      weekday = start.with(DayOfWeek.FRIDAY);
    }
    if (weekday.getDayOfWeek() == DayOfWeek.SUNDAY) {
      weekday = weekday.minusDays(2);
    }
    if (weekday.getDayOfWeek() == DayOfWeek.SATURDAY) {
      weekday = weekday.minusDays(1);
    }
    return weekday;
  }


  /**
   * @param start {@link Date} to set
   * @param end {@link Date} to set
   * @return to set
   */
  public static int duration(Date start, Date end) {
    if (null == start || end == null) {
      return 0;
    }
    long diff = Math.abs(end.getTime() - start.getTime());
    return (int) (diff / (24 * 60 * 60 * 1000));
  }

  /**
   * helper to address NPE and null message in exception
   * 
   * @param e
   *          {@link Exception} null proof
   * @return {@link String} never null
   */
  public static String errorMessage(Throwable e) {
    if (null != e) {
      return (null == e.getMessage() ? (e instanceof NullPointerException ? "NPE" : e.getClass().getSimpleName()) : e.getMessage());
    }
    return "not an exception";
  }

  /**
   * Helper to format a message from a {@link Pair}
   * 
   * @param pair {@link Pair}, if null return an empty string
   * @return String, if Pair is error return a string formatted as "Error: %s", otherwise the string message
   */
  public static String returnMessage(Pair<Boolean, String> pair) {
    if (null == pair) {
      return "";
    }
    return pair.getKey() ? String.format("Error: %s", pair.getValue()) : pair.getValue();
  }

  /**
   * Helper to format a message see {@link #returnMessage(Pair)}
   * 
   * @param error
   *          {@link Boolean}
   * @param message
   *          null proof
   *          {@link String}
   * @return message or empty string
   */
  public static String returnMessage(boolean error, String message) {
    return returnMessage(Pair.of(error, message == null ? "" : message));
  }

  /**
   * remove All Non Alphanumeric in a string
   * 
   * @param toClean
   *          string to process
   * @return string cleaned up
   */
  public static final String removeAllNonAlphanumeric(final String toClean) {
    if (StringUtils.isNotBlank(toClean)) {
      return Defaults.patternNonAlphanumeric.matcher(toClean).replaceAll("");
    }
    return toClean;
  }

  /**
   * replace All Non Alphanumeric in a string<br>
   * pattern:  [^\\p{L}\\p{Nd}]+
   * @param toClean
   *          string to process
   * @param replacement
   *          if null act as {@link #removeAllNonAlphanumeric(String)}
   * @return string cleaned up
   */
  public static final String replaceAllNonAlphanumeric(final String toClean, String replacement) {
    if (StringUtils.isNotBlank(toClean)) {
      return Defaults.patternNonAlphanumeric.matcher(toClean).replaceAll(replacement == null ? "" : replacement);
    }
    return toClean;
  }

  /**
   * remove Duplicate White Spaces in a string by 1 space
   * 
   * @param toClean
   *          string to process
   * @return string cleaned up
   */
  public static final String removeDuplicateWhiteSpaces(final String toClean) {
    return removeDuplicateWhiteSpaces(toClean, " ");
  }

  /**
   * remove Duplicate White Spaces in a string by 1 space
   * 
   * @param toClean
   *          string to process
   * @param replacement
   *          String replacement
   * @return string cleaned up
   */
  public static final String removeDuplicateWhiteSpaces(final String toClean, String replacement) {
    if (StringUtils.isNotBlank(toClean) && null != replacement) {
      return Defaults.patternWhitespaceMany.matcher(toClean).replaceAll(replacement);
    }
    return toClean;
  }

  /**
   * @param toClean string
   * @return string
   */
  public static final String removeNumeric(final String toClean) {
    return removeNumeric(toClean, "");
  }

  /**
   * @param toClean string
   * @param replacement to set
   * @return cleaned string
   */
  public static final String removeNumeric(final String toClean, String replacement) {
    if (StringUtils.isNotBlank(toClean) && null != replacement) {
      return toClean.replaceAll("[0-9]", replacement).trim();
    }
    return toClean;
  }

  /**
   * @param toClean string
   * @return cleaned string
   */
  public static final String removeNonNumeric(final String toClean) {
    return replaceNonNumeric(toClean, "");
  }

  /**
   * @param toClean string
   * @param replacement to set
   * @return cleaned string
   */
  public static final String replaceNonNumeric(final String toClean, String replacement) {
    if (StringUtils.isNotBlank(toClean) && null != replacement) {
      return toClean.replaceAll("\\D+", replacement).trim();
    }
    return toClean;
  }

  /**
   * remove all CR LF in a string 
   * see {@link #replaceAllCrLf(String, String)}l
   * @param toClean string
   * @return cleaned string
   */
  public static final String removeAllCrLf(final String toClean) {
    return replaceAllCrLf(toClean, null);
  }
  
  /**
   * used to address CRLF Injection logs<br>
   * pattern: [\\u000A\\u000B\\u000C\\u000D\\u0085\\u2028\\u2029]+
   * @param toClean string
   * @param replacement to replace with, if null act as {@link #removeAllCrLf(String)}
   * @return cleaned string
   */
  public static final String replaceAllCrLf(final String toClean, @Nullable String replacement) {
    if (StringUtils.isNotEmpty(toClean)) {
      return Defaults.patternCrLf.matcher(toClean).replaceAll(replacement == null ? "" : replacement);
    }
    return toClean;
  }
  
  /**
   * check if a mobile phone number is valid for indonesia
   * 
   * @param phone
   *          null proof
   * @return true if match
   */
  public static boolean isValidIndoNumber(String phone) {
    if (StringUtils.isBlank(phone)) {
      return false;
    }
    return Defaults.patternMobileIndo.matcher(phone).find();
  }

  /**
   * parsing from available locales and accept language and pick one <br>
   * BCP 47 Validator {@linkplain http://schneegans.de/lv/?tags=en-GB&format=text}
   * 
   * @param language to set
   * @return locale if found, null otherwise
   */
  public static Locale getLocale(String language) {
    Locale locale = Locale.ENGLISH;
    if (StringUtils.isNotBlank(language)) {
      List<LanguageRange> languages = Locale.LanguageRange.parse(language);
      locale = Locale.lookup(languages, locales);
    }
    return locale;
  }

  /**
   * normalize phone number by prepending 62, and removing 0
   * @param phoneNumber nullable
   * @return normalize phone number or null
   */
  public static String normalizePhoneNumber(String phoneNumber) {
    if (StringUtils.startsWith(phoneNumber, "0")) {
      phoneNumber = StringUtils.replaceOnce(phoneNumber, "0", "62");
    }
    phoneNumber = StringUtils.prependIfMissing(phoneNumber, "62");
    return phoneNumber;
  }
  
  /**
   * <p>Uses <code>ReflectionToStringBuilder</code> to generate a
   * <code>toString</code> for the specified object in multi line.</p>
   *  Output:<b>
   *  <br>houseNb: Kav 45-46
   *  <br>street: Jalan Jendral Sudirman
   *  </b><br>
   *  NB to exclude a field use @org.apache.commons.lang3.builder.ToStringExclude
   * @param object  the Object to be output
   * @return the String result
   * @see ReflectionToStringBuilder#toString(Object,ToStringStyle)
   */
  public static String toStringFormatted(Object object) {
    object = ObjectUtils.firstNonNull(object, "");
    String str = ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE);
    str = StringUtils.substringAfter(str, "[");
    str = StringUtils.removeEnd(str, "]").trim();
    str = RegExUtils.replaceAll(str, "=", ": ");
    return RegExUtils.replaceAll(str, ",", ", ");
  }
  
  /**
   * <p>Uses <code>ReflectionToStringBuilder</code> to generate a
   * <code>toString</code> for the specified object in multi line.</p>
   *  Output:<b>
   *  <br>houseNb: Kav 45-46
   *  <br>street: Jalan Jendral Sudirman
   *  </b><br>
   *  NB to exclude a field use @org.apache.commons.lang3.builder.ToStringExclude
   * @param object  the Object to be output
   * @param excludeFieldNames Sets the field names to exclude
   * @return the String result
   * @see ReflectionToStringBuilder#toString(Object,ToStringStyle)
   */
  public static String toStringFormatted(Object object, String... excludeFieldNames) {
    object = ObjectUtils.firstNonNull(object, "");
    ReflectionToStringBuilder builder = new ReflectionToStringBuilder(object, ToStringStyle.MULTI_LINE_STYLE, null, null, false, false);
    builder.setExcludeFieldNames(excludeFieldNames);
    String str = builder.toString();
    str = StringUtils.substringAfter(str, "[");
    str = StringUtils.removeEnd(str, "]").trim();
    str = RegExUtils.replaceAll(str, "=", ": ");
    return RegExUtils.replaceAll(str, ",", ", ");
  }

  /**
   * helper to parse date to {@link LocalDateTime}
   * @param dtf {@link DateTimeFormatter}
   * @param date to parse
   * @return {@link LocalDateTime}
   */
  public static LocalDateTime toLocalDateTime(DateTimeFormatter dtf, String date) {
    return LocalDateTime.from(dtf.parse(date));
  }
  
  /**
   * helper to parse date to {@link ZonedDateTime}
   * @param dtf {@link DateTimeFormatter}
   * @param date to parse
   * @return {@link ZonedDateTime}
   */
  public static ZonedDateTime toZonedDateTime(DateTimeFormatter dtf, String date) {
    return ZonedDateTime.from(dtf.parse(date));
  }
  
  /**
   * Helper to get the date in UTC properly formatted
   * @param ref Nullable {@link ZonedDateTime} reference
   * @return UTC DateTime or empty string if ref is null
   */
  public static String dcsDateTime(ZonedDateTime ref) {
    if (null == ref) {
      return "";
    }
    ZonedDateTime zdt = ZonedDateTime.ofInstant(ref.toInstant(), ZoneId.of("UTC"));
    return Defaults.DATE_TIME_NANO_FORMATTER.format(zdt);
  }
  
  /**
   * create a unique uniform Id: prefix, 2 last digit of current year, day in year, random number
   * @param prefix to prepend
   * @return ID in shape of prefixYYDDD99999
   */
  public static String createUniqueId(@Nullable String prefix) {
    LocalDate now = LocalDate.now(Defaults.defaultZoneId);
    return String.format("%s%02d%03d%05d", StringUtils.defaultIfBlank(prefix, ""), now.getYear() - 2000, now.getDayOfYear(), RandomUtils.nextInt(1000, 99999));
  }
  
  /**
   * create a unique uniform Id: 2 last digit of current year, day in year, random number
   * @see {@link #createUniqueId(String)} for unique id with prefix
   * @return ID in shape of prefixYYDDD99999
   */
  public static String createUniqueId() {
    return createUniqueId(null);
  }

  /**
   * compute VAT for a price
   * @param price price without VAT
   * @param vat in % as 10%
   * @return price with VAT
   */
  public static BigDecimal computeTax(BigDecimal price, Float vat) {
    return price.add(price.multiply(BigDecimal.valueOf(vat / 100))).round(new MathContext(mathContextPrecision, RoundingMode.HALF_UP));
  }
  
  /**
   * compute VAT for a price
   * @param price price without VAT
   * @param vat in 0 < vat < 1 or 20% /100
   * @return price with VAT
   */
  public static BigDecimal computeTax1(BigDecimal price, Float vat) {
  	Objects.requireNonNull(price, "price cannot be null for conversion");
  	Objects.requireNonNull(vat, "VAT cannot be null for conversion");
    return price.add(price.multiply(BigDecimal.valueOf(vat))).round(new MathContext(mathContextPrecision, RoundingMode.HALF_UP));
  }
  
  /**
   * 
   * @param bytes ie Runtime.getRuntime().totalMemory()
   * @param si true for SI units, false for binary units
   * @return for 452984832:   SI 453.0 MB,  Binary 432.0 MiB
   */
  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) {
      return bytes + " B";
    }
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }
  
  /**
   * Searches for the property with the specified key in a property file. If
   * the key is not found in this property file, the default property list, and
   * its defaults, recursively, are then checked. The method returns the default
   * value argument if the property is not found.
   * 
   * @param propFile     properties file located in classpath
   * @param key          the hashtable key.
   * @param defaultValue a default value.
   * @return {@link Collection} of values
   */
  public static Collection<String> loadApplicationPropertiesKey(String propFile, String key, String defaultValue) {
    String value;
    final Properties properties = new Properties();
    try (final InputStream stream = new ClassPathResource(propFile).getInputStream()) {
      properties.load(stream);
      value = properties.getProperty(key, defaultValue);
    } catch (Exception e) {
      value = defaultValue;
    }
    return Arrays.asList(value.split(","));
  }
  
  /**
   * rewrite with {@link List} because {@link CompletableFuture} allOf take [] as constructor
   * <br> see question stackoverflow 30025428
   * 
   * @see java.util.concurrent.CompletableFuture#allOf(CompletableFuture...)
   * @param list of {@link CompletableFuture}
   * @return a new CompletableFuture that is completed when all of the
   * given CompletableFutures complete
   */
  public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> list) {
    return CompletableFuture.allOf(list.toArray(new CompletableFuture<?>[list.size()]))
        .thenApply(v -> list.stream().map(CompletableFuture::join).collect(Collectors.toList()));
  }
  
  /**
   * Convert Kilocalorie (th) to Kilojoule 
   * @param <T> Number
   * @param kcal to convert
   * @return value in Kilojoule
   */
  public static <T extends Number> Double convertKcal2kJoule(@Nonnull T kcal){
		return Double.valueOf(kcal.doubleValue() * 4.184);
  }
  
  /**
   * Convert Kilojoule to Kilocalorie (th)
   * @param <T> Number
   * @param kj to convert
   * @return value in Kilocalorie
   */
  public static <T extends Number> Double convertkJoule2Kcal(@Nonnull T kj){
		return Double.valueOf(kj.doubleValue() * 0.2390057361);
  }
}
