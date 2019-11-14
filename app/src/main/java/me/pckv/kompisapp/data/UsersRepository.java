package me.pckv.kompisapp.data;

import lombok.Getter;
import me.pckv.kompisapp.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of createUser status and user credentials information.
 */
public class UsersRepository {

    private static volatile UsersRepository instance;

    private UsersDataSource dataSource;

    @Getter
    private LoggedInUser user;

    private UsersRepository() {
        this.dataSource = new UsersDataSource();
    }

    public static UsersRepository getInstance() {
        if (instance == null) {
            instance = new UsersRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    public String getToken() {
        if (isLoggedIn()) {
            return user.getToken();
        }

        // Just use empty token for now
        return "";
    }

    public void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result<LoggedInUser> login(String email, String password) {
        Result<LoggedInUser> result = dataSource.login(email, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }

        return result;
    }

    public DatalessResult createUser(String displayName, String email, String password) {
        return dataSource.createUser(displayName, email, password);
    }
}
