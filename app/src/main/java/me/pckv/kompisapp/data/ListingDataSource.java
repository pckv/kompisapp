package me.pckv.kompisapp.data;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import me.pckv.kompisapp.Endpoints;
import me.pckv.kompisapp.data.model.CreateListing;
import me.pckv.kompisapp.data.model.Listing;

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

    public Result<List<Listing>> getListings(String token) {
        HttpURLConnection connection = null;

        try {
            URL url = Endpoints.resolve(Endpoints.GET_LISTINGS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // Read back the JSON object of the listings received
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

                List<Listing> listings = JSON.parseArray(br.readLine(), Listing.class);
                System.out.println("Found listing: " + listings.get(0));
                connection.getInputStream().close();

                return new Result.Success<>(listings);
            } else {
                return new Result.Error(new IOException("Error getting listings: " + connection.getResponseMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error getting listings", e));
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private DatalessResult performActionOnListing(String token, URL url) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                return new DatalessResult.Success();
            } else {
                return new DatalessResult.Error(new IOException("Error getting listings: " + connection.getResponseMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatalessResult.Error(new IOException("Error getting listings", e));
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public DatalessResult activateListing(String token, long listingId) {
        return performActionOnListing(token, Endpoints.resolve(Endpoints.ACTIVATE_LISTING, listingId));
    }

    public DatalessResult deactivateListing(String token, long listingId) {
        return performActionOnListing(token, Endpoints.resolve(Endpoints.DEACTIVATE_LISTING, listingId));
    }

    public DatalessResult assignListing(String token, long listingId) {
        return performActionOnListing(token, Endpoints.resolve(Endpoints.ASSIGN_LISTING, listingId));
    }

    public DatalessResult unassignListing(String token, long listingId) {
        return performActionOnListing(token, Endpoints.resolve(Endpoints.UNASSIGN_LISTING, listingId));
    }
}
