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

    public static <T> void executeAndUpdate(MutableLiveData<TaskResult<T>> liveData, RepositoryTask<T> task) {
        new UiAsyncTask<T>(liveData) {

            @Override
            protected T doInBackground() throws HttpStatusException {
                return task.execute();
            }
        }.execute();
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
            onSuccess(result);
        } else {
            liveData.setValue(new TaskResult<>(exception));
        }
    }

    /**
     * Called when the task was successful.
     *
     * @param result the result object
     */
    protected void onSuccess(T result) {
    }

    public interface RepositoryTask<T> {

        T execute() throws HttpStatusException;
    }
}
