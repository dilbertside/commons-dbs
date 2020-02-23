/**
 * Defaults
 */
package com.dbs.lib;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;

/**
 * @author dbs on Aug 21, 2016 3:10:28 PM
 * @since 1.0.0
 * @version 1.0
 * @version 1.1 add static from ConstantsDto
 * @version 1.2 add {@link #patternCrLf}
 *
 */
@lombok.experimental.UtilityClass
public class Defaults {

  /**
   * | field separator used for CSV replacement which does not collide with ,
   */
  public static final String FIELD_SEPARATOR = "|";
  
  /**
   * example<br>
   * float success, total;<br>
   *  String.format("%.2f%%", (success / (total == 0 ? 1 : total)) * 100)
   */
  public static final String STRING_FORMAT_PERCENT = "%.2f%%";

  public static Pattern patternNonAlphanumeric;

  public static Pattern patternWhitespaceMany;

  public static Pattern patternStrictNumeric;

  /**
   * this pattern will detect any non characters not allowed except apha-numeric and . - _ and space
   */
  public static final Pattern patternInvalidCharacter;
  
  /**
   * this pattern will detect any non characters not allowed except apha-numeric and . - _ space ( ) { } [ ]
   */
  public static final Pattern patternInvalidCharacterExtended;
  
  public static final String REGEX_MOBILE_INDO = "^(((0|((\\+)?62([- ])?))|((\\((\\+)?62\\)([- ])?)))?[8-9]\\d{7,12})?$";
  /**
   * alternative is to use google phone library
   */
  public static final Pattern patternMobileIndo;
  
  /**
   * Linebreak matcher Any Unicode linebreak CR FL sequence, \u000D\u000A|[\u000A\u000B\u000C\u000D\u0085\u2028\u2029] 
   */
  public static final String PATTERN_CR_LF = "[\\u000A\\u000B\\u000C\\u000D\\u0085\\u2028\\u2029]+";
  
  public static final Pattern patternCrLf;
  

  public static final String DATE_PATTERN_DDMMYYYY = "ddMMyyyy";

  public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
  
  /**
   * format for MMM dd, yyyy hh:mm:ss a
   * <br>ex: 
   * Oct 30, 2017 12:00:00 AM
   */
  public static final String DATE_PATTERN_BSS_BILLING = "MMM dd, yyyy hh:mm:ss a";

  public static final DateTimeFormatter DATE_FORMATTER_DDMMYYYY;

  public static final DateTimeFormatter DATE_FORMATTER_YYYYMMDD;

  public static final String DATE_PATTERN_PARTNER = "yyyy-MM-dd'T'HH:mm:ssZ";
  
  public static final String DATE_TIME_NANO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.n";
  
  public static final DateTimeFormatter DATE_FORMATTER_PARTNER;
  
  public static final DateTimeFormatter DATE_TIME_NANO_FORMATTER;
  
  /**
   * ISO-8601 format {@code uuuu-MM-dd}.
   */
  public static final String DATE_ISO_8601 = " uuuu-MM-dd";
  
  public static final DateTimeFormatter DATE_ISO_8601_FORMATTER;
  
  /**
   * {@link #DATE_PATTERN_BSS_BILLING}
   */
  public static final DateTimeFormatter DATE_FORMATTER_BSS_BILLING;

  /**
   * Asia/Jakarta
   */
  public static final ZoneId TZ_JAKARTA = ZoneId.of("Asia/Jakarta");
  
  /**
   * time zone UTC or GMT
   */
  public static final ZoneId TZ_UTC = ZoneId.of("UTC");

  /**
   * default is {@value #TZ_JAKARTA}
   */
  static ZoneId defaultZoneId;

  static {
    
    defaultZoneId = TZ_JAKARTA;
    
    patternNonAlphanumeric = Pattern.compile("[^\\p{L}\\p{Nd}]+");

    patternWhitespaceMany = Pattern.compile("\\s+");

    patternStrictNumeric = Pattern.compile("[^\\d]");
    
    patternInvalidCharacter = Pattern.compile("[^\\w\\- \\.]+");
    
    patternInvalidCharacterExtended = Pattern.compile("[^\\w\\- \\.\\(\\)\\{\\}\\[\\]]+");
    
    patternMobileIndo = Pattern.compile(REGEX_MOBILE_INDO);
    
    patternCrLf = Pattern.compile(PATTERN_CR_LF);
    
    DATE_FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern(DATE_PATTERN_DDMMYYYY);

    DATE_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern(DATE_PATTERN_YYYYMMDD);
    
    DATE_FORMATTER_BSS_BILLING = DateTimeFormatter.ofPattern(DATE_PATTERN_BSS_BILLING);
    
    DATE_TIME_NANO_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_NANO_PATTERN);
    
    DATE_FORMATTER_PARTNER = DateTimeFormatter.ofPattern(DATE_PATTERN_PARTNER);
    
    DATE_ISO_8601_FORMATTER = DateTimeFormatter.ofPattern(DATE_ISO_8601);
  }


  /**
   * default suffix reply queue msg to add for non request/reply pattern communication
   */
  public static final String JMS_QUEUE_REPLY_SUFFIX = "-reply";


  // Regex for acceptable logins
  public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

  
  /**
   * application/csp-report;charset=UTF-8
   */
  public static final String APPLICATION_CSP_REPORT_DATA_VALUE = "application/csp-report;charset=UTF-8";
  
  /**
   * application/zip
   */
  public static final String APPLICATION_ZIP_DATA_VALUE = "application/zip";
  
  /**
   * text/csv 
   * RFC http://tools.ietf.org/html/rfc7111
   */
  public static final String TEXT_CSV_DATA_VALUE = "text/csv";
  
  /**
   * {@link MediaType} application/csp-report;charset=UTF-8
   */
  public static final MediaType APPLICATION_CSP_REPORT_DATA = MediaType.valueOf(APPLICATION_CSP_REPORT_DATA_VALUE);
  
  /**
   * {@link MediaType} application/zip
   */
  public static final MediaType APPLICATION_ZIP_DATA = MediaType.valueOf(APPLICATION_ZIP_DATA_VALUE);
  
  /**
   * text/csv 
   * RFC http://tools.ietf.org/html/rfc7111
   */
  public static final MediaType TEXT_CSV_ZIP_DATA = MediaType.valueOf(TEXT_CSV_DATA_VALUE);

  /**
   * @return the defaultZoneId
   */
  public static ZoneId getDefaultZoneId() {
    return defaultZoneId;
  }

  /**
   * default {@link #defaultZoneId}
   * @param defaultZoneId the defaultZoneId to set
   */
  public static void setDefaultZoneId(ZoneId defaultZoneId) {
    Defaults.defaultZoneId = defaultZoneId;
  }
  
}
