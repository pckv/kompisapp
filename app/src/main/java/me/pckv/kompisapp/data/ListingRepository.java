package me.pckv.kompisapp.data;

import java.util.List;

import me.pckv.kompisapp.data.model.Listing;

public class ListingRepository {

    private static volatile ListingRepository instance;

    private ListingDataSource listingDatasource;
    private UsersRepository usersRepository;

    public ListingRepository() {
        this.listingDatasource = new ListingDataSource();
        this.usersRepository = UsersRepository.getInstance();
    }

    public static ListingRepository getInstance() {
        if (instance == null) {
            instance = new ListingRepository();
        }

        return instance;
    }

    public DatalessResult createListing(String title, boolean driver) {
        return listingDatasource.createListing(usersRepository.getToken(), title, driver);
    }

    public Result<List<Listing>> getListings() {
        return listingDatasource.getListings(usersRepository.getToken());
    }
}
