package net.rdyonline.catwalk.data.api.cat;

import java.util.List;

import net.rdyonline.catwalk.data.Image;
import retrofit.http.GET;
import retrofit.http.Query;

/***
 * Retrofit friendly interface for defining the interactions with the Cat API
 * 
 * @author Ben Pearson
 * 
 */
public interface ICat {

	@GET("/api/images/get?format=xml&results_per_page=20&size=med&type=png")
	ResponseWrapper getCats(@Query("page") int page);

	/**
	 * The XML that comes back has various wrappers around the list of images
	 * that comes back, so we wrap it in the response wrapper and data nodes
	 * which it expects for direct data binding
	 * 
	 * @author Ben Pearson
	 *
	 */
	public class ResponseWrapper {

		public Data data;
	}

	public class Data {
		public List<Image> images;
	}
}
