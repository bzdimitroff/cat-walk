package net.rdyonline.catwalk.data.api;

import net.rdyonline.catwalk.data.converter.SimpleXmlConverter;
import retrofit.converter.Converter;

/**
 * There was already a snippet on GitHub for converting XML, so I just nabbed
 * it from here:
 * 
 * https://github.com/mobile-professionals/retrofit-simplexmlconverter
 * 
 * @author Ben Pearson
 *
 */
public class XmlApi extends WebApi {

	public XmlApi(String domain) {
		super(domain);
	}

	@Override
	protected Converter getConverter() {
		return new SimpleXmlConverter();
	}

}
