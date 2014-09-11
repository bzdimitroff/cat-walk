package net.rdyonline.catwalk;

import java.util.List;

import net.rdyonline.catwalk.data.Image;
import net.rdyonline.catwalk.data.api.cat.CatApi;
import net.rdyonline.catwalk.tasks.SafeASyncTask;
import net.rdyonline.catwalk.ui.RoundedTransformation;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/***
 * The activity is pretty straight forward, so I didn't see the need to add in
 * any fragments to contain the logic for the sake of having fragments.
 */
public class DisplayCatActivity extends Activity {

	private int mCurrentPage = 1;
	private int mPositionInPage = 0;

	private ViewGroup mLoadingContainer;
	private TextView mLoadingText;
	private ImageView mCatImage;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!isConnected()) {
				mCatImage.setVisibility(View.INVISIBLE);
				mLoadingContainer.setVisibility(View.VISIBLE);
				mLoadingText.setText(context.getText(R.string.no_network));
			} else {
				mLoadingText.setText(context.getText(R.string.loading));
			}
		}
	};

	/**
	 * Without a network connection the app doesn't work at all, so the state
	 * needs to be determined so we know whether we can load cats
	 * 
	 * @return true if either WiFi or mobile data is available
	 */
	// TODO(benp) move to utils class
	private boolean isConnected() {
		ConnectivityManager conMngr = (ConnectivityManager) this
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = conMngr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = conMngr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		return (null != wifi && wifi.isConnected())
				|| (null != mobile && mobile.isConnected());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display_cat);
		bindViews();
		setListeners();

		final IntentFilter filters = new IntentFilter();
		filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
		filters.addAction("android.net.wifi.STATE_CHANGE");
		registerReceiver(mReceiver, filters);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isConnected())
			loadNextCat();
	}

	/**
	 * Can use dependency injection library like ButterKnige here for views if
	 * you want, but there isn't very much UI, so I've just bound them manually
	 */
	private void bindViews() {
		mLoadingContainer = (ViewGroup) findViewById(R.id.loading_container);
		mLoadingText = (TextView) findViewById(R.id.loading_text);
		mCatImage = (ImageView) findViewById(R.id.cat);
	}

	private void setListeners() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadNextCat();
			}
		};

		mCatImage.setOnClickListener(listener);
	}

	private void loadNextCat() {
		showLoadingContainer();
		mLoadingText.setText(this.getString(R.string.loading));

		SafeASyncTask<List<Image>> task = new SafeASyncTask<List<Image>>(this) {

			@Override
			protected List<Image> onRun() {
				// TODO(benp) should be loaded in from Config..
				return new CatApi("http://thecatapi.com").getPage(mCurrentPage);
			}

			@Override
			protected void onSuccess(List<Image> result) {
				if (result != null) {
					updateImage(getContext(), result.get(mPositionInPage));
				} else {
					// TODO(benp) swap this out for checking network state
					String error = getContext().getString(R.string.error);
					Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)
							.show();
				}
			}
		};

		task.execute();
	}
	
	private void showImage() {
		Animation shrink = AnimationUtils.loadAnimation(this, R.anim.shrink);
		mLoadingContainer.startAnimation(shrink);
		
		Animation grow = AnimationUtils.loadAnimation(this, R.anim.grow);
		mCatImage.startAnimation(grow);
	}
	
	private void showLoadingContainer() {
		Animation shrink = AnimationUtils.loadAnimation(this, R.anim.shrink);
		mCatImage.startAnimation(shrink);
		
		Animation grow = AnimationUtils.loadAnimation(this, R.anim.grow);
		mLoadingContainer.startAnimation(grow);
		
	}

	/**
	 * There's a new image available for the users perusal
	 * 
	 * @param context
	 * @param image
	 */
	private void updateImage(Context context, Image image) {
		Picasso.with(context).load(image.url)
				.transform(new RoundedTransformation()).into(mCatImage);
		
		showImage();

		incrementPosition();
	}

	/**
	 * We need to keep track of where the user is through the list of cats the
	 * API is returning
	 * 
	 * At some point, the API will stop returning lists of cats, but that isn't
	 * accounted for in this implementation of the app. With more time, it's
	 * something that should be considered
	 */
	private void incrementPosition() {
		if (mPositionInPage == 20 - 1) {
			mCurrentPage++;
			mPositionInPage = 0;
		} else {
			mPositionInPage++;
		}
	}
}
