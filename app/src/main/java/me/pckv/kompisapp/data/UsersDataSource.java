package me.pckv.kompisapp.data;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import me.pckv.kompisapp.Endpoints;
import me.pckv.kompisapp.data.model.CreateUser;
import me.pckv.kompisapp.data.model.LoggedInUser;
import me.pckv.kompisapp.data.model.LoginUser;
import me.pckv.kompisapp.data.model.User;

/**
 * Class that handles authentication w/ createUser credentials and retrieves user information.
 */
public class UsersDataSource {

    public Result<LoggedInUser> login(String email, String password) {
        HttpURLConnection connection = null;

        try {
            URL url = Endpoints.resolve(Endpoints.AUTHORIZE);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");

            LoginUser loginUser = new LoginUser(email.trim(), password.trim());
            String json = JSON.toJSONString(loginUser);

            // Write the JSON object
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] dataBytes = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(dataBytes);
            }

            int status = connection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                // Read back the JSON object of the user received
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                User user = JSON.parseObject(br.readLine(), User.class);
                connection.getInputStream().close();

                String token = connection.getHeaderField("Authorization").replace("Bearer ", "");

                return new Result.Success<>(new LoggedInUser(token, email, user.getDisplayName()));
            } else {
                return new Result.Error(new IOException("Error logging in: " + connection.getResponseMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

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
