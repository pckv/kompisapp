package me.pckv.kompisapp.data;

import java.net.MalformedURLException;
import java.net.URL;


public class Endpoints {

    private static final String HOST = "http://kompis.pckv.me:8080/";

    public static final String CREATE_USER = HOST + "users";
    public static final String AUTHORIZE = HOST + "users/authorize";
    public static final String GET_USER = HOST + "users/%d";
    public static final String GET_CURRENT_USER = HOST + "users/current";
    public static final String DELETE_CURRENT_USER= HOST + "users/current";

    public static final String GET_LISTINGS = HOST + "listings";
    public static final String CREATE_LISTING = HOST + "listings";
    public static final String GET_LISTING = HOST + "listings/%d";
    public static final String DELETE_LISTING = HOST + "listings/%d";
    public static final String ACTIVATE_LISTING = HOST + "listings/%d/activate";
    public static final String DEACTIVATE_LISTING = HOST + "listings/%d/deactivate";
    public static final String ASSIGN_LISTING = HOST + "listings/%d/assign";
    public static final String UNASSIGN_LISTING = HOST + "listings/%d/unassign";

    public static URL resolve(String endpoint) {
        try {
            return new URL(endpoint);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("URL was malformed when resolving API endpoint");
        }
    }

    public static URL resolve(String endpoint, long id) {
        return resolve(String.format(endpoint, id));
    }
}
