/**
 * ReplacedElementFactoryImpl
 */
package com.dbs.lib.graphic.pdf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

/**
 * Used with flying-saucer-pdf-openpdf library
 * @author dbs at 19 Mar 2021 00:28:33
 * @since 1.1.2
 * @version 1.0
 */
@lombok.extern.slf4j.Slf4j
public class ReplacedElementFactoryImpl implements ReplacedElementFactory {

	@Override
	public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth,
	    int cssHeight) {
		Element e = box.getElement();
		if (e == null) {
			return null;
		}
		String nodeName = e.getNodeName();
		// Look for img tag in the HTML
		if (nodeName.equals("img")) {
			String imagePath = e.getAttribute("src");
			log.debug("imagePath {} ",  imagePath);
			FSImage fsImage;
			try {
				fsImage = getImageInstance(uac.getBaseURL(), imagePath);
			} catch (BadElementException | IOException e1) {
				fsImage = null;
			}
			if (fsImage != null) {
					if (cssWidth != -1 || cssHeight != -1) {
						fsImage.scale(cssWidth, cssHeight);
					} else {
						fsImage.scale(fsImage.getWidth() * 1000, fsImage.getHeight() * 1000);
					}
				return new ITextImageElement(fsImage);
			}
		}
		return null;
	}

	private FSImage getImageInstance(final String _baseUrl, final String imagePath) throws IOException, BadElementException {
		FSImage fsImage = null;
		URI result;
		try {
			result = new URI(_baseUrl);
			Resource res = null;
			if (result.isAbsolute()) {
				res = new UrlResource(_baseUrl + imagePath);
			} else {
				String pathImage = _baseUrl + StringUtils.substringBeforeLast(imagePath, "-") + "." + StringUtils.substringAfterLast(imagePath, ".");
				res = new ClassPathResource(pathImage);
			}
			final byte[] bytes = IOUtils.toByteArray(res.getInputStream());
			final Image image = Image.getInstance(bytes);
			fsImage = new ITextFSImage(image);
		} catch (URISyntaxException e) {
			log.error("ReplacedElementFactoryImpl::getImageInstance", e);
		}
		return fsImage;
	}

	@Override
	public void reset() {

	}

	@Override
	public void remove(Element e) {

	}

	@Override
	public void setFormSubmissionListener(FormSubmissionListener listener) {

	}

}
