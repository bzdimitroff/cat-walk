package net.rdyonline.catwalk.data.api.cat;

import java.util.List;

import net.rdyonline.catwalk.data.Image;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ICat {

	
	@GET("/api/images/get?format=xml&results_per_page=20&size=med&type=png")
	ResponseWrapper getCats(@Query("page") int page);
	
	public class ResponseWrapper {
		
		public Data data;
	}
	
	public class Data {
		public List<Image> images;
	}
}
