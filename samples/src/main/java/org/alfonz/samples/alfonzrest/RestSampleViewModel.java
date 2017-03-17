package org.alfonz.samples.alfonzrest;

import android.databinding.ObservableField;

import org.alfonz.rest.HttpException;
import org.alfonz.rest.call.CallManager;
import org.alfonz.rest.call.Callback;
import org.alfonz.samples.SamplesApplication;
import org.alfonz.samples.alfonzmvvm.BaseViewModel;
import org.alfonz.samples.alfonzrest.entity.RepoEntity;
import org.alfonz.samples.alfonzrest.rest.RestHttpLogger;
import org.alfonz.samples.alfonzrest.rest.RestResponseHandler;
import org.alfonz.samples.alfonzrest.rest.provider.RepoServiceProvider;
import org.alfonz.utility.NetworkUtility;
import org.alfonz.view.StatefulLayout;

import retrofit2.Call;
import retrofit2.Response;


public class RestSampleViewModel extends BaseViewModel<RestSampleView>
{
	public final ObservableField<StatefulLayout.State> state = new ObservableField<>();
	public final ObservableField<RepoEntity> repo = new ObservableField<>();

	private CallManager mCallManager = new CallManager(new RestResponseHandler(), new RestHttpLogger());


	@Override
	public void onStart()
	{
		super.onStart();

		// load data
		if(repo.get() == null) loadData();
	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();

		// cancel async tasks
		if(mCallManager != null) mCallManager.cancelRunningCalls();
	}


	public void refreshData()
	{
		loadData();
	}


	private void loadData()
	{
		if(NetworkUtility.isOnline(SamplesApplication.getContext()))
		{
			String callType = RepoServiceProvider.REPO_CALL_TYPE;
			if(!mCallManager.hasRunningCall(callType))
			{
				// show progress
				state.set(StatefulLayout.State.PROGRESS);

				// enqueue call
				Call<RepoEntity> call = RepoServiceProvider.getService().repo("petrnohejl", "Alfonz");
				RepoCallback callback = new RepoCallback(mCallManager);
				mCallManager.enqueueCall(call, callback, callType);
			}
		}
		else
		{
			// show offline
			state.set(StatefulLayout.State.OFFLINE);
		}
	}


	private void setState(ObservableField<RepoEntity> data)
	{
		if(data.get() != null)
		{
			state.set(StatefulLayout.State.CONTENT);
		}
		else
		{
			state.set(StatefulLayout.State.EMPTY);
		}
	}


	private class RepoCallback extends Callback<RepoEntity>
	{
		public RepoCallback(CallManager callManager)
		{
			super(callManager);
		}


		@Override
		public void onSuccess(Call<RepoEntity> call, Response<RepoEntity> response)
		{
			repo.set(response.body());
			setState(repo);
		}


		@Override
		public void onError(Call<RepoEntity> call, HttpException exception)
		{
			handleError(mCallManager.getHttpErrorMessage(exception));
			setState(repo);
		}


		@Override
		public void onFail(Call<RepoEntity> call, Throwable throwable)
		{
			handleError(mCallManager.getHttpErrorMessage(throwable));
			setState(repo);
		}
	}
}
