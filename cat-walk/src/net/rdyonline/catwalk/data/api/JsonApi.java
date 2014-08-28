package net.rdyonline.catwalk.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/***
 * I prefer APIs to provide JSON for many reasons, so would strongly encourage
 * the designer of the API to provide a JSON version. (I've voted for this
 * feature on the cat API, you should too!)
 * 
 * @author Ben Pearson
 *
 */
public class JsonApi extends WebApi {

	protected Gson mGson = buildGsonObject();

	public JsonApi(String domain) {
		super(domain);
	}

	@Override
	protected Converter getConverter() {
		return new GsonConverter(buildGsonObject());
	}

	protected Gson buildGsonObject() {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();

		return builder.create();
	}
}
