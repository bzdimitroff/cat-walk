package net.rdyonline.catwalk.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/***
 * Prefer the image to have rounded corners
 * 
 * @author Ben Pearson
 * 
 */
public class RoundedTransformation implements
		com.squareup.picasso.Transformation {

	private final int IMAGE_PADDING = 20;
	private final float IMAGE_ROUNDING_RADIUS = 5f;

	@Override
	public Bitmap transform(final Bitmap bitmap) {
		Bitmap bmp;

		bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap,
				BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

		float radius = Math.min(bitmap.getWidth(), bitmap.getHeight())
				/ IMAGE_ROUNDING_RADIUS;
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);

		RectF rect = new RectF(IMAGE_PADDING, IMAGE_PADDING, bitmap.getWidth()
				- (IMAGE_PADDING), bitmap.getHeight() - (IMAGE_PADDING));
		canvas.drawRoundRect(rect, radius, radius, paint);

		// the original must be recycled to avoid chewing up memory
		bitmap.recycle();

		return bmp;
	}

	@Override
	public String key() {
		return "rounded";
	}
}