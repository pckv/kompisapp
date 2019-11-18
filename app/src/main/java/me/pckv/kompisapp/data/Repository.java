package me.pckv.kompisapp.data;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.List;

import me.pckv.kompisapp.data.model.CreateListing;
import me.pckv.kompisapp.data.model.CreateUser;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.Location;
import me.pckv.kompisapp.data.model.LoggedInUser;
import me.pckv.kompisapp.data.model.LoginUser;
import me.pckv.kompisapp.data.model.ServerException;
import me.pckv.kompisapp.data.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static volatile Repository instance = new Repository();

    private KompisService service;
    private LoggedInUser loggedInUser;

    private Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kompis.pckv.me:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(KompisService.class);
    }

    public static Repository getInstance() {
        return instance;
    }

    private String getAuthorization() {
        if (loggedInUser == null) {
            return "";
        }

        return "Bearer " + loggedInUser.getToken();
    }

    public User getLoggedInAsUser() {
        return new User(loggedInUser.getId(), loggedInUser.getDisplayName());
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public void logout() {
        loggedInUser = null;
    }

    public boolean isOwner(Listing listing) {
        return loggedInUser.getId() == listing.getOwner().getId();
    }

    public boolean isAssignee(Listing listing) {
        if (listing.getAssignee() == null) {
            return false;
        }

        return loggedInUser.getId() == listing.getAssignee().getId();
    }

    private <T> Response<T> execute(Call<T> call) throws HttpStatusException {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new HttpStatusException(e.getMessage());
        }

        if (!response.isSuccessful()) {
            try {
                ServerException serverException = JSON.parseObject(response.errorBody().string(), ServerException.class);
                throw new HttpStatusException(response.code(), serverException.toFormattedMessage());
            } catch (IOException e) {
                throw new HttpStatusException(response.code(), "Unknown reason");
            }
        }

        return response;
    }

    public User createUser(String displayName, String email, String password) throws HttpStatusException {
        return execute(service.createUser(new CreateUser(displayName, email, password))).body();
    }

    public LoggedInUser authorize(String email, String password) throws HttpStatusException {
        Response<User> response = execute(service.authorize(new LoginUser(email, password)));

        String authorization = response.headers().get("Authorization");
        if (authorization == null) {
            throw new HttpStatusException("Authorize was successful but no Authorization provided");
        }

        String token = authorization.replace("Bearer ", "");
        User user = response.body();

        loggedInUser = new LoggedInUser(token, user.getId(), email, user.getDisplayName());
        return loggedInUser;
    }

    public LoggedInUser authenticate(LoggedInUser loggedInUser) throws HttpStatusException {
        Response<User> response = execute(service.getCurrentUser("Bearer " + loggedInUser.getToken()));

        // Response was successful, which means we're authenticated
        User user = response.body();

        // Update the logged in user with potentially new data from API
        this.loggedInUser = new LoggedInUser(loggedInUser.getToken(), user.getId(), loggedInUser.getEmail(), user.getDisplayName());
        return this.loggedInUser;
    }

    public List<Listing> getListings() throws HttpStatusException {
        return execute(service.getListings(getAuthorization())).body();
    }

    public Listing createListing(String title, Location location, boolean driver) throws HttpStatusException {
        return execute(service.createListing(getAuthorization(), new CreateListing(title, location, driver))).body();
    }

    public void deleteListing(long listingId) throws HttpStatusException {
        execute(service.deleteListing(getAuthorization(), listingId));
    }

    public void activateListing(long listingId) throws HttpStatusException {
        execute(service.activateListing(getAuthorization(), listingId));
    }

    public void deactivateListing(long listingId) throws HttpStatusException {
        execute(service.deactivateListing(getAuthorization(), listingId));
    }

    public void assignListing(long listingId) throws HttpStatusException {
        execute(service.assignListing(getAuthorization(), listingId));
    }

    public void unassignListing(long listingId) throws HttpStatusException {
        execute(service.unassignListing(getAuthorization(), listingId));
    }
}
