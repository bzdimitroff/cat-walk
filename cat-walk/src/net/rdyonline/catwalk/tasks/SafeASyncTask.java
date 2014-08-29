package net.rdyonline.catwalk.tasks;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Once the task has been run, make sure that the Activity hasn't been disposed
 * or isn't being disposed to avoid null pointers where a Context being passed
 * around it used.
 * 
 * @author Ben Pearson
 * 
 * @param <T>
 *            result to be passed back from {@link ASyncTask}
 */
abstract public class SafeASyncTask<T> extends AsyncTask<Void, Void, T> {

	WeakReference<Activity> weakActivity;

	public SafeASyncTask(Activity activity) {
		weakActivity = new WeakReference<Activity>(activity);
	}

	@Override
	protected final T doInBackground(Void... voids) {
		return onRun();
	}

	protected Context getContext() {
		return weakActivity.get();
	}

	private boolean canContinue() {
		Activity activity = weakActivity.get();
		return activity != null && activity.isFinishing() == false;
	}

	@Override
	protected void onPostExecute(T t) {
		if (canContinue()) {
			onSuccess(t);
		}
	}

	abstract protected T onRun();

	abstract protected void onSuccess(T result);
}