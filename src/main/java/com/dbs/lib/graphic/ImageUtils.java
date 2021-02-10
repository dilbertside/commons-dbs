/**
 * ImageUtils
 */
package com.dbs.lib.graphic;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author dbs on Mar 14, 2018 4:28:39 PM
 * @since 1.0.0
 * @version 1.0
 *
 */
@lombok.experimental.UtilityClass
public class ImageUtils {
 
  /**
   * helper to scale up/down a picture
   * @param original {@link BufferedImage}
   * @param newWidth to set
   * @param newHeight to set
   * @return {@link BufferedImage} resized
   */
  public static BufferedImage scale(BufferedImage original, int newWidth, int newHeight) {
    if (original.getWidth() == newWidth && original.getHeight() == newHeight) {
      return original;
    }
    BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
    Graphics2D g = resized.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
    g.dispose();
    return resized;
  }
  
  /**
   * convert byte[] to a BufferedImage
   * 
   * @param raw byte array
   * @return {@link BufferedImage}
   * @throws IOException
   */
  public static BufferedImage fromBytes(byte[] raw) throws IOException {
  	return ImageIO.read(new ByteArrayInputStream(raw));
  }
  
  /**
   * convert BufferedImage to byte[]
   * 
   * @param image {@link BufferedImage}
   * @param format {@link ImageFormat}
   * @return byte array
   * @throws IOException
   */
  public static byte[] toBytes(BufferedImage image, ImageFormat format) throws IOException {
  	ByteArrayOutputStream baos = new ByteArrayOutputStream();
  	ImageIO.write(image, format.getFormat(), baos);
    return baos.toByteArray();
  }
  
}
