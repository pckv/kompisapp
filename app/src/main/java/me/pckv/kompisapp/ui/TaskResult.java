package me.pckv.kompisapp.ui;

import lombok.Getter;
import me.pckv.kompisapp.data.HttpStatusException;

public class TaskResult<T> {

    @Getter
    private T success;

    @Getter
    private HttpStatusException error;

    public TaskResult(T success) {
        this.success = success;
        this.error = null;
    }

    public TaskResult(HttpStatusException error) {
        this.error = error;
        this.success = null;
    }

    public boolean isSuccess() {
        return success != null;
    }

    public boolean isError() {
        return error != null;
    }
}
