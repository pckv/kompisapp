package me.pckv.kompisapp.data;

import java.util.List;

import me.pckv.kompisapp.data.model.CreateListing;
import me.pckv.kompisapp.data.model.CreateUser;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.Location;
import me.pckv.kompisapp.data.model.LoginUser;
import me.pckv.kompisapp.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KompisService {

    @POST("/users")
    Call<User> createUser(@Body CreateUser user);

    @POST("/users/authorize")
    Call<User> authorize(@Body LoginUser user);

    @GET("/users/current")
    Call<User> getCurrentUser(@Header("Authorization") String auth);

    @PUT("/users/current/firebase")
    Call<Void> putFirebaseToken(@Header("Authorization") String auth, @Query("token") String token);

    @GET("/listings")
    Call<List<Listing>> getListings(@Header("Authorization") String auth);

    @POST("/listings")
    Call<Listing> createListing(@Header("Authorization") String auth, @Body CreateListing listing);

    @DELETE("/listings/{id}")
    Call<Void> deleteListing(@Header("Authorization") String auth, @Path("id") long listingId);

    @GET("/listings/{id}/activate")
    Call<Void> activateListing(@Header("Authorization") String auth, @Path("id") long listingId);

    @GET("/listings/{id}/deactivate")
    Call<Void> deactivateListing(@Header("Authorization") String auth, @Path("id") long listingId);

    @POST("/listings/{id}/assign")
    Call<Void> assignListing(@Header("Authorization") String auth, @Path("id") long listingId, @Body Location location);

    @GET("/listings/{id}/unassign")
    Call<Void> unassignListing(@Header("Authorization") String auth, @Path("id") long listingId);
}
