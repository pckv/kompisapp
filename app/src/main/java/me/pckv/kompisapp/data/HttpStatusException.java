package me.pckv.kompisapp.data;

import lombok.Getter;

public class HttpStatusException extends Exception {

    @Getter
    private int status;

    @Getter
    private String message;

    public HttpStatusException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatusException(String message) {
        this(-1, message);
    }
}
