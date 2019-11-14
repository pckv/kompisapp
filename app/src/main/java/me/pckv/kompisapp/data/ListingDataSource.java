package me.pckv.kompisapp.data;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import me.pckv.kompisapp.Endpoints;
import me.pckv.kompisapp.data.model.CreateListing;

public class ListingDataSource {

    public DatalessResult createListing(String token, String title, boolean driver) {
        HttpURLConnection connection = null;

        try {
            URL url = Endpoints.resolve(Endpoints.CREATE_LISTING);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");

            String jsonListing = JSON.toJSONString(new CreateListing(title.trim(), driver));

            // Write the JSON object
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] dataBytes = jsonListing.getBytes(StandardCharsets.UTF_8);
                outputStream.write(dataBytes);
            }

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                return new DatalessResult.Success();
            } else {
                return new DatalessResult.Error(new IOException("Error creating listing: " + connection.getResponseMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatalessResult.Error(new IOException("Error creating listing", e));
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
