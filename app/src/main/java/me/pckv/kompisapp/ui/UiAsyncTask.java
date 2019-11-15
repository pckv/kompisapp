package me.pckv.kompisapp.ui;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import me.pckv.kompisapp.data.HttpStatusException;

public abstract class UiAsyncTask<T> extends AsyncTask<Void, Void, T> {

    private HttpStatusException exception;
    private MutableLiveData<TaskResult<T>> liveData;

    public UiAsyncTask(MutableLiveData<TaskResult<T>> liveData) {
        this.exception = null;
        this.liveData = liveData;
    }

    @Override
    protected T doInBackground(Void... voids) {
        try {
            return doInBackground();
        } catch (HttpStatusException e) {
            exception = e;
            return null;
        }
    }

    abstract protected T doInBackground() throws HttpStatusException;

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);

        if (exception == null) {
            liveData.setValue(new TaskResult<>(result));
        } else {
            liveData.setValue(new TaskResult<T>(exception));
        }
    }
}
