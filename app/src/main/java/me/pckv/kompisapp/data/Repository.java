package me.pckv.kompisapp.data;

import java.io.IOException;
import java.util.List;

import me.pckv.kompisapp.data.model.CreateListing;
import me.pckv.kompisapp.data.model.CreateUser;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.LoggedInUser;
import me.pckv.kompisapp.data.model.LoginUser;
import me.pckv.kompisapp.data.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {

    private static volatile Repository instance = new Repository();

    private KompisService service;
    private LoggedInUser loggedInUser;

    private Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kompis.pckv.me:8080/")
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

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public void logout() {
        loggedInUser = null;
    }

    private <T> Response<T> execute(Call<T> call) throws HttpStatusException {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new HttpStatusException(e.getMessage());
        }

        if (!response.isSuccessful()) {
            throw new HttpStatusException(response.code(), response.message());
        }

        return response;
    }

    public User createUser(String displayName, String email, String password) throws HttpStatusException {
        return execute(service.createUser(new CreateUser(displayName, email, password))).body();
    }

    public User authorize(String email, String password) throws HttpStatusException {
        Response<User> response = execute(service.authorize(new LoginUser(email, password)));

        String authorization = response.headers().get("Authorization");
        if (authorization == null) {
            throw new HttpStatusException("Authorize was successful but no Authorization provided");
        }

        String token = authorization.replace("Bearer ", "");
        User user = response.body();

        loggedInUser = new LoggedInUser(token, user.getId(), email, user.getDisplayName());
        return user;
    }

    public List<Listing> getListings() throws HttpStatusException {
        return execute(service.getListings(getAuthorization())).body();
    }

    public Listing createListing(String title, boolean driver) throws HttpStatusException {
        return execute(service.createListing(getAuthorization(), new CreateListing(title, driver))).body();
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