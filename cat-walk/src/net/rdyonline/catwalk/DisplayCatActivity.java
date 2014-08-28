package net.rdyonline.catwalk;

import java.util.List;

import net.rdyonline.catwalk.data.Image;
import net.rdyonline.catwalk.data.api.cat.CatApi;
import net.rdyonline.catwalk.tasks.SafeASyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
		new SafeASyncTask<List<Image>>(this) {

			@Override
			protected List<Image> onRun() {
				return new CatApi("http://thecatapi.com").getPage(mCurrentPage);
			}

			@Override
			protected void onSuccess(List<Image> result) {
				Image image = result.get(mPositionInPage);
				Picasso.with(this.getContext()).load(image.url).into(mCatImage);

				mCatImage.setVisibility(View.VISIBLE);

				if (mPositionInPage == 20 - 1) {
					mCurrentPage++;
					mPositionInPage = 0;
				} else {
					mPositionInPage++;
				}
			}
		}.execute();
	}

}
