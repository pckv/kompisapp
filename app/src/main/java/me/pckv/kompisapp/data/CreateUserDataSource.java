package me.pckv.kompisapp.data;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import me.pckv.kompisapp.Endpoints;
import me.pckv.kompisapp.data.model.CreateUser;

public class CreateUserDataSource {

    public DatalessResult createUser(String displayName, String email, String password) {
        HttpURLConnection connection = null;

        try {
            URL url = Endpoints.resolve(Endpoints.CREATE_USER);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");

            String jsonUser = JSON.toJSONString(new CreateUser(displayName.trim(), email.trim(), password.trim()));

            // Write the JSON object
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] dataBytes = jsonUser.getBytes(StandardCharsets.UTF_8);
                outputStream.write(dataBytes);
            }

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                return new DatalessResult.Success();
            } else {
                return new DatalessResult.Error(new IOException("Error creating user: " + connection.getResponseMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatalessResult.Error(new IOException("Error logging in", e));
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
