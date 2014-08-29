package net.rdyonline.catwalk.data.api.cat;

import java.util.List;

import net.rdyonline.catwalk.data.Image;
import net.rdyonline.catwalk.data.api.XmlApi;
import net.rdyonline.catwalk.data.api.cat.ICat.ResponseWrapper;
import retrofit.RetrofitError;
import android.util.Log;

public class CatApi extends XmlApi {

	private String TAG = CatApi.class.getSimpleName();
	
	public CatApi(String domain) {
		super(domain);
	}

	public List<Image> getPage(int pageNumber) {
		final ResponseWrapper wrapper;

		try {
			wrapper = newInstance(ICat.class).getCats(pageNumber);
		} catch (RetrofitError e) {
			Log.e(TAG, e.getMessage());
			return null;
		}

		return parseResponse(wrapper);
	}

	/***
	 * Look inside the wrapper and extract the images
	 * 
	 * @param wrapper
	 *            the direct result of binding
	 * @return list of POJO
	 */
	private List<Image> parseResponse(ResponseWrapper wrapper) {
		return wrapper.data.images;
	}
}
