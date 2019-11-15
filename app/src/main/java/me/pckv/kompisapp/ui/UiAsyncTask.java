package me.pckv.kompisapp.ui;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import me.pckv.kompisapp.data.HttpStatusException;

public abstract class UiAsyncTask<Result> extends AsyncTask<Void, Void, Result> {

    private HttpStatusException exception;
    private MutableLiveData<TaskResult<Result>> liveData;

    public UiAsyncTask(MutableLiveData<TaskResult<Result>> liveData) {
        this.exception = null;
        this.liveData = liveData;
    }

    @Override
    protected Result doInBackground(Void... voids) {
        try {
            return doInBackground();
        } catch (HttpStatusException e) {
            exception = e;
            return null;
        }
    }

    abstract protected Result doInBackground() throws HttpStatusException;

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (exception == null) {
            liveData.setValue(new TaskResult<>(result));
        } else {
            liveData.setValue(new TaskResult<Result>(exception));
        }
    }
}
