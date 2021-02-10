/**
 * ImageFormat
 */
package com.dbs.lib.graphic;

/**
 * @author lch at 14 Jan 2021 23:59:32
 * @since 1.0.0
 * @version 1.0
 */
@lombok.Getter
public enum ImageFormat {
	png("png", "image/png"),
	jpeg("jpeg", "image/jpeg"),
	bmp("bmp", "image/bmp"),
	wbmp("wbmp", "image/vnd.wap.wbmp"),
	gif("gif", "image/gif");
	
	String format;
	String mimeType;
	/**
	 * @param format
	 * @param mimeType
	 */
	private ImageFormat(String format, String mimeType) {
		this.format = format;
		this.mimeType = mimeType;
	}
}
