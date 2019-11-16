package me.pckv.kompisapp.data;

import java.util.List;

import me.pckv.kompisapp.data.model.CreateListing;
import me.pckv.kompisapp.data.model.CreateUser;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.LoginUser;
import me.pckv.kompisapp.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KompisService {

    @POST("/users")
    Call<User> createUser(@Body CreateUser user);

    @POST("/users/authorize")
    Call<User> authorize(@Body LoginUser user);

    @GET("/users/current")
    Call<User> getCurrentUser(@Header("Authorization") String auth);

    @GET("/listings")
    Call<List<Listing>> getListings(@Header("Authorization") String auth);

    @POST("/listings")
    Call<Listing> createListing(@Header("Authorization") String auth, @Body CreateListing listing);

    @GET("/listings/{id}/activate")
    Call activateListing(@Header("Authorization") String auth, @Path("id") long listingId);

    @GET("/listings/{id}/deactivate")
    Call deactivateListing(@Header("Authorization") String auth, @Path("id") long listingId);

    @GET("/listings/{id}/assign")
    Call assignListing(@Header("Authorization") String auth, @Path("id") long listingId);

    @GET("/listings/{id}/unassign")
    Call unassignListing(@Header("Authorization") String auth, @Path("id") long listingId);
}
