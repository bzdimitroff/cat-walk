package net.rdyonline.catwalk;

import java.util.List;

import net.rdyonline.catwalk.data.Image;
import net.rdyonline.catwalk.data.api.cat.CatApi;
import net.rdyonline.catwalk.tasks.SafeASyncTask;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/***
 * The activity is pretty straight forward, so I didn't see the need to add in
 * any fragments to contain the logic for the sake of having fragments.
 * 
 * 
 */
public class DisplayCatActivity extends Activity {

	private int mCurrentPage = 1;
	private int mPositionInPage = 0;

	private ImageView mCatImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display_cat);
		bindViews();
		setListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadNextCat();
	}

	private void bindViews() {
		mCatImage = (ImageView) findViewById(R.id.cat);
	}

	private void setListeners() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCatImage.setVisibility(View.INVISIBLE);
				loadNextCat();
			}
		};

		mCatImage.setOnClickListener(listener);
	}

	private void loadNextCat() {
		SafeASyncTask<List<Image>> task = new SafeASyncTask<List<Image>>(this) {

			@Override
			protected List<Image> onRun() {
				return new CatApi("http://thecatapi.com").getPage(mCurrentPage);
			}

			@Override
			protected void onSuccess(List<Image> result) {
				if (result != null) {
					updateImage(getContext(), result.get(mPositionInPage));
				} else {
					String error = getContext().getString(R.string.error);
					Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
		
		task.execute();
	}
	
	private void updateImage(Context context, Image image) {
		Picasso.with(context).load(image.url).into(mCatImage);
		mCatImage.setVisibility(View.VISIBLE);

		incrementPosition();
	}

	private void incrementPosition() {
		if (mPositionInPage == 20 - 1) {
			mCurrentPage++;
			mPositionInPage = 0;
		} else {
			mPositionInPage++;
		}
	}
}
