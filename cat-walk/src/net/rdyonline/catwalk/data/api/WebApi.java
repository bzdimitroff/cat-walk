package net.rdyonline.catwalk.data.api;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.Converter;

/***
 * Core web API that wraps Retrofit.
 * 
 * @author Ben Pearson
 * 
 */
public abstract class WebApi {

	protected String mDomain = null;

	private RestAdapter mAdapter;

	/***
	 * Construct the the base url that you will be using for web requests
	 * 
	 * @param domain
	 *            root URL with a trailing slash
	 */
	public WebApi(String domain) {
		mDomain = domain;

		// sanitize the domain string passed in
		if (!domain.endsWith("/"))
			domain += "/";

		mAdapter = new RestAdapter.Builder().setEndpoint(domain)
				.setConverter(getConverter())
				.setErrorHandler(new ErrorHandler() {

					@Override
					public Throwable handleError(RetrofitError err) {

						return parseError(err);
					}
				}).build();
	}

	/**
	 * This class needs to be extended and you need to provide your converter
	 * You can provide any RetroFit converter, e.g. Json or Xml
	 * 
	 * @return new instance of a RetroFit {@link Converter}
	 */
	protected abstract Converter getConverter();

	/**
	 * I've highlighted a couple of edge cases that could happen and you would
	 * likely want to handle. If I was spending more than a few hours on this
	 * app I would handle them more gracefully. You could easily pass them back
	 * to the UI using an EventBus
	 * 
	 * @param cause
	 * @return return the error that has passed in - this gives you the
	 *         opportunity to modify it if you want before sending it back
	 */
	private Throwable parseError(RetrofitError cause) {
		Response r = cause.getResponse();

		if (r != null && r.getStatus() == 503) {
			// might get this if there is a network connection, but they can't
			// connect,
			// e.g. they are on "the cloud" WiFi and not yet authenticated
		}

		if (r != null && r.getStatus() == 400) {
			// there was an error here and the server told us it was a bad
			// request.
			// this might mean that there's some JSON to parse to work
			// out what the error was all about
		}

		return cause;
	}

	protected <T> T newInstance(Class<T> type) {
		return (T) mAdapter.create(type);
	}
}
