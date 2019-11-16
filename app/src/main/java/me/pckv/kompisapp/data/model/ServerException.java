package me.pckv.kompisapp.data.model;

import java.util.Locale;

import lombok.Data;

@Data
public class ServerException {

    private String error;
    private String message;
    private String path;
    private int status;
    private long timestamp;

    public String toFormattedMessage() {
        return String.format(Locale.getDefault(), "%d %s: %s", status, error, message);
    }
}
